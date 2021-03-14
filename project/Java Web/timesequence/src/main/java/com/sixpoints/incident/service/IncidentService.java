package com.sixpoints.incident.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.entity.dynasty.IncidentListVO;
import com.sixpoints.entity.dynasty.IncidentVO;
import com.sixpoints.incident.dao.IncidentDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class IncidentService {
    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private IncidentDao incidentDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //查询事件列表
    public List<IncidentListVO> getIncidentList(int id){//朝代id
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(id)){
            return new LinkedList<>();
        }
        if(redisUtil.exists("dynasty_incident-"+id)){
            return (List<IncidentListVO>) redisUtil.get("dynasty_incident-"+id);
        }
        //查询朝代
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(id);
        //创建数组
        List<IncidentListVO> listVOS = new LinkedList<>();
        if(dynastyOptional.isPresent()){
            //查询数据
            Optional<List<Incident>> listOptional = incidentDao.getOrderIncidents(id);
            if(!listOptional.isPresent()){
                return listVOS;
            }
            List<Incident> list = listOptional.get();
            //封装数据
            for(int i=0;i<list.size();i++){
                Incident incident = list.get(i);
                IncidentListVO incidentListVO = new IncidentListVO(incident.getIncidentId(),incident.getIncidentName(),incident.getIncidentPicture());
                listVOS.add(incidentListVO);
            }
        }
        //存入redis中
        redisUtil.set("dynasty_incident-"+id,listVOS);
        return listVOS;
    }

    //根据id，查询事件详情
    public IncidentVO getIncidentDetailsById(int id){
        if(!auxiliaryBloomFilterUtil.incidentIdIsExist(id)){
            return new IncidentVO();
        }
        if(redisUtil.exists("incident-count-"+id)){
            redisUtil.incr("incident-count-"+id);
        }
        if(redisUtil.exists("incident-"+id)){
            return (IncidentVO) redisUtil.get("incident-"+id);
        }
        //根据id查询事件
        Optional<Incident> incidentOptional = incidentDao.findById(id);
        IncidentVO incidentVO = null;
        if(incidentOptional.isPresent()){
            //封装事件
            Incident incident = incidentOptional.get();
            incidentVO = new IncidentVO(incident.getIncidentId(),incident.getIncidentName(),incident.getIncidentInfo(),incident.getIncidentPicture(),incident.getIncidentDialog());
        }else{
            incidentVO = new IncidentVO();
        }
        //存入redis中
        redisUtil.set("incident-"+id,incidentVO);
        //返回事件信息
        return incidentVO;
    }
}
