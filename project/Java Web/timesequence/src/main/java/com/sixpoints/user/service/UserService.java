package com.sixpoints.user.service;

import com.sixpoints.constant.Constant;
import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.status.UserStatus;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.UserDetails;
import com.sixpoints.entity.user.UserListVO;
import com.sixpoints.entity.user.UserVO;
import com.sixpoints.entity.user.dynasty.UserUnlockDynasty;
import com.sixpoints.status.dao.UserStatusDao;
import com.sixpoints.status.service.UserStatusService;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userdetails.dao.UserDetailsDao;
import com.sixpoints.userunlockdynasty.dao.UserUnlockDynastyDao;
import com.sixpoints.userunlockdynasty.service.UserUnlockDynastyService;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private UserDetailsDao userDetailsDao;

    @Resource
    private UserStatusDao userStatusDao;

    @Resource
    private UserUnlockDynastyDao userUnlockDynastyDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    public List<UserListVO> getTop20() {
        //查找前二十用户
        List<User> userList = userDao.findList();
        //创建集合
        List<UserListVO> list = new LinkedList<>();
        //封装用户
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            UserDetails userDetails = userDetailsDao.findById(user.getUserId()).get();
            UserListVO userListVO = new UserListVO(user.getUserId(), user.getUserHeader(), user.getUserNickname(), user.getUserExperience(), userDetails.getUserSignature());
            list.add(userListVO);
        }
        return list;
    }

    //用户注册
    @Transactional
    public boolean register(User user) {
        if (exits(user.getUserAccount())) {//已存在账号
            return false;
        }
        user.setUserCount(Constant.INIT_USER_COUNT);//初始积分
        user.setUserExperience(Constant.INIT_USER_EXPERIENCE);//初始经验
        user.setUserStatus(userStatusDao.findById(Constant.INIT_USER_STATUS).get());//初始地位
        if (user.getUserNickname() == null || "".equals(user.getUserNickname())) {
            user.setUserNickname(Constant.INIT_USER_NICKNAME);//初始昵称
        }
        User save = userDao.save(user);
        System.out.printf(save.toString());
        if (save != null) {
            //为其创建解锁初始化关卡
            for (int i = 0; i < Constant.INIT_UNLOCK_DYNASTY.length; i++) {
                UserUnlockDynasty userUnlockDynasty = new UserUnlockDynasty();
                userUnlockDynasty.setProgress(0);
                userUnlockDynasty.setDynasty(dynastyDao.findById((Constant.INIT_UNLOCK_DYNASTY)[i]).get());
                UserUnlockDynasty saveUserUnlockDynasty = userUnlockDynastyDao.save(userUnlockDynasty);
                if (saveUserUnlockDynasty != null) {
                    save.getUserUnlockDynasties().add(saveUserUnlockDynasty);
                }
            }
            userDao.save(save);

            UserDetails userDetails = new UserDetails();
            userDetails.setUserId(save.getUserId());
            userDetails.setUserCreationTime(System.currentTimeMillis());
            userDetails.setUserSex(Constant.INIT_USER_SEX);//初始性别
            userDetails.setUserNickname(save.getUserNickname());
            userDetailsDao.save(userDetails);

            auxiliaryBloomFilterUtil.userIdAdd(save.getUserId());

            return true;
        }
        return false;
    }

    //判断某一用户是否已存在
    public boolean exits(String userAccount) {
        Optional<User> userOptional = userDao.existsUser(userAccount);
        if (userOptional.isPresent()) {
            return true;
        }
        return false;
    }

    //判断某一用户是否已存在
    public boolean exits(String userAccount, String password) {
        Optional<User> userOptional = userDao.existsUser(userAccount, password);
        if (userOptional.isPresent()) {
            return true;
        }
        return false;
    }

    //查询用户详情
    public UserVO getUser(String userAccount, String password) {
        Optional<User> userOptional = userDao.existsUser(userAccount, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserVO(user.getUserId(), user.getUserHeader(), user.getUserNickname(), user.getUserExperience(), user.getUserCount(), user.getUserStatus(), user.getUserFirstDonateTime(), user.getUserTotalDonateBooks());
        }
        return new UserVO();
    }

    public UserVO getUser(String userAccount) {
        Optional<User> userOptional = userDao.existsUser(userAccount);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserVO(user.getUserId(), user.getUserHeader(), user.getUserNickname(), user.getUserExperience(), user.getUserCount(), user.getUserStatus(), user.getUserFirstDonateTime(), user.getUserTotalDonateBooks());
        }
        return new UserVO();
    }

    //判断用户是否可以升级
    public boolean isUp(User user) {
        //判断
        if (user != null) {
            //获取user的经验
            long userExperience = user.getUserExperience();
            //获取user的地位
            UserStatus userStatus = user.getUserStatus();
            //进行比较
            if (userExperience > userStatus.getStatusExperienceTop()) {
                //可以升级的话就升级
                return true;
            }
            //否则不升级
            return false;
        }
        return false;
    }

    //获取现有用户的个数
    public int getAllUsrCount() {
        return userDao.getAllCount();
    }

}
