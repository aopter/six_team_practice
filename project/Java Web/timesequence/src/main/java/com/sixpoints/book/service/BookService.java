package com.sixpoints.book.service;

import com.sixpoints.book.dao.BookDao;
import com.sixpoints.entity.book.Book;
import com.sixpoints.entity.book.BookListVO;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class BookService {

    @Resource
    private BookDao bookDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

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


}
