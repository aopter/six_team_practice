package com.sixpoints.team.dao;

import com.sixpoints.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TeamDao extends JpaRepository<Team,Integer>, JpaSpecificationExecutor<Team> {
}
