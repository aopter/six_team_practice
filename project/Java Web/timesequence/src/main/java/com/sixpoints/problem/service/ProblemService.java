package com.sixpoints.problem.service;

import com.sixpoints.constant.Constant;
import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Problem;
import com.sixpoints.entity.dynasty.ProblemVO;
import com.sixpoints.entity.status.UserStatus;
import com.sixpoints.entity.user.User;
import com.sixpoints.problem.dao.ProblemDao;
import com.sixpoints.status.dao.UserStatusDao;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.user.service.UserService;
import com.sixpoints.userproblem.service.UserProblemService;
import com.sixpoints.userunlockdynasty.service.UserUnlockDynastyService;
import com.sixpoints.userunlockdynastyincident.service.UserUnlockDynastyIncidentService;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.MathUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProblemService {

    @Resource
    private ProblemDao problemDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserProblemService userProblemService;

    @Resource
    private UserUnlockDynastyService userUnlockDynastyService;

    @Resource
    private UserService userService;

    @Resource
    private UserStatusDao userStatusDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //查询朝代的题目总数
    public int getProblemCount(int id){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(id)){
            return 0;
        }
        //查询朝代
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(id);
        //创建集合
        Set<Problem> problems = null;
        if(dynastyOptional.isPresent()){
            //根据朝代获取set集合
            problems = dynastyOptional.get().getProblems();
            return problems.size();
        }
        //返回集合数量
        return  0;
    }

    //用户进入答题模式（预准备三个题目）
    /**
     * 1.根据种类分组，根据id排序，生成随机数，取出指定的题目（limit k-1，1）
     * 2.随机取三次，每次种类不同
     * 3.封装集合
     * 4.返回集合
     * */
    public List<ProblemVO> readyToAnswer(int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        //创建集合
        List<ProblemVO> problemVOS = new LinkedList<>();
        //获取三种题目
        for(int i=1;i<4;i++){
            problemVOS.add(getRandomProblem(i,dynastyId));
        }
        return problemVOS;
    }

    //随机获取指定朝代和种类的题目
    public ProblemVO getRandomProblem(int problemType,int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new ProblemVO();
        }
        //1.获取数量
        int count = problemDao.getCount(problemType, dynastyId);
        //2.生成随机数
        int num = MathUtil.random(0,count);
        //3.获取题目
        Optional<Problem> problemOptional = problemDao.getProblem(problemType, dynastyId, num);
        //4.封装题目
        if(problemOptional.isPresent()){
            Problem problem = problemOptional.get();
            return new ProblemVO(problem.getProblemId(),problem.getProblemType(),problem.getProblemContent(),problem.getProblemKey(),problem.getProblemDetails());
        }
        //5.返回封装后的题目
        return new ProblemVO();
    }

    //用户答题
    @Transactional
    public String userAnswer(int userId,int dynastyId,int problemId,int result){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemId)){
            return "{'userId':"+ userId +",'userCount':0,'userExperience':0,'result':false,'unlock':false}";
        }
        //查询用户对象
        Optional<User> userOptional = userDao.findById(userId);
        if(userOptional.isPresent()){//用户存在
            User user = userOptional.get();
            //用户加经验
            user.setUserExperience(user.getUserExperience()+ Constant.PROBLEM_EXPERIENCE);
            //判断用户是否可以升级
            if(userService.isUp(user)){//可以升级升级
                Optional<UserStatus> userStatusOptional = userStatusDao.findById(user.getUserStatus().getStatusId()+1);
                if(userStatusOptional.isPresent()){
                    user.setUserStatus(userStatusOptional.get());
                }
            }
            //判断答题结果（如果正确加积分，错误不加积分）
            if(result == 1 && userUnlockDynastyService.isAddCount(userId,dynastyId)==false){//答案正确同时答题数目不足解锁
                //加答对题目个数
                userUnlockDynastyService.addOneProgress(userId,dynastyId);
                //加积分
                user.setUserCount(user.getUserCount()+Constant.PROBLEM_COUNT);
            }else{//答案错误
                //答案错误将题目收藏至收藏夹
                userProblemService.collect(userId, dynastyId, problemId);
            }
            //保存用户信息
            userDao.save(user);
            //返回信息{'userId':1,'userCount':111,'userExperience':1111,'result':true/false}格式
            return "{'userId':"+ userId +",'userCount':"+ user.getUserCount() +"," +
                    "'userExperience':"+ user.getUserExperience() +",'result':true,'unlock':"+ userUnlockDynastyService.isAddCount(userId,dynastyId) +"}";
        }
        return "{'userId':"+ userId +",'userCount':0,'userExperience':0,'result':false,'unlock':false}";
    }
}
