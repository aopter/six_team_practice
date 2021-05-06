package com.sixpoints.card.service;

import com.sixpoints.card.dao.CardDao;
import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.card.CardVO;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.picture.service.PictureService;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceToWeb {
    @Resource
    private CardDao cardDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private PictureService pictureService;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    public List<Card> getCardsByDynasty(int dynastyId){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return new LinkedList<>();
        }
        if(redisUtil.exists("web-dynasty-card-list-"+dynastyId)){
            return (List<Card>) redisUtil.get("web-dynasty-card-list-"+dynastyId);
        }
        //查询朝代
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
        if(dynastyOptional.isPresent()){
            //获取卡片信息并返回
            redisUtil.set("web-dynasty-card-list-"+dynastyId,cardDao.findCardsByCardDynasty(dynastyOptional.get()));
            return cardDao.findCards(dynastyOptional.get().getDynastyId());
        }
        return new LinkedList<>();
    }

    public boolean addCard(Card card, int dynastyId, MultipartFile file){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return false;
        }
        Optional<Dynasty> optionalDynasty = dynastyDao.findById(dynastyId);
        if(!optionalDynasty.isPresent()){
            return false;
        }
        Dynasty dynasty = optionalDynasty.get();
        card.setCardDynasty(dynasty);
        //设置属性
        card.setCardCreationTime(System.currentTimeMillis());
        //插入卡片
        Card save = cardDao.save(card);
        System.out.printf(save.toString()+"___________________________________");
        if(save != null){
            save.setCardPicture("/card/card-"+save.getCardId()+".png");
            cardDao.save(save);

            pictureService.pictureUpload(save.getCardPicture(),file);

            //清空相关缓存
            if(redisUtil.exists("web-dynasty-card-list-"+dynastyId)){
                redisUtil.remove("web-dynasty-card-list-"+dynastyId);
            }
            if(redisUtil.exists("card-"+save.getCardId())){
                redisUtil.remove("card"+save.getCardId());
            }

            auxiliaryBloomFilterUtil.cardIdAdd(save.getCardId());

            //返回信息
            return true;
        }
        //返回信息
        return false;
    }

    //查询卡片详情
    public Card getCardDetails(int cardId){
        if(!auxiliaryBloomFilterUtil.cardIdIsExist(cardId)){
            System.out.println("_______________null____________"+cardId+"______________");
            return new Card();
        }
        Optional<Card> cardOptional = cardDao.findById(cardId);
        if(cardOptional.isPresent()){
            Card card = cardOptional.get();
            card.setCardDynasty(null);
            return card;
        }
        return new Card();
    }
}
