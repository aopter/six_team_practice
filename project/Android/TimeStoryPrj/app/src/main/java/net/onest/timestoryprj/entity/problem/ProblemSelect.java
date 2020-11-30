package net.onest.timestoryprj.entity.problem;

import net.onest.timestoryprj.entity.Problem;

import java.io.Serializable;

public class ProblemSelect extends Problem implements Serializable {
//     朝代id
    private String dynastyId;//朝代标识符
//     题目id
    private int problemId;//题目标识符
    private String title;//标题
    private String problemDetails;

    @Override
    public String getProblemDetails() {
        return problemDetails;
    }

    @Override
    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //     选项A
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
//     选项B
//     选项C
//     选项D
//     选项A图
//     选项B图
//     选项C图
//     选项D图
    private String optionApic;
    private String optionBpic;
    private String optionCpic;
    private String optionDpic;
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
}
