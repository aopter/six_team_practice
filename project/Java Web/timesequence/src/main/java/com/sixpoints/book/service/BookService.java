package com.sixpoints.book.service;

import com.sixpoints.book.dao.BookDao;
import com.sixpoints.entity.book.Book;
import com.sixpoints.entity.book.BookListVO;
import com.sixpoints.entity.book.BookVO;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Resource
    private BookDao bookDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    // 获取全部公益图书列表
    public List<BookListVO> getBookList() {
        if (redisUtil.exists("book-getBookList")) {
            return (List<BookListVO>) redisUtil.get("book-getBookList");
        }
        //创建返回数组
        List<BookListVO> listVOS = new LinkedList<>();
        //获取图书信息
        List<Book> books = bookDao.findAll();
        //将信息封装入list中
        for (Book book : books) {
            if (book.getFlag() == 1) {
                BookListVO temp = new BookListVO(book.getBookId(), book.getBookName(), book.getBookPic(), book.getTotalNum(), book.getGoalNum());
                listVOS.add(temp);
            }
        }
        //存入redis中
        redisUtil.set("book-getBookList", listVOS);
        //返回信息
        return listVOS;
    }

    /**
     * 根据bookId，查询特定公益图书详情
     * 封装信息
     * 返回信息
     */
    public BookVO getSpecificBookById(int bookId) {
        if (!auxiliaryBloomFilterUtil.bookIdIsExist(bookId)) {//不存在
            return new BookVO();
        }
        if (redisUtil.exists("book-count-" + bookId)) {
            redisUtil.incr("book-count-" + bookId);
        }
        if (redisUtil.exists("book-" + bookId)) {
            return (BookVO) redisUtil.get("book-" + bookId);
        }
        Optional<Book> bookOptional = bookDao.findById(bookId);
        BookVO bookVO = null;
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            bookVO = new BookVO(book.getBookId(), book.getBookName(), book.getBookDes(), book.getBookPic());
        } else {
            bookVO = new BookVO();
        }
        redisUtil.set("book-" + bookId, bookVO);
        return bookVO;
    }
}
