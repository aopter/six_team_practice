package com.sixpoints.userunlockdynasty.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.dynasty.UserUnlockDynasty;
import com.sixpoints.entity.user.dynasty.UserUnlockDynastyVO;
import com.sixpoints.problem.service.ProblemService;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userunlockdynasty.dao.UserUnlockDynastyDao;
import com.sixpoints.userunlockdynastyincident.service.UserUnlockDynastyIncidentService;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserUnlockDynastyService {

    @Resource
    private UserUnlockDynastyDao userUnlockDynastyDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ProblemService problemService;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private UserUnlockDynastyIncidentService userUnlockDynastyIncidentService;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //查询某个朝代答对题目的个数
    public int getTrueCount(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return 0;
        }
        //查询
        Optional<UserUnlockDynasty> un = userUnlockDynastyDao.findCount(userId, dynastyId);
        if(un.isPresent()){
            return un.get().getProgress();
        }
        return 0;
    }

    //用户查询自己解锁的朝代列表
    public List<UserUnlockDynastyVO> getUnlockDynastyList(int userId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return new LinkedList<>();
        }
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        //创建集合
        List<UserUnlockDynastyVO> list = new LinkedList<>();
        if(userOptional.isPresent()){
            //获取用户解锁朝代
            List<UserUnlockDynasty> userUnlockDynasties = userUnlockDynastyDao.findUserUnlockDynastyByUserId(userId);

            //封装信息
            for(int i=0;i<userUnlockDynasties.size();i++){
                UserUnlockDynasty userUnlockDynasty = userUnlockDynasties.get(i);
                UserUnlockDynastyVO userUnlockDynastyVO = new UserUnlockDynastyVO(userId,userUnlockDynasty.getDynasty().getDynastyId(),userUnlockDynasty.getDynasty().getDynastyName(),userUnlockDynasty.getProgress());
                list.add(userUnlockDynastyVO);
            }
        }
        return list;
    }

    //查询是否可以过关
    public boolean isPassDynasty(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return false;
        }
        //获取朝代的总题数
        int total = problemService.getProblemCount(dynastyId);
        //用户用户已经答题的个数
        int progress = getTrueCount(userId,dynastyId);
        //判断是否过关
        if(progress >= total/3*2 && total != 0){
            return true;
        }
        //获取朝代事件总数
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
        if(dynastyOptional.isPresent() == false){
            return false;
        }
        Set<Incident> incidentSet = dynastyOptional.get().getIncidents();
        int totalIncident = incidentSet.size();
        //获取用户解锁事件数目
        int progressIncident = userUnlockDynastyIncidentService.getUnlockIncidentCount(userId,dynastyId);
        //判断是否可以过关
        if(totalIncident == progressIncident && totalIncident != 0){
            return true;
        }
        return false;
    }

    //判断是否继续加积分
    public boolean isAddCount(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return false;
        }
        //获取朝代的总题数
        int total = problemService.getProblemCount(dynastyId);
        //用户用户已经答题的个数
        int progress = getTrueCount(userId,dynastyId);
        //判断是否过关
        if(progress >= total/3*2){
            return true;
        }
        return false;
    }

    //增加答对题目个数
    public void addOneProgress(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return;
        }
        //查询
        Optional<UserUnlockDynasty> un = userUnlockDynastyDao.findCount(userId, dynastyId);
        if(un.isPresent()){
            UserUnlockDynasty userUnlockDynasty = un.get();
            userUnlockDynasty.setProgress(userUnlockDynasty.getProgress()+1);
        }
    }

    //用户添加解锁朝代
    public boolean addUnlockDynasty(int userId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return false;
        }
        if(userUnlockDynastyDao.findCount(userId,dynastyId).isPresent()){
            return false;
        }
        //查询人和朝代
        Optional<User> userOptional = userDao.findById(userId);
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
        if(!userOptional.isPresent() || !dynastyOptional.isPresent()){
            return false;
        }
        //创建新对象
        User user = userOptional.get();
        Dynasty dynasty = dynastyOptional.get();
        UserUnlockDynasty userUnlockDynasty = new UserUnlockDynasty();
        userUnlockDynasty.setProgress(0);
        userUnlockDynasty.setDynasty(dynasty);
        //设置关联关系
        user.getUserUnlockDynasties().add(userUnlockDynasty);
        //保存
        userUnlockDynastyDao.save(userUnlockDynasty);
        userDao.save(user);
        return true;
    }
}
