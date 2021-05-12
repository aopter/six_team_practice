package net.onest.timestoryprj.entity;

/**
 * @author ASUS
 * @createTime 2021/5/5 9:58
 * @projectName demo
 * @className UserBookListVO.java
 * @description 证书中用户公益列表实体类
 */
public class CertificateUserBookListVO {
    private String donateTime;//捐赠时间,当process达到100时记录
    private String donateObject;//捐赠对象
    private Integer bookId;//相对的图书标识符

    public CertificateUserBookListVO() {
    }

    public CertificateUserBookListVO(String donateTime, String donateObject, Integer bookId) {
        this.donateTime = donateTime;
        this.donateObject = donateObject;
        this.bookId = bookId;
    }

    public String getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(String donateTime) {
        this.donateTime = donateTime;
    }

    public String getDonateObject() {
        return donateObject;
    }

    public void setDonateObject(String donateObject) {
        this.donateObject = donateObject;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "CertificateUserBookListVO{" +
                "donateTime=" + donateTime +
                ", donateObject='" + donateObject + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}
