package com.sixpoints.entity.dynasty;

import java.io.Serializable;

public class IncidentListVO implements Serializable {
    private Integer incidentId;//朝代事件标识符
    private String incidentName;//事件名称
    private String incidentPicture;//事件图片(只有事件的图片)

    public IncidentListVO() {}

    public IncidentListVO(Integer incidentId, String incidentName, String incidentPicture) {
        this.incidentId = incidentId;
        this.incidentName = incidentName;
        this.incidentPicture = incidentPicture;
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

    public String getIncidentPicture() {
        return incidentPicture;
    }

    public void setIncidentPicture(String incidentPicture) {
        this.incidentPicture = incidentPicture;
    }
}
