package com.sixpoints.userunlockdynastyincident.controller;

import com.sixpoints.entity.dynasty.IncidentListVO;
import com.sixpoints.userunlockdynastyincident.service.UserUnlockDynastyIncidentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/userincident")
public class UserUnlockDynastyIncidentController {

    @Resource
    private UserUnlockDynastyIncidentService userUnlockDynastyIncidentService;

    @RequestMapping("/unlock/{userId}/{dynastyId}/{incidentId}")
    @ResponseBody
    public String unlockIncident(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("incidentId")int incidentId){
        return "{'result':"+ userUnlockDynastyIncidentService.addIncident(userId,dynastyId,incidentId) +"}";
    }

    @RequestMapping("/list/{userId}/{dynastyId}")
    @ResponseBody
    public List<IncidentListVO> findUnlockIncident(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId){
        return userUnlockDynastyIncidentService.getUnlockIncident(userId,dynastyId);
    }
}
