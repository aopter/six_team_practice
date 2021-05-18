package net.onest.timestoryprj.entity;

import java.io.Serializable;

/**
 * @author ASUS
 * @createTime 2021/5/5 9:55
 * @projectName demo
 * @className BookVO.java
 * @description 公益图书详情
 */
public class BookVO implements Serializable {
    private Integer bookId;//图书标识符
    private String bookName;//图书名称
    private String bookDes;//图书描述
    private String bookPic;//图书图片存放路径

    public BookVO() {
    }

    public BookVO(Integer bookId, String bookName, String bookDes, String bookPic) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookDes = bookDes;
        this.bookPic = bookPic;
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

    public String getBookDes() {
        return bookDes;
    }

    public void setBookDes(String bookDes) {
        this.bookDes = bookDes;
    }

    public String getBookPic() {
        return bookPic;
    }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
    }

    @Override
    public String toString() {
        return "BookVO{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookDes='" + bookDes + '\'' +
                ", bookPic='" + bookPic + '\'' +
                '}';
    }
}
