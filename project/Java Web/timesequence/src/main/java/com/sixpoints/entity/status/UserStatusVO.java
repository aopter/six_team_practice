package com.sixpoints.entity.status;

import java.io.Serializable;

public class UserStatusVO implements Serializable {
    private Integer statusId;//地位标识符
    private String statusName;//地位名称
    private long statusExperienceLow;//最低经验点
    private long statusExperienceTop;//最高经验点
    private String statusInfo;//地位简介

    public UserStatusVO() {}

    public UserStatusVO(Integer statusId, String statusName, long statusExperienceLow, long statusExperienceTop, String statusInfo) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.statusExperienceLow = statusExperienceLow;
        this.statusExperienceTop = statusExperienceTop;
        this.statusInfo = statusInfo;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public long getStatusExperienceLow() {
        return statusExperienceLow;
    }

    public void setStatusExperienceLow(long statusExperienceLow) {
        this.statusExperienceLow = statusExperienceLow;
    }

    public long getStatusExperienceTop() {
        return statusExperienceTop;
    }

    public void setStatusExperienceTop(long statusExperienceTop) {
        this.statusExperienceTop = statusExperienceTop;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }
}
