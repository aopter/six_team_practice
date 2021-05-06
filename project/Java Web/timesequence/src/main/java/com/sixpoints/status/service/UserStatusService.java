package com.sixpoints.status.service;

import com.sixpoints.entity.status.UserStatus;
import com.sixpoints.entity.status.UserStatusVO;
import com.sixpoints.status.dao.UserStatusDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserStatusService {
    @Resource
    private UserStatusDao userStatusDao;

    @Cacheable(value = {"status"},key = "#root.methodName")
    public List<UserStatusVO> getUserStatusList(){
        //查询信息
        List<UserStatus> userStatusList = userStatusDao.findAll();
        //创建集合
        List<UserStatusVO> list = new LinkedList<>();
        for(int i = 0;i<userStatusList.size();i++){
            UserStatus userStatus = userStatusList.get(i);
            UserStatusVO userStatusVO = new UserStatusVO(userStatus.getStatusId(),userStatus.getStatusName(),userStatus.getStatusExperienceLow(),userStatus.getStatusExperienceTop(),userStatus.getStatusInfo());
            list.add(userStatusVO);
        }
        return list;
    }
}
