package com.sixpoints.team.service;

import com.sixpoints.entity.team.Team;
import com.sixpoints.team.dao.TeamDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TeamService {
    @Resource
    private TeamDao teamDao;

    @Cacheable(value = {"team"},key = "#root.methodName")
    public Team getTeam(){
        return teamDao.findById(1).get();
    }
}
