package com.sixpoints.entity.dynasty;


import java.io.Serializable;

public class ProblemLinkLine implements Serializable {

    private Integer dynastyId;//朝代标识符
    private Integer problemId;//题目标识符
    private Integer problemType;//题目种类
    private String problemDetails;
    private String title;

    private String optionA;//选项A
    private String optionB;//选项B
    private String optionC;//选项C
    private String optionD;//选项D

    private String optionAdes;
    private String optionBdes;
    private String optionCdes;
    private String optionDdes;

    private String answer;//答案A

    private String details;//解析

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

    public void setProblemId(int problemId) {
        this.problemId = problemId;
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

    public String getProblemDetails() {
        return problemDetails;
    }

    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionAdes() {
        return optionAdes;
    }

    public void setOptionAdes(String optionAdes) {
        this.optionAdes = optionAdes;
    }

    public String getOptionBdes() {
        return optionBdes;
    }

    public void setOptionBdes(String optionBdes) {
        this.optionBdes = optionBdes;
    }

    public String getOptionCdes() {
        return optionCdes;
    }

    public void setOptionCdes(String optionCdes) {
        this.optionCdes = optionCdes;
    }

    public String getOptionDdes() {
        return optionDdes;
    }

    public void setOptionDdes(String optionDdes) {
        this.optionDdes = optionDdes;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        return "ProblemLinkLine{" +
                "dynastyId='" + dynastyId + '\'' +
                ", problemId=" + problemId +
                ", problemType=" + problemType +
                ", problemDetails='" + problemDetails + '\'' +
                ", title='" + title + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", optionAdes='" + optionAdes + '\'' +
                ", optionBdes='" + optionBdes + '\'' +
                ", optionCdes='" + optionCdes + '\'' +
                ", optionDdes='" + optionDdes + '\'' +
                ", answer='" + answer + '\'' +
                ", details='" + details + '\'' +
                ", problemCreator='" + problemCreator + '\'' +
                ", problemCreationTime=" + problemCreationTime +
                '}';
    }
}
