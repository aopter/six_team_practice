package net.onest.timestoryprj.entity.donate;

/**
 * 用户正在进行的项目
 */
public class UserBookListVO {
    private Integer processId;//捐赠的标识符
    private int process; // 捐赠图书的进度
    private BookListVO bookListVO;  // 项目的公益图书详情

    public UserBookListVO() {
    }

    public UserBookListVO(Integer processId, int process, BookListVO bookListVO) {
        this.processId = processId;
        this.process = process;
        this.bookListVO = bookListVO;
    }

    @Override
    public String toString() {
        return "UserBookListVO{" +
                "processId=" + processId +
                ", process=" + process +
                ", bookListVO=" + bookListVO +
                '}';
    }
}
