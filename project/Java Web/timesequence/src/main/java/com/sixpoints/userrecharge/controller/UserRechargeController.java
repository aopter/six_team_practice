package com.sixpoints.userrecharge.controller;

import com.sixpoints.userrecharge.service.UserRechargeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class UserRechargeController {
    @Resource
    private UserRechargeService userRechargeService;

    @PostMapping("/recharge")
    @ResponseBody
    public String recharge(int userId,int rechargeId){
        return "{"+ userRechargeService.userRecharge(userId,rechargeId) +"}";
    }
}
