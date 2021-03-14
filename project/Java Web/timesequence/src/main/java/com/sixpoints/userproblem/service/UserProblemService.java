package com.sixpoints.userproblem.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.DynastyListVO;
import com.sixpoints.entity.dynasty.Problem;
import com.sixpoints.entity.dynasty.ProblemVO;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.dynasty.UserProblem;
import com.sixpoints.entity.user.dynasty.UserProblemVO;
import com.sixpoints.problem.dao.ProblemDao;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userproblem.dao.UserProblemDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProblemService {
    @Resource
    private UserProblemDao userProblemDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private ProblemDao problemDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //收藏题目
    public boolean collect(int userId,int dynastyId,int problemId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemId)){
            return false;
        }
        if(isCollect(userId,dynastyId,problemId)){//已收藏
            return true;
        }
        boolean flag = false;
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        //查询朝代
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
        //查询问题
        Optional<Problem> problemOptional = problemDao.findById(problemId);
        //用户
        User user = null;
        if(userOptional.isPresent()&&dynastyOptional.isPresent()&&problemOptional.isPresent()){
            UserProblem userProblem = new UserProblem();
            userProblem.setDynasty(dynastyOptional.get());
            userProblem.setProblem(problemOptional.get());
            //保存新题
            userProblemDao.save(userProblem);

            user = userOptional.get();
            //保存关系
            user.getProblems().add(userProblem);
            userDao.save(user);
            flag = true;
        }
        return flag;
    }

    //判断某个题是否已经收藏
    public boolean isCollect(int userId,int dynastyId,int problemId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemId)){
            return false;
        }
        boolean flag = false;
        //查询是否存在
        Optional<UserProblem> userProblemOptional = userProblemDao.findUserProblem(userId, dynastyId, problemId);
        //判断
        if(userProblemOptional.isPresent()){
            flag = true;
        }
        return flag;
    }

    //删除指定的题目（取消收藏）
    public boolean cancelCollect(int userId,int dynastyId,int problemId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)||!auxiliaryBloomFilterUtil.problemIsExist(problemId)){
            return false;
        }
        //判断是否存在
        if(!isCollect(userId,dynastyId,problemId)){
            return false;
        }
        //存在查询这个题
        Optional<UserProblem> userProblemOptional = userProblemDao.findUserProblem(userId, dynastyId, problemId);
        UserProblem userProblem = userProblemOptional.get();
        //删除
        userProblemDao.delete(userProblem);
        return true;
    }

    //查看收藏的题目(有朝代)
    public List<UserProblemVO> searchCollect(int userId, int dynastyId,int pageNum,int pageSize){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        //判断页数
        int pageTotalNum = getPageTotalNum(userId,dynastyId,pageSize);
        if(pageNum > pageTotalNum){
            pageNum = pageTotalNum;
        }
        if(pageNum < 1){
            pageNum = 1;
        }
        //查询题目
        List<UserProblem> problems = userProblemDao.findUserProblems(userId,dynastyId,(pageNum-1)*pageSize,pageSize);
        //创建集合
        List<UserProblemVO> userProblemVOS = new LinkedList<>();
        //封装题目
        for(int i=0;i<problems.size();i++){
            UserProblem userProblem = problems.get(i);
            Problem tempProblem = userProblem.getProblem();
            Dynasty tempDynasty = userProblem.getDynasty();
            //新建DynastyListVO
            DynastyListVO dynastyListVO = new DynastyListVO(tempDynasty.getDynastyId(),tempDynasty.getDynastyName());
            //新建ProblemVO
            ProblemVO problemVO = new ProblemVO(tempProblem.getProblemId(),tempProblem.getProblemType(),tempProblem.getProblemContent(),tempProblem.getProblemKey(),tempProblem.getProblemDetails());
            //新建UserProblemVO
            UserProblemVO userProblemVO = new UserProblemVO(dynastyListVO,problemVO);
            //放入集合中userProblemVOS
            userProblemVOS.add(userProblemVO);
        }
        //返回数据
        return userProblemVOS;
    }

    //查看收藏的题目（无朝代）
    public List<UserProblemVO> searchCollect(int userId,int pageNum,int pageSize){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return new LinkedList<>();
        }
        //判断页数
        int pageTotalNum = getPageTotalNum(userId,pageSize);
        if(pageNum > pageTotalNum){
            pageNum = pageTotalNum;
        }
        if(pageNum < 1){
            pageNum = 1;
        }
        //查询题目
        List<UserProblem> problems = userProblemDao.findUserProblems(userId,(pageNum-1)*pageSize,pageSize);
        //创建集合
        List<UserProblemVO> userProblemVOS = new LinkedList<>();
        //封装题目
        for(int i=0;i<problems.size();i++){
            UserProblem userProblem = problems.get(i);
            Problem tempProblem = userProblem.getProblem();
            Dynasty tempDynasty = userProblem.getDynasty();
            //新建DynastyListVO
            DynastyListVO dynastyListVO = new DynastyListVO(tempDynasty.getDynastyId(),tempDynasty.getDynastyName());
            //新建ProblemVO
            ProblemVO problemVO = new ProblemVO(tempProblem.getProblemId(),tempProblem.getProblemType(),tempProblem.getProblemContent(),tempProblem.getProblemKey(),tempProblem.getProblemDetails());
            //新建UserProblemVO
            UserProblemVO userProblemVO = new UserProblemVO(dynastyListVO,problemVO);
            //放入集合中userProblemVOS
            userProblemVOS.add(userProblemVO);
        }
        //返回数据
        return userProblemVOS;
    }

    //获取收藏题目总数（有朝代）
    public int getPageTotalNum(int userId,int dynastyId,int pageSize){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)||!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return 0;
        }
        int elementNum = userProblemDao.userProblemsCount(userId,dynastyId);
        if(elementNum % pageSize != 0){
            return (elementNum/pageSize)+1;
        }
        return elementNum/pageSize;
    }

    //获取收藏题目总数（无朝代）
    public int getPageTotalNum(int userId,int pageSize){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return 0;
        }
        int elementNum = userProblemDao.userProblemsCount(userId);
        if(elementNum % pageSize != 0){
            return (elementNum/pageSize)+1;
        }
        return elementNum/pageSize;
    }

    //用户获取自己收藏的某种类型题目的最大可分页数
    public int getPageTotalNumByProblemType(int userId,int pageSize,int problemType){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return 0;
        }
        //获取用户有的题目
        int elementNum = userProblemDao.countUserProblemByUserIdAndProblemType(userId, problemType);
        //返回数量
        if(elementNum % pageSize != 0){
            return (elementNum/pageSize)+1;
        }
        return elementNum/pageSize;
    }

    //用户分页查询自己收藏的某个类型的题目
    public List<UserProblemVO> getUserProblemByType(int userId,int problemType,int pageNum,int pageSize){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return new LinkedList<>();
        }
        //判断页数
        int pageTotalNum = getPageTotalNumByProblemType(userId,pageSize,problemType);
        if(pageNum > pageTotalNum){
            pageNum = pageTotalNum;
        }
        if(pageNum < 1){
            pageNum = 1;
        }
        //查询题目
        Optional<List<UserProblem>> problemsOptional= userProblemDao.UserProblemListByUserIdAndProblemType(userId,problemType,(pageNum-1)*pageSize,pageSize);
        //创建集合
        List<UserProblemVO> userProblemVOS = new LinkedList<>();
        if(!problemsOptional.isPresent()){
            return userProblemVOS;
        }
        List<UserProblem> problems = problemsOptional.get();
        //封装题目
        for(int i=0;i<problems.size();i++){
            UserProblem userProblem = problems.get(i);
            Problem tempProblem = userProblem.getProblem();
            Dynasty tempDynasty = userProblem.getDynasty();
            //新建DynastyListVO
            DynastyListVO dynastyListVO = new DynastyListVO(tempDynasty.getDynastyId(),tempDynasty.getDynastyName());
            //新建ProblemVO
            ProblemVO problemVO = new ProblemVO(tempProblem.getProblemId(),tempProblem.getProblemType(),tempProblem.getProblemContent(),tempProblem.getProblemKey(),tempProblem.getProblemDetails());
            //新建UserProblemVO
            UserProblemVO userProblemVO = new UserProblemVO(dynastyListVO,problemVO);
            //放入集合中userProblemVOS
            userProblemVOS.add(userProblemVO);
        }
        //返回数据
        return userProblemVOS;
    }

}
