package com.sixpoints.userproblem.controller;

import com.sixpoints.entity.user.dynasty.UserProblemVO;
import com.sixpoints.userproblem.service.UserProblemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/userproblem")
public class UserProblemController {
    @Resource
    private UserProblemService userProblemService;

    @RequestMapping("/collect/{userId}/{dynastyId}/{problemId}")
    @ResponseBody
    public String collectProblem(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("problemId")int problemId){
        return "{'result':"+ userProblemService.collect(userId,dynastyId,problemId) +"}";
    }

    @RequestMapping("/uncollect/{userId}/{dynastyId}/{problemId}")
    @ResponseBody
    public String uncollectProblem(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("problemId")int problemId){
        return "{'result':"+ userProblemService.cancelCollect(userId,dynastyId,problemId) +"}";
    }

    @RequestMapping("/search/{userId}/{dynastyId}/{pageNum}/{pageSize}")
    @ResponseBody
    public List<UserProblemVO> searchCollect(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("pageNum")int pageNum,@PathVariable("pageSize")int pageSize){
        return userProblemService.searchCollect(userId,dynastyId,pageNum,pageSize);
    }

    @RequestMapping("/search/{userId}/{pageNum}/{pageSize}")
    @ResponseBody
    public List<UserProblemVO> searchCollect(@PathVariable("userId")int userId,@PathVariable("pageNum")int pageNum,@PathVariable("pageSize")int pageSize){
        return userProblemService.searchCollect(userId,pageNum,pageSize);
    }

    @RequestMapping("/count/{userId}/{dynastyId}/{pageSize}")
    @ResponseBody
    public String getPageTotalNum(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("pageSize")int pageSize){
        return "{'count':"+ userProblemService.getPageTotalNum(userId,dynastyId,pageSize) +"}";
    }

    @RequestMapping("/count/{userId}/{pageSize}")
    @ResponseBody
    public String getPageTotalNum(@PathVariable("userId")int userId,@PathVariable("pageSize")int pageSize){
        return "{'count':"+ userProblemService.getPageTotalNum(userId,pageSize) +"}";
    }

    @RequestMapping("/iscollection/{userId}/{dynastyId}/{problemId}")
    @ResponseBody
    public String isCollection(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("problemId")int problemId){
        return "{'result':"+ userProblemService.isCollect(userId,dynastyId,problemId) +"}";
    }

    @RequestMapping("/typecount/{userId}/{problemType}/{pageSize}")
    @ResponseBody
    public String getPageTotalNumByType(@PathVariable("userId")int userId,@PathVariable("problemType")int problemType,@PathVariable("pageSize")int pageSize){
        return "{'count':"+ userProblemService.getPageTotalNumByProblemType(userId,pageSize,problemType) +"}";
    }

    @RequestMapping("/searchlist/{userId}/{problemType}/{pageNum}/{pageSize}")
    @ResponseBody
    public List<UserProblemVO> searchCollectionByType(@PathVariable("userId")int userId,@PathVariable("problemType")int problemType,@PathVariable("pageNum")int pageNum,@PathVariable("pageSize")int pageSize){
        return userProblemService.getUserProblemByType(userId,problemType,pageNum,pageSize);
    }
}
