package com.sixpoints.recharge.service;

import com.sixpoints.entity.recharge.Pricing;
import com.sixpoints.recharge.dao.PricingDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PricingService {
    @Resource
    private PricingDao pricingDao;

    //获取充值类型标价
    @Cacheable(value = {"pricing"},key = "#root.methodName")
    public List<Pricing> getPricingList(){
        return pricingDao.findAll();
    }
}
