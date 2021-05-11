package com.sixpoints.book.dao;

import com.sixpoints.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookDao extends JpaRepository<Book,Integer>, JpaSpecificationExecutor<Book> {
}
