package my.controller;

import my.pojo.Customer;
import my.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/customer")
public class CustomerListController {
    @Autowired
    CustomerService customerService;
    @RequestMapping("/info")
    public String list(Customer customer, ModelMap m){
        //查询分页数据
        HashMap<String, Object> map=customerService.select(customer);
        //把数据传到前端
        m.put("info",map);
        return "CustomerInfoManage";
    }
    @RequestMapping("/editPage")
    public String edit(Customer customer,ModelMap map){
        Customer c = customerService.selectByUserId(customer);
        //把数据传递到前端
        map.addAttribute("customer",c);
        return "customer-edit";
    }
    @RequestMapping("/upDate")
    @ResponseBody
    public HashMap<String,Object> update(Customer customer)
    {
        HashMap<String,Object> map = new HashMap<String,Object>();
        String info =customerService.update(customer);
        map.put("info",info);
        return map;
    }
    @RequestMapping("/kehuxiangxixinxi")
    public String kehuxiangxixinxi(Customer customer,ModelMap map,HttpServletRequest request){
        Customer cc = customerService.selectByUserId(customer);
        HttpSession session = request.getSession();
        //把数据传递到前端
        session.setAttribute("session1",cc);
        map.addAttribute("customer1",cc);
        return "kehuxiangxixinxi";
    }
    @RequestMapping("/delete")
    @ResponseBody
    public HashMap<String,Object> delete(Customer customer){
        HashMap<String,Object> map = new HashMap<>();
        String info = customerService.delete(customer);
        map.put("info",info);
        return map;
    }
    @RequestMapping("/excelWrite")
    public void excelWrite(HttpServletResponse response){

        customerService.excelWrite(response);
    }

}
