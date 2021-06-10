package my.controller;

import my.pojo.Customer;
import my.pojo.Manager;
import my.service.CustomerService;
import my.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/login")
public class loginController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ManagerService managerService;
    @RequestMapping("loginPage")
    public String login(){
        return "login1";
    }
    @RequestMapping("/cloginAjax")//客户登录
    @ResponseBody
    public HashMap<String,Object> cloginAjax(Customer customer, HttpServletRequest request)
    {
        System.out.println(customer.getName()+customer.getPwd()+customer.getCode());
        HashMap<String,Object> map = new HashMap();
        String info = customerService.login(customer,request);
        map.put("info",info);
        return map;
    }
    @RequestMapping("/mloginAjax")//经理登录
    @ResponseBody
    public HashMap<String,Object> mloginAjax(Manager manager, HttpServletRequest request)
    {

        HashMap<String,Object> map = new HashMap();
        String info = managerService.login(manager,request);
        map.put("info",info);
        return map;
    }
    @RequestMapping("/sendEmail")
    @ResponseBody
    public HashMap<String,Object> sendEmail(Customer customer, HttpServletRequest request){
        return customerService.sendCode(customer,request);
    }
}
