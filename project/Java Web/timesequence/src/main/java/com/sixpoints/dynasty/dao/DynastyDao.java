package com.sixpoints.dynasty.dao;

import com.sixpoints.entity.dynasty.Dynasty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DynastyDao extends JpaRepository<Dynasty,Integer>, JpaSpecificationExecutor<Dynasty> {
}
