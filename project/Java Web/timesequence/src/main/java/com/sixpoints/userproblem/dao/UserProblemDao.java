package com.sixpoints.userproblem.dao;

import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.user.dynasty.UserProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProblemDao extends JpaRepository<UserProblem,Integer>, JpaSpecificationExecutor<UserProblem> {

    @Query(value="select * from us_user_problem where user_id = ?1 and dynasty_id = ?2 and problem_id = ?3",nativeQuery=true)
    Optional<UserProblem> findUserProblem(int userId, int dynastyId, int problemId);

    @Query(value="select count(*) from us_user_problem where user_id = ?1 and dynasty_id = ?2",nativeQuery=true)
    int userProblemsCount(int userId,int dynastyId);

    @Query(value="select * from us_user_problem where user_id = ?1 and dynasty_id = ?2 limit ?3,?4",nativeQuery=true)
    List<UserProblem> findUserProblems(int userId,int dynastyId,int pageNum,int pageSize);

    @Query(value="select count(*) from us_user_problem where user_id = ?1",nativeQuery=true)
    int userProblemsCount(int userId);

    @Query(value="select * from us_user_problem where user_id = ?1 limit ?2,?3",nativeQuery=true)
    List<UserProblem> findUserProblems(int userId,int pageNum,int pageSize);

    @Query(value="select count(*) from us_user_problem,dy_problem " +
            "where us_user_problem.problem_id=dy_problem.problem_id and user_id=?1 and problem_type=?2",nativeQuery=true)
    int countUserProblemByUserIdAndProblemType(int userId,int problemType);

    @Query(value="select * from us_user_problem where user_id=?1 and problem_id in " +
            "(select problem_id from dy_problem where problem_type=?2) limit ?3,?4",nativeQuery=true)
    Optional<List<UserProblem>> UserProblemListByUserIdAndProblemType(int userId,int problemType,int pageNum,int pageSize);
}
