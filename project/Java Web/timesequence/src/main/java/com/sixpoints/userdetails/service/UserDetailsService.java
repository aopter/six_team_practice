package com.sixpoints.userdetails.service;

import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.UserDetails;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userdetails.dao.UserDetailsDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserDetailsService {

    @Resource
    private UserDetailsDao userDetailsDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserDao userDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //查询用户详情
    public UserDetails getUserDetails(int userId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return new UserDetails();
        }
        if(redisUtil.exists("user-details::"+userId)){
            return (UserDetails)redisUtil.get("user-details::"+userId);
        }
        //查询用户详情
        Optional<UserDetails> userDetailsOptional = userDetailsDao.findById(userId);
        if(userDetailsOptional.isPresent()){
            redisUtil.set("user-details::"+userId,userDetailsOptional.get(),(long)(60*5));
            return userDetailsOptional.get();
        }
        redisUtil.set("user-details::"+userId,userDetailsOptional.get(),(long)(60*30));
        return new UserDetails();
    }

    //修改用户详情
    @Transactional
    public boolean updateUserDetails(UserDetails userDetails){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userDetails.getUserId())){
            return false;
        }
        //修改用户信息
        UserDetails save = userDetailsDao.save(userDetails);
        if(save != null){
            User user = userDao.findById(save.getUserId()).get();
            user.setUserNickname(save.getUserNickname());
            userDao.save(user);
            //修改缓存
            redisUtil.set("user-details::"+userDetails.getUserId(),save,(long)(60*5));
            return true;
        }
        return false;
    }
}
