package com.sixpoints.problem.dao;

import com.sixpoints.entity.dynasty.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProblemDao extends JpaRepository<Problem,Integer>, JpaSpecificationExecutor<Problem> {

    @Query(value="select count(*) from dy_problem where problem_type = ?1 and dynasty_id = ?2",nativeQuery=true)
    int getCount(int problemType,int dynastyId);

    @Query(value="select * from dy_problem where problem_type = ?1 and dynasty_id = ?2 order by problem_id limit ?3,1",nativeQuery=true)
    Optional<Problem> getProblem(int problemType, int dynastyId, int num);

    @Query(value="select * from dy_problem where problem_type = ?2 and dynasty_id = ?1 order by problem_id",nativeQuery=true)
    Optional<List<Problem>> getProblemList(int dynastyId,int problemType);
}
