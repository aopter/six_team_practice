package com.sixpoints.team.controller;

import com.sixpoints.entity.team.Team;
import com.sixpoints.team.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TeamController {
    @Resource
    private TeamService teamService;

    @RequestMapping("/team")
    @ResponseBody
    public Team teamInformation(){
        return teamService.getTeam();
    }
}
