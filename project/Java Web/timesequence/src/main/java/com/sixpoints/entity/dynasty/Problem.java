package com.sixpoints.entity.dynasty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dy_problem")
public class Problem implements Serializable {
    private Integer problemId;//题目标识符
    private Integer problemType;//题目类型
    private String problemContent;//题目描述
    private String problemKey;//题目答案
    private String problemDetails;//题目解析
    private String problemCreator;//创建人
    private long problemCreationTime;//创建时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    @Column
    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    @Column(length = 1000)
    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    @Column(length = 1000)
    public String getProblemKey() {
        return problemKey;
    }

    public void setProblemKey(String problemKey) {
        this.problemKey = problemKey;
    }

    @Column(length = 1000)
    public String getProblemDetails() {
        return problemDetails;
    }

    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }

    @Column(length = 10)
    public String getProblemCreator() {
        return problemCreator;
    }

    public void setProblemCreator(String problemCreator) {
        this.problemCreator = problemCreator;
    }

    @Column
    public long getProblemCreationTime() {
        return problemCreationTime;
    }

    public void setProblemCreationTime(long problemCreationTime) {
        this.problemCreationTime = problemCreationTime;
    }
}
