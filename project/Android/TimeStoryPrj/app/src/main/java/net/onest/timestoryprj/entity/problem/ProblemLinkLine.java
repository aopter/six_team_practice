package net.onest.timestoryprj.entity.problem;

import net.onest.timestoryprj.entity.Problem;

import java.io.Serializable;

public class ProblemLinkLine extends Problem implements Serializable {
    //     朝代id
    private String dynastyId;//朝代标识符
    //     题目id
    private int problemId;//题目标识符
    private String problemDetails;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getProblemDetails() {
        return problemDetails;
    }

    @Override
    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }
    //     选项A
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    //     选项B
//     选项C
//     选项D
    private String optionAdes;
    private String optionBdes;
    private String optionCdes;
    private String optionDdes;
    //     答案A
    private String answer;

    private String details;


    public String getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(String dynastyId) {
        this.dynastyId = dynastyId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
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
}
