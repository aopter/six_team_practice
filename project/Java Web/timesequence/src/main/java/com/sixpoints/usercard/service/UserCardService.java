package com.sixpoints.usercard.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.card.CardListVO;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.card.UserCard;
import com.sixpoints.entity.user.card.UserCardVO;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.usercard.dao.UserCardDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserCardService {

    @Resource
    private UserDao userDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    /**
     * 用户查询已有卡片
     * 1.传入用户id，朝代id
     * 2.根据用户和朝代id，查询卡片
     * 3.封装卡片
     * 4.返回集合
     */
    public List<UserCardVO> queryExistingCardByIdAndDynasty(int userId, int dynastyId) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId) || !auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)) {
            return new LinkedList<>();
        }
        //定义集合
        List<UserCardVO> userCardVOS = new LinkedList<>();
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<UserCard> userCardSet = user.getUserCards();
            //查询朝代信息
            Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
            if (dynastyOptional.isPresent()) {
                Dynasty dynasty = dynastyOptional.get();
                for (UserCard userCard : userCardSet) {
                    if (userCard.getCard().getCardDynasty().equals(dynasty)) {
                        UserCardVO userCardVO = new UserCardVO();
                        userCardVO.setUserCardId(userCard.getUserCardId());
                        userCardVO.setCardCount(userCard.getCardCount());

                        Card card = userCard.getCard();
                        CardListVO cardListVO = new CardListVO();

                        cardListVO.setCardId(card.getCardId());
                        cardListVO.setCardName(card.getCardName());
                        cardListVO.setCardType(card.getCardType());
                        cardListVO.setCardPicture(card.getCardPicture());

                        userCardVO.setCardListVO(cardListVO);

                        userCardVOS.add(userCardVO);
                    }
                }
            }
        }
        //返回信息
        return userCardVOS;
    }

    public List<UserCardVO> queryExistingCardByIdAndDynasty(int userId, int dynastyId, int cardType) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId) || !auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)) {
            return new LinkedList<>();
        }
        //定义集合
        List<UserCardVO> userCardVOS = new LinkedList<>();
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<UserCard> userCardSet = user.getUserCards();
            //查询朝代信息
            Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
            if (dynastyOptional.isPresent()) {
                Dynasty dynasty = dynastyOptional.get();
                for (UserCard userCard : userCardSet) {
                    if ((userCard).getCard().getCardDynasty().equals(dynasty) && userCard.getCard().getCardType() == cardType) {
                        UserCardVO userCardVO = new UserCardVO();
                        userCardVO.setUserCardId(userCard.getUserCardId());
                        userCardVO.setCardCount(userCard.getCardCount());

                        Card card = userCard.getCard();
                        CardListVO cardListVO = new CardListVO();

                        cardListVO.setCardId(card.getCardId());
                        cardListVO.setCardName(card.getCardName());
                        cardListVO.setCardType(card.getCardType());
                        cardListVO.setCardPicture(card.getCardPicture());

                        userCardVO.setCardListVO(cardListVO);

                        userCardVOS.add(userCardVO);
                    }
                }
            }
        }
        //返回信息
        return userCardVOS;
    }

    /**
     * 用户根据关键字模糊查询
     * 1、传入用户id，传入朝代id，传入关键字
     * 2.调用queryExistingCardByIdAndDynasty方法获取所有符合条件的卡片
     * 3.利用关键字进行匹配
     * 4.返回信息
     */
    public List<UserCardVO> fuzzySearch(int userId, int dynastyId, String key) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId) || !auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)) {
            return new LinkedList<>();
        }
        List<UserCardVO> tempList = queryExistingCardByIdAndDynasty(userId, dynastyId);
        List<UserCardVO> re = new LinkedList<>();
        for (int i = 0; i < tempList.size(); i++) {
            UserCardVO userCardVO = tempList.get(i);

            if (userCardVO.getCardListVO().getCardName().contains(key)) {
                re.add(userCardVO);
            }
        }
        return re;
    }

    public List<UserCardVO> fuzzySearch(int userId, int dynastyId, int cardType, String key) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId) || !auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)) {
            return new LinkedList<>();
        }
        List<UserCardVO> tempList = queryExistingCardByIdAndDynasty(userId, dynastyId, cardType);
        List<UserCardVO> re = new LinkedList<>();
        for (int i = 0; i < tempList.size(); i++) {
            UserCardVO userCardVO = tempList.get(i);

            if (userCardVO.getCardListVO().getCardName().contains(key)) {
                re.add(userCardVO);
            }
        }
        return re;
    }

    /**
     * 用户查询全部的已有卡片
     * 1.传入用户id
     * 2.根据用户id，查询卡片
     * 3.封装卡片
     * 4.返回集合
     */
    public List<UserCardVO> queryExistingCardByUserId(int userId) {
        if (!auxiliaryBloomFilterUtil.userIdIsExist(userId)) {
            return new LinkedList<>();
        }
        //定义集合
        List<UserCardVO> userCardVOS = new LinkedList<>();
        //查询用户
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 获得用户卡片信息
            Set<UserCard> userCardSet = user.getUserCards();
            System.out.println(userCardSet.size());
            for (UserCard userCard : userCardSet) {
                UserCardVO userCardVO = new UserCardVO();
                userCardVO.setUserCardId(userCard.getUserCardId());
                userCardVO.setCardCount(userCard.getCardCount());
                Card card = userCard.getCard();
                CardListVO cardListVO = new CardListVO();
                cardListVO.setCardId(card.getCardId());
                cardListVO.setCardName(card.getCardName());
                cardListVO.setCardType(card.getCardType());
                cardListVO.setCardPicture(card.getCardPicture());
                userCardVO.setCardListVO(cardListVO);
                userCardVOS.add(userCardVO);
                System.out.println(userCardVO);
            }
        }
        //返回信息
        return userCardVOS;
    }
}
