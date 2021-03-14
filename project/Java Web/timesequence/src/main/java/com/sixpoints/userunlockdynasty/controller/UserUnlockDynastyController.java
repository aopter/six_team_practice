package com.sixpoints.userunlockdynasty.controller;

import com.sixpoints.entity.user.dynasty.UserUnlockDynastyVO;
import com.sixpoints.userunlockdynasty.service.UserUnlockDynastyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/userunlockdynasty")
public class UserUnlockDynastyController {

    @Resource
    private UserUnlockDynastyService userUnlockDynastyService;

    @RequestMapping("/count/{userId}/{dynastyId}")
    @ResponseBody
    public String getCount(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId){
        return "{'count':"+ userUnlockDynastyService.getTrueCount(userId,dynastyId) +"}";
    }

    @RequestMapping("/list/{userId}")
    @ResponseBody
    public List<UserUnlockDynastyVO> getList(@PathVariable("userId")int userId){
        return userUnlockDynastyService.getUnlockDynastyList(userId);
    }

    @RequestMapping("/ispass/{userId}/{dynastyId}")
    @ResponseBody
    public String isPass(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId){
        return "{'result':"+ userUnlockDynastyService.isPassDynasty(userId,dynastyId) +"}";
    }

    @RequestMapping("/addunlockdynasty/{userId}/{dynastyId}")
    @ResponseBody
    public String addUnlockDynasty(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId){
        return "{'result':"+ userUnlockDynastyService.addUnlockDynasty(userId,dynastyId) +"}";
    }
}
