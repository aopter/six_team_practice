package com.sixpoints.entity.dynasty;

import java.io.Serializable;

public class IncidentVO implements Serializable {
    private Integer incidentId;//朝代事件标识符
    private String incidentName;//事件名称
    private String incidentInfo;//事件简介
    private String incidentPicture;//事件图片
    private String incidentDialog;//事件对话内容

    public IncidentVO() {}

    public IncidentVO(Integer incidentId, String incidentName, String incidentInfo, String incidentPicture, String incidentDialog) {
        this.incidentId = incidentId;
        this.incidentName = incidentName;
        this.incidentInfo = incidentInfo;
        this.incidentPicture = incidentPicture;
        this.incidentDialog = incidentDialog;
    }

    public Integer getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }

    public String getIncidentName() {
        return incidentName;
    }

    public void setIncidentName(String incidentName) {
        this.incidentName = incidentName;
    }

    public String getIncidentInfo() {
        return incidentInfo;
    }

    public void setIncidentInfo(String incidentInfo) {
        this.incidentInfo = incidentInfo;
    }

    public String getIncidentPicture() {
        return incidentPicture;
    }

    public void setIncidentPicture(String incidentPicture) {
        this.incidentPicture = incidentPicture;
    }

    public String getIncidentDialog() {
        return incidentDialog;
    }

    public void setIncidentDialog(String incidentDialog) {
        this.incidentDialog = incidentDialog;
    }
}
