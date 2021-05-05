package com.sixpoints.entity.rule;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ru_rule")
public class Rule implements Serializable {
    private Integer ruleId;//规则标识符
    private String ruleInfo;//规则详情

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    @Column(length = 1000)
    public String getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(String ruleInfo) {
        this.ruleInfo = ruleInfo;
    }
}
