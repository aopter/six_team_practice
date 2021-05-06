package com.sixpoints.entity.dynasty;

import java.io.Serializable;

public class ProblemVO implements Serializable {
    private Integer problemId;//题目标识符
    private Integer problemType;//题目类型
    private String problemContent;//题目描述
    private String problemKey;//题目答案
    private String problemDetails;//题目解析

    public ProblemVO() {}

    public ProblemVO(Integer problemId, Integer problemType, String problemContent, String problemKey, String problemDetails) {
        this.problemId = problemId;
        this.problemType = problemType;
        this.problemContent = problemContent;
        this.problemKey = problemKey;
        this.problemDetails = problemDetails;
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
}
