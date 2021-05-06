package com.sixpoints.recharge.controller;

import com.sixpoints.entity.recharge.Pricing;
import com.sixpoints.recharge.service.PricingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/pricing")
public class PricingController {
    @Resource
    private PricingService pricingService;

    @RequestMapping("/list")
    @ResponseBody
    public List<Pricing> getPricingList(){
        return pricingService.getPricingList();
    }
}
