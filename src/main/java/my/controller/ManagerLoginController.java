package my.controller;

import my.pojo.Manager;
import my.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/manager")
public class ManagerLoginController {
    @Autowired
    ManagerService managerService;
    @RequestMapping("/customerList")
    public String customerLsit(Manager manager, ModelMap m, HttpServletRequest request){
        HashMap<String, Object> map=managerService.selectByManager(request,manager);
        //把数据传到前端
        m.put("info",map);
        return "customerListByManger";
    }
}
