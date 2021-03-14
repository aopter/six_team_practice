package com.sixpoints.userdetails.dao;

import com.sixpoints.entity.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDetailsDao extends JpaRepository<UserDetails,Integer>, JpaSpecificationExecutor<UserDetails> {
}
