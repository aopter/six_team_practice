package com.sixpoints.userrecharge.service;

import com.sixpoints.entity.recharge.Pricing;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.recharge.UserRecharge;
import com.sixpoints.recharge.dao.PricingDao;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userrecharge.dao.UserRechargeDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserRechargeService {

    @Resource
    private UserRechargeDao userRechargeDao;

    @Resource
    private UserDao userDao;

    @Resource
    private PricingDao pricingDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    /**
     * 用户充值
     * 1.查询用户
     * 2.进行充值
     * 3.返回充值结果和充值后的金额
     */
    @Transactional
    public String userRecharge(int userId,int pricingId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return "'result':false','count':0";
        }
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        //查询订单类型
        Optional<Pricing> pricingOptional = pricingDao.findById(pricingId);
        //进行判断
        if(userOptional.isPresent()&&pricingOptional.isPresent()){
            //加count
            User user = userOptional.get();
            Pricing pricing = pricingOptional.get();
            user.setUserCount(user.getUserCount()+pricing.getPricingCount());

            //保存修改
            User save = userDao.save(user);
            if(save != null){//充值成功
                UserRecharge userRecharge = new UserRecharge();
                userRecharge.setCreateTime(System.currentTimeMillis());
                userRecharge.setUser(user);
                userRecharge.setPricing(pricing);
                //数据库保存
                userRechargeDao.save(userRecharge);
                return "'result':true,'count':"+user.getUserCount();
            }
        }
        return "'result':false','count':0";
    }
}
