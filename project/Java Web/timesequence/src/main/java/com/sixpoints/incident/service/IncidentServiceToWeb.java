package com.sixpoints.incident.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.entity.dynasty.IncidentListVO;
import com.sixpoints.entity.dynasty.IncidentVO;
import com.sixpoints.incident.dao.IncidentDao;
import com.sixpoints.picture.service.PictureService;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentServiceToWeb {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private IncidentDao incidentDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private PictureService pictureService;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //查询事件列表
    public List<Incident> getIncidentList(int id,int pageNum,int pageSize){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(id)){
            return new LinkedList<>();
        }
        if(redisUtil.exists("web-dynasty_incident-"+id)){
            return (List<Incident>) redisUtil.get("web-dynasty_incident-"+id);
        }
        //分页查询
        List<Incident> incidents = incidentDao.getIncidents(id, pageNum-1, pageSize);
        //存入redis中
        redisUtil.set("web-dynasty_incident-"+id,incidents);
        return incidents;
    }

    //根据id，查询事件详情
    public Incident getIncidentById(int incidentId){
        if(!auxiliaryBloomFilterUtil.incidentIdIsExist(incidentId)){
            return new Incident();
        }
        if(redisUtil.exists("web-incident-"+incidentId)){
            return (Incident) redisUtil.get("web-incident-"+incidentId);
        }
        //查询事件详情
        Optional<Incident> incidentOptional = incidentDao.findById(incidentId);
        if(incidentOptional.isPresent()){
            redisUtil.set("web-incident-"+incidentId,incidentOptional.get());
            return incidentOptional.get();
        }
        return new Incident();
    }

    //修改事件
    @Transactional
    public boolean modifyIncident(Incident tempIncident,List<MultipartFile> files){
        if(!auxiliaryBloomFilterUtil.incidentIdIsExist(tempIncident.getIncidentId())){
            return false;
        }
        Optional<Incident> incidentOptional = incidentDao.findById(tempIncident.getIncidentId());
        if(!incidentOptional.isPresent()){
            return false;
        }
        Incident incident = incidentOptional.get();
        incident.setIncidentName(tempIncident.getIncidentName());
        incident.setIncidentInfo(tempIncident.getIncidentInfo());
        incident.setIncidentCreator(tempIncident.getIncidentCreator());
        incident.setIncidentDialog(tempIncident.getIncidentDialog());
        Incident save = incidentDao.save(incident);
        if(save != null){
            //修改成功
            //获取朝代名称
            Optional<Integer> integerOptional = incidentDao.getDynastyId(save.getIncidentId());
            //获取名称
            String[] name = new String[4];
            name[0] = new String("incident/inc-"+ save.getIncidentId() +".png");
            name[1] = new String("incident/tang-"+ integerOptional.get() +"-other.png");
            name[2] = new String("incident/tang-"+ integerOptional.get() +"-1.png");
            name[3] = new String("incident/tang-"+ integerOptional.get() +"-2.png");
            //保存图片
            for(int i=0;i <files.size();i++){
                pictureService.pictureUpload(name[i],files.get(i));
            }


            //取消所有相关缓存
            if(redisUtil.exists("web-dynasty_incident-"+incident.getIncidentId())){
               redisUtil.remove("web-dynasty_incident-"+incident.getIncidentId());
            }
            if(redisUtil.exists("web-incident-"+incident.getIncidentId())){
                redisUtil.remove("web-incident-"+incident.getIncidentId());
            }
            if(redisUtil.exists("dynasty_incident-"+incident.getIncidentId())){
                redisUtil.remove("dynasty_incident-"+incident.getIncidentId());
            }
            if(redisUtil.exists("incident-"+incident.getIncidentId())){
                redisUtil.remove("incident-"+incident.getIncidentId());
            }
            //返回true
            return true;
        }
        //返回false
        return false;
    }

    //添加事件
    @Transactional
    public boolean addIncident(Incident incident, int dynastyId, List<MultipartFile> files){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynastyId)){
            return false;
        }
        //先获取朝代
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(dynastyId);
        if(dynastyOptional.isPresent()){
            incident.setIncidentCreationTime(System.currentTimeMillis());
            Dynasty dynasty = dynastyOptional.get();
            dynasty.getIncidents().add(incident);
            //保存
            Incident saveIncident = incidentDao.save(incident);
            Dynasty saveDynasty = dynastyDao.save(dynasty);
            if(saveDynasty!=null&&saveIncident!=null){
                //获取名称
                String[] name = new String[4];
                name[0] = new String("incident/inc-"+ saveIncident.getIncidentId() +".png");
                name[1] = new String("incident/tang-"+ saveDynasty.getDynastyId() +"-other.png");
                name[2] = new String("incident/tang-"+ saveDynasty.getDynastyId() +"-1.png");
                name[3] = new String("incident/tang-"+ saveDynasty.getDynastyId() +"-2.png");
                //保存图片
                for(int i=0;i <files.size();i++){
                    pictureService.pictureUpload(name[i],files.get(i));
                }
                saveIncident.setIncidentPicture(name[0]+"&&&"+name[1]+"&&&"+name[2]+"&&&"+name[3]);
                incidentDao.save(saveIncident);

                //清除相关缓存
                if(redisUtil.exists("web-dynasty_incident-"+dynastyId)){
                    redisUtil.remove("web-dynasty_incident-"+dynastyId);
                }
                if(redisUtil.exists("dynasty_incident-"+dynastyId)){
                    redisUtil.remove("dynasty_incident-"+dynastyId);
                }
                auxiliaryBloomFilterUtil.incidentAdd(saveIncident.getIncidentId());
                return true;
            }
            return false;
        }
        return false;
    }
}
