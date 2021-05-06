package com.sixpoints.incident.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.*;
import com.sixpoints.entity.user.dynasty.UserUnlockDynasty;
import com.sixpoints.incident.dao.IncidentDao;
import com.sixpoints.userunlockdynasty.dao.UserUnlockDynastyDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class IncidentService {
    @Autowired
    private SolrClient solrClient;

    @Resource
    private UserUnlockDynastyDao userUnlockDynastyDao;

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

    // 根据关键词对事件进行全文搜索
    public List<SearchIncident> getIncidentListByKey(int userId, String keywords) {
        List<UserUnlockDynasty> userUnlockDynasties = userUnlockDynastyDao.findUserUnlockDynastyByUserId(userId);
        int maxDynastyId = 1;
        for (UserUnlockDynasty userUnlockDynasty : userUnlockDynasties) {
            if (maxDynastyId <= userUnlockDynasty.getDynasty().getDynastyId()) {
                maxDynastyId = userUnlockDynasty.getDynasty().getDynastyId();
            }
        }
        List<SearchIncident> searchIncidents = new LinkedList<>();
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("incidentName:" + keywords + " OR ");
            buffer.append("incidentInfo:" + keywords);
            SolrQuery solrQuery = new SolrQuery(buffer.toString());
            solrQuery.setRows(15);
            // 设置高亮排序
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("incidentName");
            solrQuery.addHighlightField("incidentInfo");
            solrQuery.setHighlightSimplePre("<font color='red'>");
            solrQuery.setHighlightSimplePost("</font>");
            QueryResponse query = solrClient.query(solrQuery);
            SolrDocumentList results = query.getResults();
            System.out.println("查询到的数目：" + results.getNumFound());
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            for (SolrDocument result : results) {
                SearchIncident incident = new SearchIncident();
                incident.setIncidentId(Integer.parseInt(result.getFieldValue("id").toString()));
                incident.setIncidentName(result.getFieldValue("incidentName").toString());
                incident.setIncidentInfo(result.getFieldValue("incidentInfo").toString());
                incident.setDynastyId(Integer.parseInt(result.getFieldValue("dynastyId").toString()));
                incident.setDynastyName(result.getFieldValue("dynastyName").toString());
                // 处理是否解锁问题
                if (incident.getDynastyId() <= maxDynastyId) {
                    incident.setFlag(true);
                }
                // 处理高亮
                if (solrQuery.getHighlight()) {
                    if (null != highlighting && !highlighting.isEmpty()) {
                        for (Map.Entry<String, Map<String, List<String>>> entry : highlighting.entrySet()) {
                            String key = entry.getKey();
                            if (key.equals(incident.getIncidentId().toString())) {
                                Map<String, List<String>> value = entry.getValue();
                                if (value.containsKey("incidentName")) {
                                    incident.setIncidentName(value.get("incidentName").get(0));
                                }
                                if (value.containsKey("incidentInfo")) {
                                    incident.setIncidentInfo(value.get("incidentInfo").get(0));
                                }
                            }
                        }
                    }
                }
                System.out.println(incident);
                searchIncidents.add(incident);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchIncidents;
    }
}
