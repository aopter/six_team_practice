package com.sixpoints.user.dao;

import com.sixpoints.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {

    @Query(value="select * from us_user order by user_experience desc limit 0,20",nativeQuery=true)
    List<User> findList();

    @Query(value="select * from us_user where user_account = ?1 and user_password = ?2 limit 0,1",nativeQuery=true)
    Optional<User> existsUser(String userAccount, String userPassword);

    @Query(value="select * from us_user where user_account = ?1 limit 0,1",nativeQuery=true)
    Optional<User> existsUser(String userAccount);

    @Query(value="select count(*) from us_user",nativeQuery=true)
    int getAllCount();

}
