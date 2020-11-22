package net.onest.timestoryprj.entity;

public class Card {
    private Integer cardId;//卡片标识符
    private String cardName;//卡片名称
    private Integer cardType;//卡片分类
    private String cardInfo;//卡片简介
    private String cardPicture;//卡片图片
    private String cardStore;//卡片故事
    private String cardCreator;//创建人
    private long cardCreationTime;//创建时间

    private Dynasty cardDynasty;//卡片所属朝代

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

    public String getCardStore() {
        return cardStore;
    }

    public void setCardStore(String cardStore) {
        this.cardStore = cardStore;
    }

    public String getCardCreator() {
        return cardCreator;
    }

    public void setCardCreator(String cardCreator) {
        this.cardCreator = cardCreator;
    }

    public long getCardCreationTime() {
        return cardCreationTime;
    }

    public void setCardCreationTime(long cardCreationTime) {
        this.cardCreationTime = cardCreationTime;
    }

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
                ", cardStore='" + cardStore + '\'' +
                ", cardCreator='" + cardCreator + '\'' +
                ", cardCreationTime=" + cardCreationTime +
                ", cardDynasty=" + cardDynasty +
                '}';
    }
}
