package com.sixpoints.entity.dynasty;


import java.io.Serializable;
import java.util.List;

public class ProblemgetOrder implements Serializable {

    private Integer dynastyId;//朝代标识符
    private Integer problemId;//题目标识符
    private Integer problemType;//题目种类
    private String problemContent;//题目内容

    private String title;

    private List<OrderBean> contents;//内容

    private String key;//答案

    private String problemDetails;//解析

    private String problemCreator;//创建人
    private long problemCreationTime;//创建时间

    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OrderBean> getContents() {
        return contents;
    }

    public void setContents(List<OrderBean> contents) {
        this.contents = contents;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProblemDetails() {
        return problemDetails;
    }

    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

    public String getProblemCreator() {
        return problemCreator;
    }

    public void setProblemCreator(String problemCreator) {
        this.problemCreator = problemCreator;
    }

    public long getProblemCreationTime() {
        return problemCreationTime;
    }

    public void setProblemCreationTime(long problemCreationTime) {
        this.problemCreationTime = problemCreationTime;
    }

    @Override
    public String toString() {
        return "ProblemgetOrder{" +
                "dynastyId='" + dynastyId + '\'' +
                ", problemId=" + problemId +
                ", problemType=" + problemType +
                ", problemContent='" + problemContent + '\'' +
                ", title='" + title + '\'' +
                ", contents=" + contents +
                ", key='" + key + '\'' +
                ", problemDetails='" + problemDetails + '\'' +
                ", problemCreator='" + problemCreator + '\'' +
                ", problemCreationTime=" + problemCreationTime +
                '}';
    }
}
