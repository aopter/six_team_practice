package com.sixpoints.problem.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.*;
import com.sixpoints.picture.service.PictureService;
import com.sixpoints.problem.dao.ProblemDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemServicesToWeb {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ProblemDao problemDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private PictureService pictureService;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //获取选择题列表
    public List<ProblemSelect> searchSelectProblemList(int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+1)){
            return (List<ProblemSelect>) redisUtil.get("web-dynasty-problem-"+dynastyId+"-"+1);
        }
        //查询题目
        Optional<List<Problem>> listOptional = problemDao.getProblemList(dynastyId,1);
        List<ProblemSelect> problemSelects = new LinkedList<>();
        if(listOptional.isPresent()){
            List<Problem> list = listOptional.get();
            for(int i = 0;i < list.size();i++){
                problemSelects.add(problemSplitSelect(list.get(i)));
            }
            redisUtil.set("web-dynasty-problem-"+dynastyId+"-"+1,problemSelects);
        }
        return problemSelects;
    }

    //获取连线题列表
    public List<ProblemLinkLine> searchLinkProblemList(int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+2)){
            return (List<ProblemLinkLine>) redisUtil.get("web-dynasty-problem-"+dynastyId+"-"+2);
        }
        //查询题目
        Optional<List<Problem>> listOptional = problemDao.getProblemList(dynastyId,2);
        List<ProblemLinkLine> problemLinkLines = new LinkedList<>();
        if(listOptional.isPresent()){
            List<Problem> list = listOptional.get();
            for(int i = 0;i < list.size();i++){
                problemLinkLines.add(problemSplitLink(list.get(i)));
            }
            redisUtil.set("web-dynasty-problem-"+dynastyId+"-"+2,problemLinkLines);
        }
        return problemLinkLines;
    }

    //排序题列表
    public List<ProblemgetOrder> searchOrderProblemList(int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+3)){
            return (List<ProblemgetOrder>) redisUtil.get("web-dynasty-problem-"+dynastyId+"-"+3);
        }
        //查询题目
        Optional<List<Problem>> listOptional = problemDao.getProblemList(dynastyId,3);
        List<ProblemgetOrder> problemgetOrders = new LinkedList<>();
        if(listOptional.isPresent()){
            List<Problem> list = listOptional.get();
            for(int i = 0;i < list.size();i++){
                problemgetOrders.add(problemSplitOrder(list.get(i)));
            }
            redisUtil.set("web-dynasty-problem-"+dynastyId+"-"+3,problemgetOrders);
        }
        System.out.printf(problemgetOrders.toString());
        return problemgetOrders;
    }

    //分割选择题
    public ProblemSelect problemSplitSelect(Problem problem){
        String[] contents1 = problem.getProblemContent().split("&&&");

        ProblemSelect problemSelect = new ProblemSelect();
        problemSelect.setProblemId(problem.getProblemId());
        problemSelect.setTitle(contents1[0]);
        problemSelect.setOptionA(contents1[1]);
        problemSelect.setOptionApic(contents1[2]);
        problemSelect.setOptionB(contents1[3]);
        problemSelect.setOptionBpic(contents1[4]);
        problemSelect.setOptionC(contents1[5]);
        problemSelect.setOptionCpic(contents1[6]);
        problemSelect.setOptionD(contents1[7]);
        problemSelect.setOptionDpic(contents1[8]);
        problemSelect.setAnswer(problem.getProblemKey());
        problemSelect.setDetails(problem.getProblemDetails());
        problemSelect.setProblemCreator(problem.getProblemCreator());
        problemSelect.setProblemCreationTime(problem.getProblemCreationTime());

        return problemSelect;
    }

    //分割连线题
    public ProblemLinkLine problemSplitLink(Problem problem){
        String[] contents1 = problem.getProblemContent().split("&&&");

        ProblemLinkLine problemLinkLine = new ProblemLinkLine();
        problemLinkLine.setProblemId(problem.getProblemId());
        problemLinkLine.setTitle(contents1[0]);
        problemLinkLine.setOptionA(contents1[1]);
        problemLinkLine.setOptionB(contents1[2]);
        problemLinkLine.setOptionC(contents1[3]);
        problemLinkLine.setOptionD(contents1[4]);
        problemLinkLine.setOptionAdes(contents1[5]);
        problemLinkLine.setOptionBdes(contents1[6]);
        problemLinkLine.setOptionCdes(contents1[7]);
        problemLinkLine.setOptionDdes(contents1[8]);
        problemLinkLine.setAnswer(problem.getProblemKey());
        problemLinkLine.setDetails(problem.getProblemDetails());
        problemLinkLine.setProblemCreator(problem.getProblemCreator());
        problemLinkLine.setProblemCreationTime(problem.getProblemCreationTime());

        return problemLinkLine;
    }

    //分割排序题
    public ProblemgetOrder problemSplitOrder(Problem problem){
        String[] contents1 = problem.getProblemContent().split("&&&");

        ProblemgetOrder problemgetOrder = new ProblemgetOrder();
        problemgetOrder.setProblemId(problem.getProblemId());
        problemgetOrder.setProblemDetails(problem.getProblemDetails());
        problemgetOrder.setTitle(contents1[0]);
        problemgetOrder.setProblemCreator(problem.getProblemCreator());
        problemgetOrder.setProblemCreationTime(problem.getProblemCreationTime());
        problemgetOrder.setKey(problem.getProblemKey());
        String key = problem.getProblemKey();

        List<OrderBean> orderBeans = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            //
            OrderBean orderBean = new OrderBean();
            orderBean.setContent(contents1[i + 1]);
            orderBean.setOrder(Integer.parseInt(key.charAt(i) + ""));
            orderBeans.add(orderBean);
        }
        problemgetOrder.setContents(orderBeans);

        return problemgetOrder;
    }

    //添加选择题
    @Transactional
    public boolean problemMergeSelectAndSave(ProblemSelect problemSelect, List<MultipartFile> files){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(problemSelect.getDynastyId())){
            return false;
        }
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(problemSelect.getDynastyId());
        if(!dynastyOptional.isPresent()){
            return false;
        }
        Problem problem = new Problem();
        problem.setProblemType(problemSelect.getProblemType());
        problem.setProblemKey(problemSelect.getAnswer());
        problem.setProblemDetails(problemSelect.getDetails());
        problem.setProblemCreationTime(System.currentTimeMillis());
        problem.setProblemCreator(problemSelect.getProblemCreator());

        Problem save = problemDao.save(problem);
        if(save != null){
            int problemId = save.getProblemId();
            problemSelect.setOptionApic("/problem/pro-A-"+problemId+".png");
            problemSelect.setOptionBpic("/problem/pro-B-"+problemId+".png");
            problemSelect.setOptionCpic("/problem/pro-C-"+problemId+".png");
            problemSelect.setOptionDpic("/problem/pro-D-"+problemId+".png");
            String content = problemSelect.getTitle()+"&&&"+
                    problemSelect.getOptionA()+""+"&&&"+problemSelect.getOptionApic()+"&&&"+
                    problemSelect.getOptionB()+""+"&&&"+problemSelect.getOptionBpic()+"&&&"+
                    problemSelect.getOptionC()+""+"&&&"+problemSelect.getOptionCpic()+"&&&"+
                    problemSelect.getOptionD()+""+"&&&"+problemSelect.getOptionDpic();
            problem.setProblemContent(content);
            problemDao.save(problem);

            dynastyOptional.get().getProblems().add(problem);
            dynastyDao.save(dynastyOptional.get());

            //保存图片
            String[] name = new String[4];
            name[0] = new String("/problem/pro-A-"+problemId+".png");
            name[1] = new String("/problem/pro-B-"+problemId+".png");
            name[2] = new String("/problem/pro-C-"+problemId+".png");
            name[3] = new String("/problem/pro-D-"+problemId+".png");
            //保存图片
            for(int i=0;i <files.size();i++){
                pictureService.pictureUpload(name[i],files.get(i));
            }

            //清除相关缓存
            if(redisUtil.exists("web-dynasty-problem-"+problemSelect.getDynastyId()+"-"+1)){
                redisUtil.remove("web-dynasty-problem-"+problemSelect.getDynastyId()+"-"+1);
            }

            auxiliaryBloomFilterUtil.problemIdAdd(save.getProblemId());
            return true;
        }
        return false;
    }

    //添加连线题
    @Transactional
    public boolean problemMergeLinkAndSave(ProblemLinkLine problemLinkLine){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(problemLinkLine.getDynastyId())){
            return false;
        }
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(problemLinkLine.getDynastyId());
        if(!dynastyOptional.isPresent()){
            return false;
        }
        Problem problem = new Problem();
        problem.setProblemType(problemLinkLine.getProblemType());
        problem.setProblemKey(problemLinkLine.getAnswer());
        problem.setProblemDetails(problemLinkLine.getDetails());
        problem.setProblemCreationTime(System.currentTimeMillis());
        problem.setProblemCreator(problemLinkLine.getProblemCreator());
        String content = problemLinkLine.getTitle()+"&&&"+
                    problemLinkLine.getOptionA()+"&&&"+
                    problemLinkLine.getOptionB()+"&&&"+
                    problemLinkLine.getOptionC()+"&&&"+
                    problemLinkLine.getOptionD()+"&&&"+
                    problemLinkLine.getOptionAdes()+"&&&"+
                    problemLinkLine.getOptionBdes()+"&&&"+
                    problemLinkLine.getOptionCdes()+"&&&"+
                    problemLinkLine.getOptionDdes();
        problem.setProblemContent(content);
        Problem save = problemDao.save(problem);
        if(save != null){
            dynastyOptional.get().getProblems().add(problem);
            dynastyDao.save(dynastyOptional.get());

            //清除相关缓存
            if(redisUtil.exists("web-dynasty-problem-"+problemLinkLine.getDynastyId()+"-"+2)){
                redisUtil.remove("web-dynasty-problem-"+problemLinkLine.getDynastyId()+"-"+2);
            }
            auxiliaryBloomFilterUtil.problemIdAdd(save.getProblemId());
            return true;
        }

        return false;
    }

    //添加排序题
    @Transactional
    public boolean problemMergeOrderAndSave(ProblemgetOrder problemgetOrder){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(problemgetOrder.getDynastyId())){
            return false;
        }
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(problemgetOrder.getDynastyId());
        if(!dynastyOptional.isPresent()){
            return false;
        }
        Problem problem = new Problem();
        problem.setProblemType(problemgetOrder.getProblemType());
        problem.setProblemKey(problemgetOrder.getKey());
        problem.setProblemDetails(problemgetOrder.getProblemDetails());
        problem.setProblemCreationTime(System.currentTimeMillis());
        problem.setProblemCreator(problemgetOrder.getProblemCreator());
        String content = problemgetOrder.getTitle()+"&&&"+problemgetOrder.getProblemContent();
        problem.setProblemContent(content);
        Problem save = problemDao.save(problem);
        if(save != null){
            dynastyOptional.get().getProblems().add(problem);
            dynastyDao.save(dynastyOptional.get());
            //清除相关缓存
            if(redisUtil.exists("web-dynasty-problem-"+problemgetOrder.getDynastyId()+"-"+3)){
                redisUtil.remove("web-dynasty-problem-"+problemgetOrder.getDynastyId()+"-"+3);
            }
            auxiliaryBloomFilterUtil.problemIdAdd(save.getProblemId());
            return true;
        }
        return false;
    }


    //删除题目
    @Transactional
    public boolean deleteProblem(int problemId,int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemId)){
            return false;
        }
        Optional<Problem> problemOptional = problemDao.findById(problemId);
        if(problemOptional.isPresent()){
            int problemType = problemOptional.get().getProblemType();
            problemDao.delete(problemOptional.get());

            //删除相关缓存
            if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+problemType)){
                redisUtil.remove("web-dynasty-problem-"+dynastyId+"-"+problemType);
            }

            return true;
        }
        return false;
    }

    //获取题目
    public Problem getProblem(int problemId){
        if(!auxiliaryBloomFilterUtil.problemIsExist(problemId)){

            return null;
        }
        Optional<Problem> problemOptional = problemDao.findById(problemId);
        if(problemOptional.isPresent()){
            return problemOptional.get();
        }
        return null;
    }

    //修改选择题
    @Transactional
    public boolean problemMergeSelectAndModify(ProblemSelect problemSelect,Integer dynastyId,List<MultipartFile> files){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemSelect.getProblemId())){
            return false;
        }
        Optional<Problem> problemOptional = problemDao.findById(problemSelect.getProblemId());
        if(!problemOptional.isPresent()){
            return false;
        }
        Problem problem = problemOptional.get();
        problem.setProblemKey(problemSelect.getAnswer());
        problem.setProblemDetails(problemSelect.getDetails());
        problem.setProblemCreator(problemSelect.getProblemCreator());

        int problemId = problem.getProblemId();
        problemSelect.setOptionApic("/problem/pro-A-"+problemId+".png");
        problemSelect.setOptionBpic("/problem/pro-B-"+problemId+".png");
        problemSelect.setOptionCpic("/problem/pro-C-"+problemId+".png");
        problemSelect.setOptionDpic("/problem/pro-D-"+problemId+".png");
        String content = problemSelect.getTitle()+"&&&"+
                problemSelect.getOptionA()+""+"&&&"+problemSelect.getOptionApic()+"&&&"+
                problemSelect.getOptionB()+""+"&&&"+problemSelect.getOptionBpic()+"&&&"+
                problemSelect.getOptionC()+""+"&&&"+problemSelect.getOptionCpic()+"&&&"+
                problemSelect.getOptionD()+""+"&&&"+problemSelect.getOptionDpic();
        problem.setProblemContent(content);
        problemDao.save(problem);

        //保存图片
        String[] name = new String[4];
        name[0] = new String("/problem/pro-A-"+problemId+".png");
        name[1] = new String("/problem/pro-B-"+problemId+".png");
        name[2] = new String("/problem/pro-C-"+problemId+".png");
        name[3] = new String("/problem/pro-D-"+problemId+".png");
        //保存图片
        for(int i=0;i <files.size();i++){
            pictureService.pictureUpload(name[i],files.get(i));
        }

        //清除相关缓存
        if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+1)){
            redisUtil.remove("web-dynasty-problem-"+dynastyId+"-"+1);
        }
        return true;
    }

    //修改连线题
    @Transactional
    public boolean problemMergeLinkAndModify(ProblemLinkLine problemLinkLine,int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemLinkLine.getProblemId())){
            return false;
        }
        Optional<Problem> problemOptional = problemDao.findById(problemLinkLine.getProblemId());
        if(!problemOptional.isPresent()){
            return false;
        }
        Problem problem = problemOptional.get();
        problem.setProblemKey(problemLinkLine.getAnswer());
        problem.setProblemDetails(problemLinkLine.getDetails());
        problem.setProblemCreator(problemLinkLine.getProblemCreator());
        String content = problemLinkLine.getTitle()+"&&&"+
                problemLinkLine.getOptionA()+"&&&"+
                problemLinkLine.getOptionB()+"&&&"+
                problemLinkLine.getOptionC()+"&&&"+
                problemLinkLine.getOptionD()+"&&&"+
                problemLinkLine.getOptionAdes()+"&&&"+
                problemLinkLine.getOptionBdes()+"&&&"+
                problemLinkLine.getOptionCdes()+"&&&"+
                problemLinkLine.getOptionDdes();
        problem.setProblemContent(content);
        Problem save = problemDao.save(problem);
        //清除相关缓存
        if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+2)){
            redisUtil.remove("web-dynasty-problem-"+dynastyId+"-"+2);
        }
        return true;
    }

    //修改排序题
    @Transactional
    public boolean problemMergeOrderAndModify(ProblemgetOrder problemgetOrder,int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemgetOrder.getProblemId())){
            return false;
        }
        Optional<Problem> problemOptional = problemDao.findById(problemgetOrder.getProblemId());
        if(!problemOptional.isPresent()){
            return false;
        }
        Problem problem = problemOptional.get();
        problem.setProblemKey(problemgetOrder.getKey());
        problem.setProblemDetails(problemgetOrder.getProblemDetails());
        problem.setProblemCreator(problemgetOrder.getProblemCreator());
        String content = problemgetOrder.getTitle()+"&&&"+problemgetOrder.getProblemContent();
        problem.setProblemContent(content);
        Problem save = problemDao.save(problem);
        if(redisUtil.exists("web-dynasty-problem-"+dynastyId+"-"+3)){
            redisUtil.remove("web-dynasty-problem-"+dynastyId+"-"+3);
        }
        return true;
    }
}
