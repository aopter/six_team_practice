package com.sixpoints.book.controller;

import com.sixpoints.book.service.BookService;
import com.sixpoints.entity.book.BookListVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    @RequestMapping("/list")
    @ResponseBody
    public List<BookListVO> bookList(){
        return bookService.getBookList();
    }
}
