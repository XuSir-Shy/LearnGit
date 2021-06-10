package my.service;


import my.pojo.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface CustomerService {
    String login(Customer customer, HttpServletRequest request);//登录
    String register(Customer customer);//注册
    HashMap<String,Object> sendCode(Customer customer,HttpServletRequest request);//验证码
    HashMap<String,Object> select(Customer customer);
    String update(Customer customer);
    Customer selectByUserId(Customer customer);
    String delete(Customer customer);
    void excelWrite(HttpServletResponse response);
}
