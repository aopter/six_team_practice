package com.sixpoints.entity.dynasty;

import java.io.Serializable;

public class DynastyListVO implements Serializable {
    private Integer dynastyId;//朝代标识符
    private String dynastyName;//朝代名称

    public DynastyListVO() {}

    public DynastyListVO(Integer dynastyId, String dynastyName) {
        this.dynastyId = dynastyId;
        this.dynastyName = dynastyName;
    }

    public Integer getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(Integer dynastyId) {
        this.dynastyId = dynastyId;
    }

    public String getDynastyName() {
        return dynastyName;
    }

    public void setDynastyName(String dynastyName) {
        this.dynastyName = dynastyName;
    }
}
