package com.sixpoints.entity.user.card;

import com.sixpoints.entity.card.Card;

import javax.persistence.*;

@Entity
@Table(name = "us_user_card")
public class UserCard {
    private Integer userCardId;//用户卡片标识符
    private Integer cardCount;//卡片数量

    private Card card;//卡片

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(Integer userCardId) {
        this.userCardId = userCardId;
    }

    @Column
    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    @ManyToOne
    @JoinColumn(name = "card_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
