package net.onest.timestoryprj.entity;

import java.util.Set;

public class Dynasty {
    private Integer dynastyId;//朝代标识符
    private String dynastyName;//朝代名称
    private String dynastyTime;//朝代时间
    private String dynastyInfo;//朝代简介
    private String dynastyCreator;//创建人
    private long dynastyCreationTime;//创建时间

    private Set<Incident> incidents;//朝代中的事件
    private Set<Problem> problems;//朝代的题目
}
