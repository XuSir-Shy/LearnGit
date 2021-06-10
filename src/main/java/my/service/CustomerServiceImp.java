package my.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import my.dao.CustomDao;
import my.pojo.Customer;
import my.pojo.DailyRecord;
import my.util.InsertRecord;
import my.util.InsertRecord;
import my.util.MdFive;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class CustomerServiceImp implements CustomerService{
    @Autowired(required = false)
    CustomDao customDao;
    @Autowired
    MdFive mdFive;
    @Value("${spring.mail.username}")
    String sendEmail;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    InsertRecord insertRecord;
    @Override
    public String login(Customer customer, HttpServletRequest request) {
        Customer customer1 = customDao.selectByName(customer);
        HttpSession sessioncode = request.getSession();
        String code = sessioncode.getAttribute("valCode")+"";
        if(customer.getCode().equals(code)){
            if(customer1==null)
            {
                return "登录失败";
            }
            else {
                String pwd = mdFive.encrypt(customer.getPwd(),customer1.getSalt());
                customer.setPwd(pwd);
                Customer customer2 = customDao.login(customer);
                if(customer2==null){
                    return "登陆失败";
                }
                else {
                    HttpSession session = request.getSession();
                    session.setAttribute("customerInfo",customer2);
                    System.out.println("登录成功");
                    insertRecord.insert(customer2.getName(),"用户"+customer2.getName()+"于"+insertRecord.getTime()+"成功登录");
                    return "登陆成功";
                }
            }
        }
        else{
            return "验证码不正确";
        }
    }

    @Override
    public String register(Customer customer) {
        if(customDao.valName(customer)>0)
        {
            return "用户名已经被注册";
        }
        else
        {
            System.out.println(customer.getName());
            customer.setSalt(customer.getName());
            String pwd = mdFive.encrypt(customer.getPwd(),customer.getSalt());
            customer.setPwd(pwd);
            boolean register = customDao.register1(customer);
            if(register)
            {
                return "注册成功";
            }
            else return "注册失败";
        }
    }

    @Override
    public HashMap<String, Object> sendCode(Customer customer,HttpServletRequest request) {    //验证码
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //从session中获取当前用户信息
            HttpSession session = request.getSession();
            //创建普通邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            //设置发件人邮箱
            message.setFrom(sendEmail);
            //设置收件人邮箱
            message.setTo(customDao.selectEmail(customer));
            //设置邮件标题
            message.setSubject("您的登录验证码是：");
            // 生成随机验证码
            Random rd = new Random();
            String valCode = rd.nextInt(9999)+"";
            //设置邮件正文
            message.setText("你的验证码是:"+valCode);
            //发送邮件
            javaMailSender.send(message);
            //发送成功
            //把验证码存入session中
            session.setAttribute("valCode",valCode);
            map.put("info","发送成功");
            return map;

        } catch (Exception e) {
            System.out.println("发送邮件时发生异常！");
            e.printStackTrace();
        }
        map.put("info","发送邮件异常");
        return map;
    }


    @Override
    public HashMap<String, Object> select(Customer customer) {
        List<Customer> list = new ArrayList<>();
        HashMap<String, Object> map =  new HashMap<String, Object>();
        //1 设置分页参数
        PageHelper.startPage(customer.getPage(),customer.getRow());
        //2 查询数据库表数据
        if(customer.getConValue().equals(""))
        {
            list =customDao.select();
        }
        else
        {
            //根据用户选择的查询
            if(customer.getCondition().equals("编号"))
            {
                //设置用户输入的查询条件
                customer.setId(Integer.parseInt(customer.getConValue()));
                list = customDao.findByUserId(customer);
            }
            else if(customer.getCondition().equals("用户名"))
            {
                customer.setName(customer.getConValue());
                list = customDao.findByUserName(customer);
            }
            else
            {
                list =customDao.select();
            }
        }
        //3.把查询的数据转换成分页对象
        PageInfo<Customer> page = new PageInfo<Customer>(list);

        //获取分页的当前页的集合
        map.put("condition",customer.getCondition());
        map.put("convalue",customer.getConValue());
        map.put("list",page.getList());
        //总条数
        map.put("total",page.getTotal());
        //总页数
        map.put("totalPage",page.getPages());
        //上一页
        if(page.getPrePage()==0){
            map.put("pre",1);
        }else{
            map.put("pre",page.getPrePage());
        }

        //下一页
        //保持在最后一页
        if(page.getNextPage()==0){
            map.put("next",page.getPages());
        }else{
            map.put("next",page.getNextPage());
        }
        //当前页
        map.put("cur",page.getPageNum());
        map.put("numPage",page.getPageSize());
        return map;
    }

    @Override
    public String update(Customer customer) {
        Customer c1 = customDao.selectByUserId(customer);
        String oldname = c1.getName();
        String newname = customer.getName();
        if (oldname.equals(newname))
        {
            if(customDao.updateShow(customer)>0)
            {
                return "修改成功";
            }
            else return "修改失败";
        }
        else
        {
            if(customDao.valName(customer)==0)
            {
                if(customDao.updateShow(customer)>0)
                {
                    return "修改成功";
                }
                else return "修改失败";
            }
            else return "用户重名";
        }
    }

    @Override
    public Customer selectByUserId(Customer customer) {
        return customDao.selectByUserId(customer);
    }

    @Override
    public String delete(Customer customer) {
        if(customDao.delete(customer)>0)
        {
            return "删除成功";
        }
        else return "删除失败";
    }

    @Override
    public void excelWrite(HttpServletResponse response) {
        //excel导出
        OutputStream output=null;
        try {
            // 创建HSSFWorkbook对象
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建HSSFSheet对象,如果要创建多个sheet，就再创建sheet对象
            HSSFSheet sheet = wb.createSheet("用户表");

            // 创建HSSFRow对象，先写入列名
            HSSFRow colName = sheet.createRow(0);
            // 写入入第一行列名
            colName.createCell(0).setCellValue("编号");
            colName.createCell(1).setCellValue("头像地址");
            colName.createCell(2).setCellValue("用户名");
            colName.createCell(3).setCellValue("性别");
            colName.createCell(4).setCellValue("生日");
            colName.createCell(5).setCellValue("电话号码");
            colName.createCell(6).setCellValue("邮箱地址");
            colName.createCell(7).setCellValue("学历");
            colName.createCell(8).setCellValue("身份证");
            colName.createCell(9).setCellValue("公司");
            colName.createCell(10).setCellValue("所属经理");
            colName.createCell(11).setCellValue("备注");
            colName.createCell(12).setCellValue("盐值");
            colName.createCell(13).setCellValue("密码");
            //查询员工所有信息
            List<Customer> list = customDao.select();

            for (int i = 1; i <=list.size(); i++) {
                //从第二行开始写入数据
                HSSFRow row = sheet.createRow(i);
                row.createCell(0).setCellValue(list.get(i-1).getId());
                row.createCell(1).setCellValue(list.get(i-1).getPhoto());
                row.createCell(2).setCellValue(list.get(i-1).getName());
                row.createCell(3).setCellValue(list.get(i-1).getSex());
                row.createCell(4).setCellValue(list.get(i-1).getBirth());
                row.createCell(5).setCellValue(list.get(i-1).getPhone());
                row.createCell(6).setCellValue(list.get(i-1).getEaddress());
                row.createCell(7).setCellValue(list.get(i-1).getEducation());
                row.createCell(8).setCellValue(list.get(i-1).getIdcard());
                row.createCell(9).setCellValue(list.get(i-1).getCompany());
                row.createCell(10).setCellValue(list.get(i-1).getManager());
                row.createCell(11).setCellValue(list.get(i-1).getRemarks());
                row.createCell(12).setCellValue(list.get(i-1).getSalt());
                row.createCell(13).setCellValue(list.get(i-1).getPwd());
            }
            //输出Excel文件到页面
            output=response.getOutputStream();
            String fileName="用户表";
            //解决中文文件名下载 变成下划线的问题
            fileName=new String(fileName.getBytes("utf-8"),"ISO8859-1")+"";
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
            response.setContentType("application/msexcel");
            wb.write(output);

        } catch (Exception e) {

            e.printStackTrace();
        }finally{
            try {
                output.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
