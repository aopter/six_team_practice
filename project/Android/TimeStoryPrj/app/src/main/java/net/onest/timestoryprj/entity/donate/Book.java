package net.onest.timestoryprj.entity.donate;

public class Book {
    private Integer bookId;//图书流水号
    private String bookName;//图书名称
    private String bookDes;//图书描述
    private String bookPic;//图书图片存放路径
    private int TotalNum;//图书数量
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
        TotalNum = totalNum;
        this.goalNum = goalNum;
        this.flag = flag;
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

    public int getTotalNum() {
        return TotalNum;
    }

    public void setTotalNum(int totalNum) {
        TotalNum = totalNum;
    }

    public int getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(int goalNum) {
        this.goalNum = goalNum;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookDes='" + bookDes + '\'' +
                ", bookPic='" + bookPic + '\'' +
                ", TotalNum=" + TotalNum +
                ", goalNum=" + goalNum +
                ", flag=" + flag +
                '}';
    }
}
