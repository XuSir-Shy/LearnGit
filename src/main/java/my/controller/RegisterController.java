package my.controller;

import my.pojo.Customer;
import my.pojo.Manager;
import my.service.CustomerService;
import my.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ManagerService managerService;
    @Value("${file.address}")
    String  fileAddress;

    //用户访问的图片路径
    @Value("${file.staticPath}")
    String  upload;
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    @RequestMapping("/photo")
    public String photo(){
        return "photo";
    }
    @RequestMapping("/cregister")
    @ResponseBody
        public HashMap<String,Object> cRegister(Customer customer){
       HashMap<String,Object> map = new HashMap<String,Object>();
        String info = customerService.register(customer);
        map.put("info",info);
        return map;
    }
    @RequestMapping("/mregister")
    @ResponseBody
    public HashMap<String,Object> mRegister(Manager manager){
        HashMap<String,Object> map = new HashMap<String,Object>();
        String info = managerService.register(manager);
        map.put("info",info);
        return map;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public HashMap<String,Object> upload(MultipartFile file){
        HashMap<String,Object> map = new HashMap<String,Object>();

        //上传文件
        try {

            //定义一个文件上传的目录
            String timeDir="";
            //获取时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            timeDir = sdf.format(new Date());

            //定义上传文件的前缀，
            String pre="";
            //是为了保证文件上传后 存到服务器的文件名是唯一的
            pre = UUID.randomUUID()+"";

            //获取文件的后缀
            String hou="";
            if(file!=null){
                //.jpg
                String originalName = file.getOriginalFilename();
                hou=originalName.substring(originalName.lastIndexOf(".")+1);

            }
            //定义文件上传的全路径
            String filePath= fileAddress+"\\"+timeDir+"\\"+pre+"."+hou;

            //创建file对象
            File f = new File(filePath);

            //判断目录是否存在，不存在就创建目录
            if(!f.isDirectory()){
                //创建目录
                f.mkdirs();
            }
            //上传文件
            file.transferTo(f);
            //设置上传成功的提示信息
            map.put("code",0);
            //返回给前端 用户访问这个图片的路径
            String path = upload+"\\"+timeDir+"\\"+pre+"."+hou;
            map.put("src",path);

            return  map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置上传失败的提示信息
        map.put("code",1);

        return map;
    }


}
