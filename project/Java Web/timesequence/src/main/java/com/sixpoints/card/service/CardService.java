package com.sixpoints.card.service;

import com.sixpoints.card.dao.CardDao;
import com.sixpoints.constant.Constant;
import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.card.CardVO;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.card.UserCard;
import com.sixpoints.entity.user.dynasty.UserUnlockDynasty;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.usercard.dao.UserCardDao;
import com.sixpoints.utils.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CardService {

    @Resource
    private CardDao cardDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserCardDao userCardDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    /**
     * 用户抽卡
     * 1.传入用户userId
     * 2.查询用户已经解锁的朝代
     * 3.根据朝代查询卡片
     * 4.随机选取一张卡片
     * 5.将卡片封装
     * 6.查看用户是否有这张卡片
     * 7.有的话数量+1，没有的话添加，数量为1
     * */
    public CardVO drawACard(int userId){

        if(!canDraw(userId) || !auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return new CardVO();
        }
        //查询这个用户
        User user = userDao.findById(userId).get();
        //查询用户已解锁朝代
        Set<UserUnlockDynasty> dynastySet = user.getUserUnlockDynasties();
        //根据朝代查询卡片
        List<Card> cards = new LinkedList<>();
        //判空
        if(dynastySet.size() <= 0){
            return new CardVO();
        }
        for(UserUnlockDynasty userUnlockDynasty : dynastySet){
            //查询朝代的图片
            List<Card> tempCards = cardDao.findCardsByCardDynasty(userUnlockDynasty.getDynasty());
            //将所有卡片放入cards中
            cards.addAll(tempCards);
        }
        //随机挑选一张卡片
        Card card = cards.get(MathUtil.random(0,cards.size()));
        //封装卡片
        CardVO cardVO = new CardVO(card.getCardId(),card.getCardName(),card.getCardType(),card.getCardInfo(),card.getCardPicture(),card.getCardStory(),card.getCardDynasty().getDynastyId());
        //查看用户是否已经有这张卡片
        Set<UserCard> cardSet = user.getUserCards();
        boolean flag = false;
        for(UserCard userCard : cardSet){
            if(userCard.getCard().getCardId().equals(card.getCardId())) {
                //有
                flag = true;
                userCard.setCardCount(userCard.getCardCount() + 1);
            }
        }
        //有的话数量+1，没有的话添加，数量为1
        if(flag == false){//没有
            UserCard userCard = new UserCard();
            userCard.setCardCount(1);
            userCard.setCard(card);

            //保存到数据库
            cardSet.add(userCard);
            userCardDao.save(userCard);
        }
        //减少用户的积分
        user.setUserCount(user.getUserCount()-Constant.DRAW_CARD_COUNT);
        //保存到数据库
        user.setUserCards(cardSet);
        userDao.save(user);
        //返回信息
        return cardVO;
    }

    //判断用户积分是否可以抽卡
    public boolean canDraw(int userId){
        if(!auxiliaryBloomFilterUtil.userIdIsExist(userId)){
            return false;
        }
        //查询这个用户
        Optional<User> userOptional = userDao.findById(userId);
        if(userOptional.isPresent()){
            if(userOptional.get().getUserCount()>=Constant.DRAW_CARD_COUNT){
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 根据卡片id，查询卡片详情
     * 封装信息
     * 返回信息
     * */
    public CardVO getOneCard(int cardId){
        if(!auxiliaryBloomFilterUtil.cardIdIsExist(cardId)){//不存在
            return new CardVO();
        }
        if(redisUtil.exists("card-count-"+cardId)){
            redisUtil.incr("card-count-"+cardId);
        }
        if(redisUtil.exists("card-"+cardId)){
            return (CardVO) redisUtil.get("card-"+cardId);
        }
        Optional<Card> cardOptional = cardDao.findById(cardId);

        CardVO cardVO = null;
        if(cardOptional.isPresent()){
            Card card = cardOptional.get();
            cardVO = new CardVO(card.getCardId(),card.getCardName(),card.getCardType(),card.getCardInfo(),card.getCardPicture(),card.getCardStory(),card.getCardDynasty().getDynastyId());

        }else{
            cardVO = new CardVO();
        }
        redisUtil.set("card-"+cardId,cardVO);
        return cardVO;
    }
}
