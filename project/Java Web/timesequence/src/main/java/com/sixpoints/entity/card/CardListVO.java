package com.sixpoints.entity.card;

import java.io.Serializable;

public class CardListVO implements Serializable {
    private Integer cardId;//卡片标识符
    private String cardName;//卡片名称
    private String cardPicture;//卡片图片
    private Integer cardType;//卡片分类

    public CardListVO() {}

    public CardListVO(Integer cardId, String cardName, String cardPicture, Integer cardType) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardPicture = cardPicture;
        this.cardType = cardType;
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

    public String getCardPicture() {
        return cardPicture;
    }

    public void setCardPicture(String cardPicture) {
        this.cardPicture = cardPicture;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
}
