package com.sixpoints.problem.controller;

import com.sixpoints.constant.Constant;
import com.sixpoints.dynasty.service.DynastyServiceToWeb;
import com.sixpoints.entity.dynasty.Problem;
import com.sixpoints.entity.dynasty.ProblemLinkLine;
import com.sixpoints.entity.dynasty.ProblemSelect;
import com.sixpoints.entity.dynasty.ProblemgetOrder;
import com.sixpoints.problem.service.ProblemServicesToWeb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/problem")
public class ProblemControllerToWeb {

    @Resource
    private DynastyServiceToWeb dynastyServiceToWeb;

    @Resource
    private ProblemServicesToWeb problemServicesToWeb;

    @RequestMapping("/list")
    public String getDynastyList(HttpSession session){
        if(session.getAttribute("dynastyList") == null){
            session.setAttribute("dynastyList",dynastyServiceToWeb.getDynastyList());
        }
        session.setAttribute("problemDynasty",1);
        return "redirect:/problem-home.html";
    }

    @RequestMapping("/search")
    public String getProblemList(@RequestParam("dynastyId") int dynastyId,@RequestParam("problemType") int problemType,HttpSession session){
        switch (problemType){
            case Constant.SELECT_PROBLEM:
                session.setAttribute("problemSelectList",problemServicesToWeb.searchSelectProblemList(dynastyId));
                break;
            case Constant.LINK_PROBLEM:
                session.setAttribute("problemLinkList",problemServicesToWeb.searchLinkProblemList(dynastyId));
                break;
            case Constant.ORDER_PROBLEM:
                session.setAttribute("problemOrderList",problemServicesToWeb.searchOrderProblemList(dynastyId));
                break;
        }
        session.setAttribute("problemType",problemType);
        session.setAttribute("problemDynasty",dynastyId);

        return "redirect:/problem-home.html";
    }

    @RequestMapping("/preadd")
    public String preAdd(int dynastyId,int problemType,HttpSession session){
        session.setAttribute("addProblemDynastyId",dynastyId);
        session.setAttribute("addProblemType",problemType);
        return "redirect:/add-problem.html";
    }

    @RequestMapping("/add/select")
    public String add(@RequestParam("dynastyId")int dynastyId,
                      @RequestParam("problemType")int problemType,
                      @RequestParam("title")String title,
                      @RequestParam("optionA")String optionA,
                      @RequestParam("optionB")String optionB,
                      @RequestParam("optionC")String optionC,
                      @RequestParam("optionD")String optionD,
                      @RequestParam("answer")String answer,
                      @RequestParam("details")String details,
                      @RequestParam("problemCreator")String problemCreator,
                      @RequestParam("optionAFile") MultipartFile optionAFile,
                      @RequestParam("optionBFile")MultipartFile optionBFile,
                      @RequestParam("optionCFile")MultipartFile optionCFile,
                      @RequestParam("optionDFile")MultipartFile optionDFile){
        ProblemSelect problemSelect = new ProblemSelect();
        problemSelect.setDynastyId(dynastyId);
        problemSelect.setProblemType(problemType);
        problemSelect.setTitle(title);
        problemSelect.setOptionA(optionA);
        problemSelect.setOptionB(optionB);
        problemSelect.setOptionC(optionC);
        problemSelect.setOptionD(optionD);
        problemSelect.setAnswer(answer);
        problemSelect.setDetails(details);
        problemSelect.setProblemCreator(problemCreator);

        List<MultipartFile> list = new ArrayList<>();
        list.add(optionAFile);
        list.add(optionBFile);
        list.add(optionCFile);
        list.add(optionDFile);

        problemServicesToWeb.problemMergeSelectAndSave(problemSelect,list);
        return "redirect:/problem-home.html";
    }

    @RequestMapping("/add/link")
    public String add(ProblemLinkLine problemLinkLine){


        problemServicesToWeb.problemMergeLinkAndSave(problemLinkLine);

        return "redirect:/problem-home.html";
    }

    @RequestMapping("/add/order")
    public String add(ProblemgetOrder problemgetOrder){
        problemServicesToWeb.problemMergeOrderAndSave(problemgetOrder);
        return "redirect:/problem-home.html";
    }

    @RequestMapping("/delete/{problemId}/{dynastyId}")
    public String delete(@PathVariable("problemId")int problemId,@PathVariable("dynastyId")int dynastyId,HttpSession session){
        problemServicesToWeb.deleteProblem(problemId,dynastyId);
        session.removeAttribute("problemSelectList");
        session.removeAttribute("problemLinkList");
        session.removeAttribute("problemOrderList");
        return "redirect:/problem-home.html";
    }

    @RequestMapping("/premodify/{problemId}")
    public String preModify(@PathVariable("problemId")int problemId,HttpSession session){
        //获取到题目并优化存入session
        //1.获取题目
        Problem problem = problemServicesToWeb.getProblem(problemId);
        if(problem == null){
            return "redirect:/problem-home.html";
        }
        switch (problem.getProblemType()){
            case Constant.SELECT_PROBLEM:
                session.setAttribute("modifyProblemType",1);
                session.setAttribute("modifyProblem",problemServicesToWeb.problemSplitSelect(problem));
                break;
            case Constant.LINK_PROBLEM:
                session.setAttribute("modifyProblemType",2);
                session.setAttribute("modifyProblem",problemServicesToWeb.problemSplitLink(problem));
                break;
            case Constant.ORDER_PROBLEM:
                session.setAttribute("modifyProblemType",3);
                session.setAttribute("modifyProblem",problemServicesToWeb.problemSplitOrder(problem));
                break;
        }
        return "redirect:/modify-problem.html";
    }

    @RequestMapping("/modify/select")
    public String modifySelect(@RequestParam("problemId")Integer problemId,
                               @RequestParam("problemType")Integer problemType,
                               @RequestParam("title")String title,
                               @RequestParam("optionA")String optionA,
                               @RequestParam("optionB")String optionB,
                               @RequestParam("optionC")String optionC,
                               @RequestParam("optionD")String optionD,
                               @RequestParam("answer")String answer,
                               @RequestParam("details")String details,
                               @RequestParam("problemCreator")String problemCreator,
                               @RequestParam("optionAFile") MultipartFile optionAFile,
                               @RequestParam("optionBFile")MultipartFile optionBFile,
                               @RequestParam("optionCFile")MultipartFile optionCFile,
                               @RequestParam("optionDFile")MultipartFile optionDFile,HttpSession session){
        ProblemSelect problemSelect = new ProblemSelect();
        problemSelect.setProblemId(problemId);
        problemSelect.setProblemType(problemType);
        problemSelect.setTitle(title);
        problemSelect.setOptionA(optionA);
        problemSelect.setOptionB(optionB);
        problemSelect.setOptionC(optionC);
        problemSelect.setOptionD(optionD);
        problemSelect.setAnswer(answer);
        problemSelect.setDetails(details);
        problemSelect.setProblemCreator(problemCreator);

        List<MultipartFile> list = new ArrayList<>();
        list.add(optionAFile);
        list.add(optionBFile);
        list.add(optionCFile);
        list.add(optionDFile);

        problemServicesToWeb.problemMergeSelectAndModify(problemSelect, (Integer) session.getAttribute("problemDynasty"),list);
        session.removeAttribute("problemSelectList");
        return "redirect:/problem-home.html";
    }

    @RequestMapping("/modify/link")
    public String modifyLink(ProblemLinkLine problemLinkLine,HttpSession session){
        System.out.printf(problemLinkLine.toString());
        problemServicesToWeb.problemMergeLinkAndModify(problemLinkLine,(Integer) session.getAttribute("problemDynasty"));
        session.removeAttribute("problemLinkList");
        return "redirect:/problem-home.html";
    }

    @RequestMapping("/modify/order")
    public String modifyOrder(ProblemgetOrder problemgetOrder,HttpSession session){
        System.out.printf(problemgetOrder.toString());
        problemServicesToWeb.problemMergeOrderAndModify(problemgetOrder,(Integer) session.getAttribute("problemDynasty"));
        session.removeAttribute("problemOrderList");
        return "redirect:/problem-home.html";
    }
}
