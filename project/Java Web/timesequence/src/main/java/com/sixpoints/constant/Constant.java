package com.sixpoints.constant;

public final class Constant {
    public static final String USER_ID_BLOOM_FILTER = "userIdBloomFilter";//用户id布隆过滤器名称
    public static final String DYNASTY_ID_BLOOM_FILTER = "dynastyIdBloomFilter";//朝代id布隆过滤器名称
    public static final String INCIDENT_ID_BLOOM_FILTER = "incidentIdBloomFilter";//事件id布隆过滤器名称
    public static final String CARD_ID_BLOOM_FILTER = "cardIdBloomFilter";//卡片id布隆过滤器名称
    public static final String PROBLEM_ID_BLOOM_FILTER = "problemIdBloomFilter";//题目id布隆过滤器名称
    public static final String BOOK_ID_BLOOM_FILTER = "bookIdBloomFilter"; // 公益图书id布隆过滤器

    public static final int PROBLEM_COUNT = 10;//答对一个题加的积分数量
    public static final int PROBLEM_EXPERIENCE = 20;//用户答题得积分
    public static final int INCIDENT_EXPERIENCE = 15;//用户观看事件得积分
    public static final int DRAW_CARD_COUNT = 60;//用户抽卡一次消耗积分

    public static final int[] INIT_UNLOCK_DYNASTY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};//新注册用户解锁的朝代编号
    public static final int INIT_USER_COUNT = 100;//注册用户拥有的积分
    public static final int INIT_USER_EXPERIENCE = 1;//新注册用户拥有的经验点
    public static final String INIT_USER_SEX = "男";//新注册用户默认性别
    public static final String INIT_USER_NICKNAME = "昵称";//新注册用户默认昵称
    public static final int INIT_USER_STATUS = 1;//新注册用户地位

    public static final int SELECT_PROBLEM = 1;//选择题编号
    public static final int LINK_PROBLEM = 2;//连线题编号
    public static final int ORDER_PROBLEM = 3;//排序题编号

    public static final String DONATE_OBJECT = "希望小学";  // 捐赠图书对象
}
