package com.sixpoints.userunlockdynastyincident.service;

import com.sixpoints.constant.Constant;
import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.entity.dynasty.IncidentListVO;
import com.sixpoints.entity.status.UserStatus;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.dynasty.UserUnlockDynastyIncident;
import com.sixpoints.incident.dao.IncidentDao;
import com.sixpoints.status.dao.UserStatusDao;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.user.service.UserService;
import com.sixpoints.userunlockdynastyincident.dao.UserUnlockDynastyIncidentDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserUnlockDynastyIncidentService {

    @Resource
    private UserUnlockDynastyIncidentDao userUnlockDynastyIncidentDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private IncidentDao incidentDao;

    @Resource
    private UserService userService;

    @Resource
    private UserStatusDao userStatusDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //添加用户解锁的事件
    @Transactional
    public boolean addIncident(int userId,int dynastyId,int incidentId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.incidentIdIsExist(incidentId)){
            return false;
        }

        if(isHave(userId,dynastyId,incidentId)){
            return true;
        }
        Optional<User> userOptional = userDao.findById(userId);
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
        Optional<Incident> incidentOptional = incidentDao.findById(incidentId);
        boolean flag = false;
        if(userOptional.isPresent()&&dynastyOptional.isPresent()&&incidentOptional.isPresent()){
            addException(userId);//用户看事件加经验
            UserUnlockDynastyIncident userUnlockDynastyIncident = new UserUnlockDynastyIncident();
            userUnlockDynastyIncident.setDynasty(dynastyOptional.get());
            userUnlockDynastyIncident.setIncident(incidentOptional.get());

            //保存
            userUnlockDynastyIncidentDao.save(userUnlockDynastyIncident);
            //保存关系
            User user = userOptional.get();
            user.getUserUnlockDynastyIncidents().add(userUnlockDynastyIncident);
            userDao.save(user);
            //改变标识符
            flag = true;
        }
        return flag;
    }

    //用户观看事件加经验
    public boolean addException(int userId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return false;
        }
        Optional<User> userOptional = userDao.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setUserExperience(user.getUserExperience()+ Constant.INCIDENT_EXPERIENCE);
            //判断用户是否可以升级
            if(userService.isUp(user)){
                //升级
                Optional<UserStatus> userStatusOptional = userStatusDao.findById(user.getUserStatus().getStatusId() + 1);
                if(userStatusOptional.isPresent()){
                    user.setUserStatus(userStatusOptional.get());
                }
            }
            //保存
            userDao.save(user);
            //返回
            return true;
        }
        return false;
    }

    //查看用户解锁某个朝代事件的列表
    public List<IncidentListVO> getUnlockIncident(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        //根据用户和朝代，查询题目
        List<UserUnlockDynastyIncident> list = userUnlockDynastyIncidentDao.findUnlockList(userId,dynastyId);
        //创建集合
        List<IncidentListVO> incidentListVOS = new LinkedList<>();
        //封装事件
        for(int i=0;i < list.size();i++){
            UserUnlockDynastyIncident userUnlockDynastyIncident = list.get(i);
            Incident incident = userUnlockDynastyIncident.getIncident();
            IncidentListVO incidentListVO = new IncidentListVO(incident.getIncidentId(),incident.getIncidentName(),incident.getIncidentPicture());
            incidentListVOS.add(incidentListVO);
        }
        //返回事件
        return incidentListVOS;
    }

    //用户解锁某朝代事件的个数
    public int getUnlockIncidentCount(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return 0;
        }
        //根据用户和朝代，查询题目
        List<UserUnlockDynastyIncident> list = userUnlockDynastyIncidentDao.findUnlockList(userId,dynastyId);
        if(list != null){
            return list.size();
        }
        return 0;
    }

    //判断某个事件是否已经添加
    public boolean isHave(int userId,int dynastyId,int incidentId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.incidentIdIsExist(incidentId)){
            return false;
        }
        Optional<UserUnlockDynastyIncident> unlockDynastyIncidentOptional = userUnlockDynastyIncidentDao.findUnlockIncident(userId, dynastyId, incidentId);
        if(unlockDynastyIncidentOptional.isPresent()){
            return true;
        }
        return false;
    }
}
