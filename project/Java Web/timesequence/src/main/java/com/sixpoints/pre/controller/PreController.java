package com.sixpoints.pre.controller;

import com.sixpoints.pre.service.PreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pre")
public class PreController {

    @Resource
    private PreService preService;

    @RequestMapping("/start")
    @ResponseBody
    public String preStart(){
        return "{'result':"+ preService.preStart() +"}";
    }

    @RequestMapping("/bloom")
    @ResponseBody
    public String bloomFilter(){
        return "{'result':"+ preService.addBloomFilter() +"}";
    }

    @RequestMapping("/solr")
    public void luceneSearch(){
        preService.addIncidentIndex();
    }
}
