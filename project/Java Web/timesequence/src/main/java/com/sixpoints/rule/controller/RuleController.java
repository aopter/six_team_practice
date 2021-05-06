package com.sixpoints.rule.controller;

import com.sixpoints.rule.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class RuleController {
    @Resource
    private RuleService ruleService;

    @ResponseBody
    @RequestMapping("/rule")
    public String getRule(){
        return ruleService.getRule();
    }
}
