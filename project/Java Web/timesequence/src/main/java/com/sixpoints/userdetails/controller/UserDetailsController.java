package com.sixpoints.userdetails.controller;

import com.sixpoints.entity.user.UserDetails;
import com.sixpoints.userdetails.service.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/userdetails")
public class UserDetailsController {
    @Resource
    private UserDetailsService userDetailsService;

    @RequestMapping("/details/{userId}")
    @ResponseBody
    public UserDetails getDetails(@PathVariable("userId")int userId){
        return userDetailsService.getUserDetails(userId);
    }

    @PostMapping("/modify")
    @ResponseBody
    public String modifyUserDetails(UserDetails userDetails){
        return "{'result':"+ userDetailsService.updateUserDetails(userDetails) +"}";
    }
}
