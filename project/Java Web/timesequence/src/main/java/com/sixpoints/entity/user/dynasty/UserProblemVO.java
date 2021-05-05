package com.sixpoints.entity.user.dynasty;

import com.sixpoints.entity.dynasty.DynastyListVO;
import com.sixpoints.entity.dynasty.ProblemVO;

import java.io.Serializable;

public class UserProblemVO implements Serializable {
    private DynastyListVO dynasty;
    private ProblemVO problem;

    public UserProblemVO() {}

    public UserProblemVO(DynastyListVO dynasty, ProblemVO problem) {
        this.dynasty = dynasty;
        this.problem = problem;
    }

    public DynastyListVO getDynasty() {
        return dynasty;
    }

    public void setDynasty(DynastyListVO dynasty) {
        this.dynasty = dynasty;
    }

    public ProblemVO getProblem() {
        return problem;
    }

    public void setProblem(ProblemVO problem) {
        this.problem = problem;
    }
}
