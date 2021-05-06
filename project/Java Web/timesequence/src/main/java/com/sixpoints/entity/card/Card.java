package com.sixpoints.entity.card;

import com.sixpoints.entity.dynasty.Dynasty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ca_card")
public class Card implements Serializable {
    private Integer cardId;//卡片标识符
    private String cardName;//卡片名称
    private Integer cardType;//卡片分类
    private String cardInfo;//卡片简介
    private String cardPicture;//卡片图片
    private String cardStory;//卡片故事
    private String cardCreator;//创建人
    private long cardCreationTime;//创建时间

    private Dynasty cardDynasty;//卡片所属朝代

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    @Column(length = 10)
    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @Column
    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    @Column(length = 2000)
    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    @Column(length = 100)
    public String getCardPicture() {
        return cardPicture;
    }

    public void setCardPicture(String cardPicture) {
        this.cardPicture = cardPicture;
    }

    @Column(length = 2000)
    public String getCardStory() {
        return cardStory;
    }

    public void setCardStory(String cardStory) {
        this.cardStory = cardStory;
    }

    @Column(length = 10)
    public String getCardCreator() {
        return cardCreator;
    }

    public void setCardCreator(String cardCreator) {
        this.cardCreator = cardCreator;
    }

    @Column
    public long getCardCreationTime() {
        return cardCreationTime;
    }

    public void setCardCreationTime(long cardCreationTime) {
        this.cardCreationTime = cardCreationTime;
    }

    @ManyToOne
    @JoinColumn(name = "dynasty_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Dynasty getCardDynasty() {
        return cardDynasty;
    }

    public void setCardDynasty(Dynasty cardDynasty) {
        this.cardDynasty = cardDynasty;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardName='" + cardName + '\'' +
                ", cardType=" + cardType +
                ", cardInfo='" + cardInfo + '\'' +
                ", cardPicture='" + cardPicture + '\'' +
                ", cardStory='" + cardStory + '\'' +
                ", cardCreator='" + cardCreator + '\'' +
                ", cardCreationTime=" + cardCreationTime +
                '}';
    }
}
