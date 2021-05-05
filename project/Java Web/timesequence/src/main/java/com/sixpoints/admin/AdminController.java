package com.sixpoints.admin;

import com.sixpoints.pre.service.PreService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Resource
    private PreService preService;

    @RequestMapping("/login")
    public String adminLogin(@RequestParam("username")String username, @RequestParam("password")String password,
                             Map<String,Object> map, HttpSession session){
        if(username != null && password != null && "123456".equals(password)){
            session.setAttribute("userLogin",username);
            session.setAttribute("preMap",preService.getPre());
            return "redirect:/main.html";
        }else{
            map.put("msg","用户名或密码错误");
            return "/html/ui-login";
        }
    }
}
