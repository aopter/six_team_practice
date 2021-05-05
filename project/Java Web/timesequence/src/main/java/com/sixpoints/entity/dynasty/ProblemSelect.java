package com.sixpoints.entity.dynasty;


import java.io.Serializable;

public class ProblemSelect extends Problem implements Serializable {
    private Integer dynastyId;//朝代标识符
    private Integer problemId;//题目标识符
    private Integer problemType;//题目种类
    private String title;//标题

    private String optionA;//选项A
    private String optionB;//选项B
    private String optionC;//选项C
    private String optionD;//选项D

    private String optionApic;//选项A图
    private String optionBpic;//选项B图
    private String optionCpic;//选项C图
    private String optionDpic;//选项D图

    private String answer;//答案

    private String details;//解析

    private String problemCreator;//创建人
    private long problemCreationTime;//创建时间


    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

    @Override
    public Integer getProblemId() {
        return problemId;
    }

    @Override
    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    @Override
    public Integer getProblemType() {
        return problemType;
    }

    @Override
    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
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

    public String getOptionApic() {
        return optionApic;
    }

    public void setOptionApic(String optionApic) {
        this.optionApic = optionApic;
    }

    public String getOptionBpic() {
        return optionBpic;
    }

    public void setOptionBpic(String optionBpic) {
        this.optionBpic = optionBpic;
    }

    public String getOptionCpic() {
        return optionCpic;
    }

    public void setOptionCpic(String optionCpic) {
        this.optionCpic = optionCpic;
    }

    public String getOptionDpic() {
        return optionDpic;
    }

    public void setOptionDpic(String optionDpic) {
        this.optionDpic = optionDpic;
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

    @Override
    public String getProblemCreator() {
        return problemCreator;
    }

    @Override
    public void setProblemCreator(String problemCreator) {
        this.problemCreator = problemCreator;
    }

    @Override
    public long getProblemCreationTime() {
        return problemCreationTime;
    }

    @Override
    public void setProblemCreationTime(long problemCreationTime) {
        this.problemCreationTime = problemCreationTime;
    }

    @Override
    public String toString() {
        return "ProblemSelect{" +
                "dynastyId='" + dynastyId + '\'' +
                ", problemId=" + problemId +
                ", title='" + title + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", optionApic='" + optionApic + '\'' +
                ", optionBpic='" + optionBpic + '\'' +
                ", optionCpic='" + optionCpic + '\'' +
                ", optionDpic='" + optionDpic + '\'' +
                ", answer='" + answer + '\'' +
                ", details='" + details + '\'' +
                ", problemCreator='" + problemCreator + '\'' +
                ", problemCreationTime=" + problemCreationTime +
                '}';
    }
}
