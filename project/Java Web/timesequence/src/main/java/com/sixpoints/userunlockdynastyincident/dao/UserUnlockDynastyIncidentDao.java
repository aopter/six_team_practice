package com.sixpoints.userunlockdynastyincident.dao;

import com.sixpoints.entity.user.dynasty.UserUnlockDynastyIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserUnlockDynastyIncidentDao extends JpaRepository<UserUnlockDynastyIncident,Integer>, JpaSpecificationExecutor<UserUnlockDynastyIncident> {

    @Query(value="select * from us_user_unlock_dynasty_incident where user_id=?1 and dynasty_id=?2",nativeQuery=true)
    List<UserUnlockDynastyIncident> findUnlockList(int userId,int dynastyId);

    @Query(value="select * from us_user_unlock_dynasty_incident where user_id=?1 and dynasty_id=?2 and incident_id=?3",nativeQuery=true)
    Optional<UserUnlockDynastyIncident> findUnlockIncident(int userId, int dynastyId,int incidentId);
}
