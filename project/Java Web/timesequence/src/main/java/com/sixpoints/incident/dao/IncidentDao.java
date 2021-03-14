package com.sixpoints.incident.dao;

import com.sixpoints.entity.dynasty.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IncidentDao extends JpaRepository<Incident,Integer>, JpaSpecificationExecutor<Incident> {

    //通过朝代id，查询事件，分页查询
    @Query(value="select * from dy_incident where dynasty_id = ?1 order by incident_creation_time limit ?2,?3",nativeQuery=true)
    List<Incident> getIncidents(int dynastyId,int pageNum,int pageSize);

    @Query(value="select dynasty_id from dy_incident where incident_id = ?1",nativeQuery=true)
    Optional<Integer> getDynastyId(int incidentId);

    @Query(value="select * from dy_incident where dynasty_id = ?1 order by incident_id",nativeQuery=true)
    Optional<List<Incident>> getOrderIncidents(int dynastyId);
}
