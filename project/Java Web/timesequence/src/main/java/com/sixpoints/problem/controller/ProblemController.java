package com.sixpoints.problem.controller;

import com.sixpoints.entity.dynasty.ProblemVO;
import com.sixpoints.problem.service.ProblemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/problem")
public class ProblemController {
    @Resource
    private ProblemService problemService;

    @RequestMapping("/count/{id}")
    @ResponseBody
    public String getCount(@PathVariable("id") int id){
        return "{count:"+ problemService.getProblemCount(id) +"}";
    }

    @RequestMapping("/ready/{dynastyId}")
    @ResponseBody
    public List<ProblemVO> readyToAnswer(@PathVariable("dynastyId")int dynastyId){
        return problemService.readyToAnswer(dynastyId);
    }

    @RequestMapping("/replenish/{problemType}/{dynastyId}")
    @ResponseBody
    public ProblemVO replenishProblem(@PathVariable("problemType")int problemType,@PathVariable("dynastyId")int dynastyId){
        return problemService.getRandomProblem(problemType,dynastyId);
    }

    @RequestMapping("/answer/{userId}/{dynastyId}/{problemId}/{result}")
    @ResponseBody
    public String userAnswer(@PathVariable("userId")int userId,@PathVariable("dynastyId")int dynastyId,@PathVariable("problemId")int problemId,@PathVariable("result")int result){
        return problemService.userAnswer(userId,dynastyId,problemId,result);
    }

}
