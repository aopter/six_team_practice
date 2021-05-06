package com.sixpoints.userunlockdynasty.dao;

import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.dynasty.UserUnlockDynasty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserUnlockDynastyDao extends JpaRepository<UserUnlockDynasty,Integer>, JpaSpecificationExecutor<UserUnlockDynasty> {

    @Query(value="select * from us_user_unlock_dynasty where dynasty_id = ?2 and user_id = ?1",nativeQuery=true)
    Optional<UserUnlockDynasty> findCount(int userId, int dynastyId);

    @Query(value="select * from us_user_unlock_dynasty where user_id = ?1",nativeQuery=true)
    List<UserUnlockDynasty> findUserUnlockDynastyByUserId(int userId);
}
