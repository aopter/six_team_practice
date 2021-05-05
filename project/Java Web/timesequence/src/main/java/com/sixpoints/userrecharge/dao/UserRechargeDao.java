package com.sixpoints.userrecharge.dao;

import com.sixpoints.entity.user.recharge.UserRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRechargeDao extends JpaRepository<UserRecharge,Integer>, JpaSpecificationExecutor<UserRecharge> {

    @Query(value="SELECT DISTINCT user_id FROM us_user_recharge",nativeQuery=true)
    Optional<List<Integer>> findDistinctUser();
}
