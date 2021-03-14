package com.sixpoints.status.dao;

import com.sixpoints.entity.status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserStatusDao extends JpaRepository<UserStatus,Integer>, JpaSpecificationExecutor<UserStatus> {
}
