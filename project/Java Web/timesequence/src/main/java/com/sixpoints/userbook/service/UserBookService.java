package com.sixpoints.userbook.service;

import com.sixpoints.book.dao.BookDao;
import com.sixpoints.constant.Constant;
import com.sixpoints.entity.book.Book;
import com.sixpoints.entity.book.BookListVO;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.book.CertificateUserBookListVO;
import com.sixpoints.entity.user.book.SpecificBookCompletedListVO;
import com.sixpoints.entity.user.book.UserBookListVO;
import com.sixpoints.entity.user.book.UserBookProcess;
import com.sixpoints.entity.user.card.UserCard;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userbook.dao.UserBookDao;
import com.sixpoints.usercard.dao.UserCardDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.DateFormatUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author ASUS
 * @createTime 2021/5/6 8:51
 * @projectName demo
 * @className UserBookService.java
 * @description 获取捐赠情况的服务层
 */
@Service
public class UserBookService {
    @Resource
    private UserBookDao userBookDao;

    @Resource
    private UserDao userDao;

    @Resource
    private BookDao bookDao;

    @Resource
    private UserCardDao userCardDao;


    @Resource
    private DateFormatUtil dateFormatUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    // 某公益图书的捐赠信息
    public List<SpecificBookCompletedListVO> getTop15(int bookId) {
        //查找前15的捐赠信息
        List<UserBookProcess> userBookList = userBookDao.findListByBookId(bookId);
        //创建集合
        List<SpecificBookCompletedListVO> list = new LinkedList<>();
        //封装用户
        for (int i = 0; i < userBookList.size(); i++) {
            UserBookProcess userBookProcess = userBookList.get(i);
            String donateTime = dateFormatUtil.formatDate(userBookProcess.getDonateTime());
            SpecificBookCompletedListVO listVO = new SpecificBookCompletedListVO(userBookProcess.getUser().getUserNickname(), donateTime, userBookProcess.getDonateObject());
            list.add(listVO);
        }
        return list;
    }

    // 查询用户已捐赠完成的记录
    public List<CertificateUserBookListVO> getUserBookListVO(int userId) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId)) {
            return new LinkedList<>();
        }
        //定义集合
        List<CertificateUserBookListVO> certificateUserBookListVOS = new LinkedList<>();
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<UserBookProcess> userBookProcesses = user.getUserBookProcesses();
            for (UserBookProcess userBookProcess : userBookProcesses) {
                if (userBookProcess.getProcess() == 100) {
                    String time = dateFormatUtil.formatDate(userBookProcess.getDonateTime());
                    CertificateUserBookListVO listVO = new CertificateUserBookListVO(time, userBookProcess.getDonateObject(), userBookProcess.getBook().getBookId());
                    certificateUserBookListVOS.add(listVO);
                }
            }
        }
        Collections.sort(certificateUserBookListVOS);
        //返回信息
        return certificateUserBookListVOS;
    }

    // 获取用户正在捐赠的信息
    public List<UserBookListVO> getOnProcessing(int userId) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId)) {
            return new LinkedList<>();
        }
        //定义集合
        List<UserBookListVO> userBookListVOS = new LinkedList<>();
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<UserBookProcess> userBookProcesses = user.getUserBookProcesses();
            for (UserBookProcess userBookProcess : userBookProcesses) {
                if (userBookProcess.getProcess() != 100) {
                    BookListVO bookListVO = new BookListVO(userBookProcess.getBook().getBookId(), userBookProcess.getBook().getBookName(), userBookProcess.getBook().getBookPic(), userBookProcess.getBook().getTotalNum(), userBookProcess.getBook().getGoalNum());
                    UserBookListVO listVO = new UserBookListVO(userBookProcess.getProcessId(), userBookProcess.getProcess(), bookListVO);
                    userBookListVOS.add(listVO);
                }
            }
        }
        Collections.sort(userBookListVOS);
        //返回信息
        return userBookListVOS;
    }

    // 用户进行捐卡
    @Transactional
    public boolean donateCard(int userId, int cardId, int cardNum, int processId) {
        //查询用户,并修改用户信息
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<UserBookProcess> userBookProcesses = user.getUserBookProcesses();
            Set<UserCard> userCards = user.getUserCards();
            for (UserBookProcess userBookProcess : userBookProcesses) {
                // 找到要进行捐卡的项目
                if (userBookProcess.getProcessId() == processId) {
                    // 判断是否到达100条件
                    if (userBookProcess.getProcess() + 10 * cardNum > 100) {
                        return false;
                    } else if (userBookProcess.getProcess() + 10 * cardNum == 100) {
                        // 设置process为100
                        userBookProcess.setProcess(100);
                        // 设置捐赠对象
                        userBookProcess.setDonateObject(Constant.DONATE_OBJECT);
                        // 设置捐赠时间
                        userBookProcess.setDonateTime(System.currentTimeMillis());
                        // 判断是否为第一次捐赠
                        if (user.getUserFirstDonateTime() == 0) {
                            user.setUserFirstDonateTime(System.currentTimeMillis());
                        }
                        // 对用户总捐赠书籍个数+1
                        int totalDonateBooks = user.getUserTotalDonateBooks() + 1;
                        user.setUserTotalDonateBooks(totalDonateBooks);
                        int totalNum = userBookProcess.getBook().getTotalNum() + 1;
                        userBookProcess.getBook().setTotalNum(totalNum);
                    } else {
                        // 未到达100，直接加进度
                        int process = userBookProcess.getProcess() + 10 * cardNum;
                        userBookProcess.setProcess(process);
                    }
                    break;
                }
            }
            // 修改卡片数据，卡片数量是否足够，卡片数目若为0，则删除卡片信息，若不为0，减少相应卡片数量
            for (UserCard userCard : userCards) {
                // 卡片现有数量
                int count = userCard.getCardCount();
                if (userCard.getCard().getCardId() == cardId) {
                    if (count < cardNum) {
                        // 捐的数量多于已有数量
                        return false;
                    } else if (count == cardNum) {
                        // 捐卡数量与卡片数量相同，移除相关记录
                        //存在查询这个题
                        Optional<UserCard> userCardOptional = userCardDao.findUserCard(userId, cardId);
                        UserCard userCard1 = userCardOptional.get();
                        //删除
                        userCardDao.delete(userCard1);
                        userCards.remove(userCard);
                        break;
                    } else if (count > cardNum) {
                        userCard.setCardCount(count - cardNum);
                        break;
                    }
                }
            }
            // 将数据保存到数据库
            user.setUserCards(userCards);
            user.setUserBookProcesses(userBookProcesses);
            userDao.save(user);
            return true;
        } else {
            return false;
        }
    }

    // 开启某一公益图书的公益项目
    public boolean addDonate(int userId, int bookId) {
        if (!auxiliaryBloomFilterUtil.bookIdIsExist(bookId)) {
            System.out.println("没有该图书");
            return false;
        }
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId)) {
            System.out.println("没有该用户");
            return false;
        }
        Optional<User> optionalUser = userDao.findById(userId);
        if (!optionalUser.isPresent()) {
            return false;
        }
        User user = optionalUser.get();
        Optional<Book> optionalBook = bookDao.findById(bookId);
        if (!optionalBook.isPresent()) {
            return false;
        }
        // 判断用户是否已开启捐赠
        for (UserBookProcess userBookProcess : user.getUserBookProcesses()) {
            if (userBookProcess.getBook().getBookId() == bookId && userBookProcess.getProcess() != 100) {
                return false;
            }
        }
        Book book = optionalBook.get();
        UserBookProcess userBookProcess = new UserBookProcess();
        userBookProcess.setUser(user);
        userBookProcess.setBook(book);
        userBookProcess.setProcess(0);
        userBookDao.save(userBookProcess);
        return true;
    }
}
