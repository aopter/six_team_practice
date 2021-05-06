package com.sixpoints.entity.book;

/**
 * @author ASUS
 * @createTime 2021/5/5 9:49
 * @projectName demo
 * @className BookListVO.java
 * @description 图书公益列表实体类
 */
public class BookListVO {
    private Integer bookId;//图书标识符
    private String bookName;//图书名称
    private String bookPic;//图书图片存放路径
    private int totalNum;//图书累计捐赠数量
    private int goalNum;//图书捐赠的数量

    public BookListVO() {
    }

    public BookListVO(Integer bookId, String bookName, String bookPic, int totalNum, int goalNum) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookPic = bookPic;
        this.totalNum = totalNum;
        this.goalNum = goalNum;
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

    public String getBookPic() {
        return bookPic;
    }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        totalNum = totalNum;
    }

    public int getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(int goalNum) {
        this.goalNum = goalNum;
    }

    @Override
    public String toString() {
        return "BookListVO{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookPic='" + bookPic + '\'' +
                ", totalNum=" + totalNum +
                ", goalNum=" + goalNum +
                '}';
    }
}
