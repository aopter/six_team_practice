package com.sixpoints.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.UserListVO;
import com.sixpoints.user.service.UserService;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    @RequestMapping("/list")
    @ResponseBody
    public List<UserListVO> getTop20(){
        return userService.getTop20();
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(User user,int flag){//flag标识是手机号注册（1），还是qq和微信注册（2）
        if(flag == 1){
            //手机号注册
            if(user.getUserAccount() != null&&!"".equals(user.getUserAccount())&&
                    user.getUserPassword() != null&&!"".equals(user.getUserPassword())){
                boolean b = userService.register(user);
                return b ? "{'result':true}":"{'result':false}";
            }
        }else if(flag == 2){
            if(user.getUserAccount() != null&&!"".equals(user.getUserAccount())){
                //判断是否已经注册过了，如果已经注册过了，那就直接登录，如果没有注册过，先注册，再登录
                boolean exits = userService.exits(user.getUserAccount());
                if (exits){
                    return JSONObject.toJSONString(userService.getUser(user.getUserAccount()));
                }

                boolean b = userService.register(user);
                return b ? JSONObject.toJSONString(userService.getUser(user.getUserAccount())):"{'result':false}";
            }
        }
        return "{'result':false}";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(User user,int flag){
        if(flag == 1){
            //手机号注册
            if(user.getUserAccount() != null&&!"".equals(user.getUserAccount())&&
                    user.getUserPassword() != null&&!"".equals(user.getUserPassword())){
                boolean b = userService.exits(user.getUserAccount(),user.getUserPassword());
                return b ? JSONObject.toJSONString(userService.getUser(user.getUserAccount(),user.getUserPassword())) :"{'result':false}";
            }
        }
        return "{'result':false}";
    }
}
