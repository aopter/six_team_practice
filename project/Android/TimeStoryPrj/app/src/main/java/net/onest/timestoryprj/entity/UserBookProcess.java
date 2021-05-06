package net.onest.timestoryprj.entity;


import net.onest.timestoryprj.entity.donate.Book;

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

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
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
        return "UserBookProcess{" +
                "processId=" + processId +
                ", book=" + book +
                ", user=" + user +
                ", process=" + process +
                ", donateTime=" + donateTime +
                ", donateObject='" + donateObject + '\'' +
                '}';
    }
}
