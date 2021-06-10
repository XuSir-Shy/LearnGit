package my.service;


import my.pojo.Customer;
import my.pojo.Manager;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface ManagerService {
    String login(Manager manager, HttpServletRequest request);//登录
    String register(Manager manager);//注册
    HashMap<String,Object> sendCode(Manager manager, HttpServletRequest request);//验证码
    HashMap<String, Object> selectByManager(HttpServletRequest request,Manager manager);
}
