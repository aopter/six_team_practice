package net.onest.timestoryprj.entity;

import java.io.Serializable;

public class Problem implements Serializable {
    private int problemId;//题目标识符
    private int problemType;//题目类型
    private String problemContent;//题目描述
    private String problemKey;//题目答案
    private String problemDetails;//题目解析
    private String problemCreator;//创建人
    private long problemCreationTime;//创建时间


    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getProblemType() {
        return problemType;
    }

    public void setProblemType(int problemType) {
        this.problemType = problemType;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public String getProblemKey() {
        return problemKey;
    }

    public void setProblemKey(String problemKey) {
        this.problemKey = problemKey;
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
        return "Problem{" +
                "problemId=" + problemId +
                ", problemType=" + problemType +
                ", problemContent='" + problemContent + '\'' +
                ", problemKey='" + problemKey + '\'' +
                ", problemDetails='" + problemDetails + '\'' +
                ", problemCreator='" + problemCreator + '\'' +
                ", problemCreationTime=" + problemCreationTime +
                '}';
    }
}
