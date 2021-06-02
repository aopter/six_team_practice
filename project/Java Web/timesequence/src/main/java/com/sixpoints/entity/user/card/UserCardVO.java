package com.sixpoints.entity.user.card;

import com.sixpoints.entity.card.CardListVO;

import java.io.Serializable;

public class UserCardVO implements Serializable, Comparable<UserCardVO> {
    private Integer userCardId;//用户卡片标识符
    private Integer cardCount;//卡片数量
    private CardListVO cardListVO;//卡片列表VO

    public UserCardVO() {
    }

    public UserCardVO(Integer userCardId, Integer cardCount, CardListVO cardListVO) {
        this.userCardId = userCardId;
        this.cardCount = cardCount;
        this.cardListVO = cardListVO;
    }

    public Integer getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(Integer userCardId) {
        this.userCardId = userCardId;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public CardListVO getCardListVO() {
        return cardListVO;
    }

    public void setCardListVO(CardListVO cardListVO) {
        this.cardListVO = cardListVO;
    }

    @Override
    public int compareTo(UserCardVO o) {
        return this.userCardId - o.getUserCardId();
    }
}
