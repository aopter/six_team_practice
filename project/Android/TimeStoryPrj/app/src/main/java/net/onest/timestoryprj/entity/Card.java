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
}
