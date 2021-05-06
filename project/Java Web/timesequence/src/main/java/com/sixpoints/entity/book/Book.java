package com.sixpoints.entity.book;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 图书类
 */
@Entity
@Table(name = "li_book")
public class Book implements Serializable {
    private Integer bookId;//图书流水号
    private String bookName;//图书名称
    private String bookDes;//图书描述
    private String bookPic;//图书图片存放路径
    private int totalNum;//图书数量
    private int goalNum;//图书捐赠的数量
    private int flag;//图书是否过期（0为过期，1为正常使用）

    //无参构造
    public Book() {
    }

    //全参构造
    public Book(Integer bookId, String bookName, String bookDes, String bookPic, int totalNum, int goalNum, int flag) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookDes = bookDes;
        this.bookPic = bookPic;
        this.totalNum = totalNum;
        this.goalNum = goalNum;
        this.flag = flag;
    }

    //getter和setter放方法

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    @Column(length = 50)
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Column(length = 255)
    public String getBookDes() {
        return bookDes;
    }

    public void setBookDes(String bookDes) {
        this.bookDes = bookDes;
    }

    @Column(length = 255)
    public String getBookPic() {
        return bookPic;
    }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
    }

    @Column
    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        totalNum = totalNum;
    }

    @Column
    public int getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(int goalNum) {
        this.goalNum = goalNum;
    }

    @Column
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
