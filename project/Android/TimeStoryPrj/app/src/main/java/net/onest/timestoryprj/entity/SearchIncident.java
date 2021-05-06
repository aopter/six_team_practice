package net.onest.timestoryprj.entity;

public class SearchIncident {
    private Integer incidentId;//朝代事件标识符
    private String incidentName;//事件名称
    private String incidentInfo;//事件简介
    private Integer dynastyId;  // 所属朝代id
    private String dynastyName; // 所属朝代名称
    private boolean flag; // 是否已解锁

    public SearchIncident() {
    }

    public SearchIncident(Integer incidentId, String incidentName, String incidentInfo, Integer dynastyId, String dynastyName) {
        this.incidentId = incidentId;
        this.incidentName = incidentName;
        this.incidentInfo = incidentInfo;
        this.dynastyId = dynastyId;
        this.dynastyName = dynastyName;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "SearchIncident{" +
                "incidentId='" + incidentId + '\'' +
                ", incidentName='" + incidentName + '\'' +
                ", incidentInfo='" + incidentInfo + '\'' +
                ", dynastyId='" + dynastyId + '\'' +
                ", dynastyName='" + dynastyName + '\'' +
                ", flag=" + flag +
                '}';
    }
}
