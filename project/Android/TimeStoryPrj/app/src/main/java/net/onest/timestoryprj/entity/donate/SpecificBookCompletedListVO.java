package net.onest.timestoryprj.entity.donate;

/**
 * 已完成某公益项目的详情
 */
public class SpecificBookCompletedListVO {
    private String userName; // 捐赠爱心的用户编号
    private long donateTime;//捐赠时间,当process达到100时记录
    private String donateObject;//捐赠对象

    public SpecificBookCompletedListVO() {
    }

    public SpecificBookCompletedListVO(String userName, long donateTime, String donateObject) {
        this.userName = userName;
        this.donateTime = donateTime;
        this.donateObject = donateObject;
    }

    public String getUserId() {
        return userName;
    }

    public void setUserId(String userName) {
        this.userName = userName;
    }

    public long getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(long donateTime) {
        this.donateTime = donateTime;
    }

    public String getDonateObject() {
        return donateObject;
    }

    public void setDonateObject(String donateObject) {
        this.donateObject = donateObject;
    }

    @Override
    public String toString() {
        return "CompletedBookListVO{" +
                ", userName=" + userName +
                ", donateTime=" + donateTime +
                ", donateObject='" + donateObject + '\'' +
                '}';
    }
}
