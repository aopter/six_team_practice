package com.sixpoints.entity.user.book;

import com.sixpoints.entity.book.Book;
import com.sixpoints.entity.user.User;

import javax.persistence.*;

/**
 * 用户捐赠图书的进展情况
 */
@Entity
@Table(name = "us_user_book")
public class UserBookProcess {
    private Integer processId;//流水号id，方便之后的操作

    private Book book;//相对的图书
    private User user; // 相对的用户
    private int process;//进展情况
    private long donateTime;//捐赠时间,当process达到100时记录
    private String donateObject;//捐赠对象

    //无参构造
    public UserBookProcess() {
    }

    //全参构造
    public UserBookProcess(Integer processId, Book book, int process, long donateTime, String donateObject) {
        this.processId = processId;
        this.book = book;
        this.process = process;
        this.donateTime = donateTime;
        this.donateObject = donateObject;
    }

    //getter和setter方法
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }


    @ManyToOne
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


    @Column
    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    @Column
    public long getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(long donateTime) {
        this.donateTime = donateTime;
    }

    @Column
    public String getDonateObject() {
        return donateObject;
    }

    public void setDonateObject(String donateObject) {
        this.donateObject = donateObject;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
