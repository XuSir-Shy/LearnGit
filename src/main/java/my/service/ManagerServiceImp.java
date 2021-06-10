package my.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import my.dao.ManagerDao;
import my.dao.RecordDao;
import my.pojo.Customer;
import my.pojo.Manager;
import my.util.InsertRecord;
import my.util.InsertRecord;
import my.util.MdFive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class ManagerServiceImp implements ManagerService{
    @Autowired(required = false)
    ManagerDao managerDao;
    @Autowired
    MdFive mdFive;
    @Autowired(required = false)
    RecordDao recordDao;
    @Autowired
    InsertRecord insertRecord;
    @Value("${spring.mail.username}")
    String sendEmail;
    @Autowired
    JavaMailSender javaMailSender;
    @Override
    public String login(Manager manager, HttpServletRequest request) {
        Manager manager1 = managerDao.selectByName(manager);
        HttpSession sessioncode = request.getSession();
        String code = sessioncode.getAttribute("valCode")+"";
        if(manager.getCode().equals(code)){
            if(manager1==null)
            {
                return "登录失败";
            }
            else {
                String pwd = mdFive.encrypt(manager.getPwd(),manager1.getSalt());
                manager.setPwd(pwd);
                Manager manager2 = managerDao.login(manager);
                if(manager2==null){
                    return "登陆失败";
                }
                else {
                    HttpSession session = request.getSession();
                    session.setAttribute("managerinfo",manager2);
                    insertRecord.insert(manager2.getName(),"经理"+manager2.getName()+"于"+insertRecord.getTime()+"成功登录");
                    return "登陆成功";
                }
            }
        }
        else return "验证码不正确";
    }

    @Override
    public String register(Manager manager) {
        if(managerDao.valName(manager)>0)
        {
            return "用户名已经被注册";
        }
        else
        {
            manager.setSalt(manager.getName());
            String pwd = mdFive.encrypt(manager.getPwd(),manager.getSalt());
            manager.setPwd(pwd);
            boolean register = managerDao.register(manager);
            if(register)
            {
                return "注册成功";
            }
            else return "注册失败";
        }
    }

    @Override
    public HashMap<String, Object> sendCode(Manager manager, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //从session中获取当前用户信息
            HttpSession session = request.getSession();
            //创建普通邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            //设置发件人邮箱
            message.setFrom(sendEmail);
            //设置收件人邮箱
            message.setTo(managerDao.selectEmail(manager));
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
    public HashMap<String, Object> selectByManager(HttpServletRequest request,Manager manager) {
        List<Customer> list = new ArrayList<>();
        HashMap<String, Object> map =  new HashMap<String, Object>();
        HttpSession session = request.getSession();
        Manager loginManager = (Manager) session.getAttribute("managerinfo");
        if(loginManager!=null)
        {
            manager.setName(loginManager.getName());
        }
        //1 设置分页参数
        PageHelper.startPage(manager.getPage(),manager.getRow());
        //2 查询数据库表数据
        if(manager.getConValue().equals(""))
        {
            list =managerDao.selectByManger(manager);
        }
        else
        {
            //根据用户选择的查询
            if(manager.getCondition().equals("编号"))
            {
                //设置用户输入的查询条件
                manager.setId(Integer.parseInt(manager.getConValue()));
                list = managerDao.findByUserId(manager);
            }
            else if(manager.getCondition().equals("用户名"))
            {
                manager.setName(manager.getConValue());
                list = managerDao.findByUserName(manager);
            }
            else
            {
                list= managerDao.selectByManger(manager);
            }
        }
        //3.把查询的数据转换成分页对象
        PageInfo<Customer> page = new PageInfo<Customer>(list);

        //获取分页的当前页的集合
        map.put("condition",manager.getCondition());
        map.put("convalue",manager.getConValue());
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
}
