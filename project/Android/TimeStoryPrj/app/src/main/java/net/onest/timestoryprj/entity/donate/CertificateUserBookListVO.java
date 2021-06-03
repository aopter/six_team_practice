package net.onest.timestoryprj.entity.donate;

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
    private String bookName;// 图书名称

    public CertificateUserBookListVO() {
    }

    public CertificateUserBookListVO(String donateTime, String donateObject, Integer bookId, String bookName) {
        this.donateTime = donateTime;
        this.donateObject = donateObject;
        this.bookId = bookId;
        this.bookName = bookName;
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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "CertificateUserBookListVO{" +
                "donateTime='" + donateTime + '\'' +
                ", donateObject='" + donateObject + '\'' +
                ", bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}