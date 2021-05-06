package com.sixpoints.rule.service;

import com.sixpoints.entity.rule.Rule;
import com.sixpoints.rule.dao.RuleDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class RuleService {
    @Resource
    private RuleDao ruleDao;

    @Cacheable(value = {"rule"},key = "#root.methodName")
    public String getRule(){
        //查询规则
        Optional<Rule> ruleOptional = ruleDao.findById(1);
        //按字符串返回
        String str = "";
        if(ruleOptional.isPresent()){
            str+=(ruleOptional.get().getRuleInfo());
        }
        return str;
    }
}
