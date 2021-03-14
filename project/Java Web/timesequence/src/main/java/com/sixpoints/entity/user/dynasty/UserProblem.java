package com.sixpoints.entity.user.dynasty;

import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Problem;

import javax.persistence.*;

@Entity
@Table(name = "us_user_problem")
public class UserProblem {
    private Integer id;//流水号

    private Dynasty dynasty;//朝代
    private Problem problem;//题目

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "dynasty_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Dynasty getDynasty() {
        return dynasty;
    }

    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }

    @ManyToOne
    @JoinColumn(name = "problem_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
