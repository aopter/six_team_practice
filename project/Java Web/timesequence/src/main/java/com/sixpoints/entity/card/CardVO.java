package com.sixpoints.entity.card;

import com.sixpoints.entity.dynasty.Dynasty;

import java.io.Serializable;

public class CardVO implements Serializable {
    private Integer cardId;//卡片标识符
    private String cardName;//卡片名称
    private Integer cardType;//卡片分类
    private String cardInfo;//卡片简介
    private String cardPicture;//卡片图片
    private String cardStory;//卡片故事
    private Integer dynastyId;//卡片所属朝代

    public CardVO() {}

    public CardVO(Integer cardId, String cardName, Integer cardType, String cardInfo, String cardPicture, String cardStory, Integer dynastyId) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardInfo = cardInfo;
        this.cardPicture = cardPicture;
        this.cardStory = cardStory;
        this.dynastyId = dynastyId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardPicture() {
        return cardPicture;
    }

    public void setCardPicture(String cardPicture) {
        this.cardPicture = cardPicture;
    }

    public String getCardStory() {
        return cardStory;
    }

    public void setCardStory(String cardStory) {
        this.cardStory = cardStory;
    }

    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }
}
