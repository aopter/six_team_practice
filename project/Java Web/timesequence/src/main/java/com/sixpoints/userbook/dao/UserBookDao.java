package com.sixpoints.userbook.dao;

import com.sixpoints.entity.user.book.UserBookProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBookDao extends JpaRepository<UserBookProcess, Integer>, JpaSpecificationExecutor<UserBookProcess> {

    @Query(value = "select * from us_user_book where book_id =?1 and process = 100 order by donate_time desc limit 0,15", nativeQuery = true)
    List<UserBookProcess> findListByBookId(int bookId);
}

