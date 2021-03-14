package com.sixpoints.status.controller;

import com.sixpoints.entity.status.UserStatusVO;
import com.sixpoints.status.service.UserStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/status")
public class UserStatusController {
    @Resource
    private UserStatusService userStatusService;

    @RequestMapping("/list")
    @ResponseBody
    public List<UserStatusVO> getStatusList(){
        return userStatusService.getUserStatusList();
    }
}
