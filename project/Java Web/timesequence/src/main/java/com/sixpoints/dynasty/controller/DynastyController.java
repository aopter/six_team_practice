package com.sixpoints.dynasty.controller;

import com.sixpoints.dynasty.service.DynastyService;
import com.sixpoints.entity.dynasty.DynastyListVO;
import com.sixpoints.entity.dynasty.DynastyVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/dynasty")
public class DynastyController {

    @Resource
    private DynastyService dynastyService;

    @RequestMapping("/list")
    @ResponseBody
    public List<DynastyListVO> dynastyList(){
        return dynastyService.getDynastyList();
    }

    @RequestMapping("/details/{id}")
    @ResponseBody
    public DynastyVO dynastyDetails(@PathVariable("id") int id){
        return dynastyService.getDynastyDetailsById(id);
    }

}
