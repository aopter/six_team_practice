package com.sixpoints.pre.service;

import com.sixpoints.card.dao.CardDao;
import com.sixpoints.constant.Constant;
import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.card.Card;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.Incident;
import com.sixpoints.entity.dynasty.Problem;
import com.sixpoints.entity.dynasty.SearchIncident;
import com.sixpoints.entity.user.User;
import com.sixpoints.entity.user.recharge.UserRecharge;
import com.sixpoints.incident.dao.IncidentDao;
import com.sixpoints.problem.dao.ProblemDao;
import com.sixpoints.user.dao.UserDao;
import com.sixpoints.userrecharge.dao.UserRechargeDao;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.BloomFilterHelper;
import com.sixpoints.utils.RedisBloomFilter;
import com.sixpoints.utils.RedisUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class PreService {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CardDao cardDao;

    @Resource
    private IncidentDao incidentDao;

    @Resource
    private UserRechargeDao userRechargeDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private ProblemDao problemDao;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    @Resource
    private SolrClient solrClient;

    public boolean preStart() {
        //为每一张已有卡片在缓存中添加一个访问计数器

        //查询出所有卡片
        List<Card> cardList = cardDao.findAll();
        if (cardList == null) {
            return false;
        }
        //为每个卡片添加一个计数器
        for (int i = 0; i < cardList.size(); i++) {
            if (!redisUtil.exists("card-count-" + cardList.get(i).getCardId())) {
                redisUtil.set("card-count-" + cardList.get(i).getCardId(), 0);
            }
        }

        //为每个事件详情添加一个访问计数器

        //查询出所有的事件
        List<Incident> incidentList = incidentDao.findAll();
        if (incidentList == null) {
            return false;
        }
        //为每个事件添加一个计数器
        for (int i = 0; i < incidentList.size(); i++) {
            if (!redisUtil.exists("incident-count-" + incidentList.get(i).getIncidentId())) {
                redisUtil.set("incident-count-" + incidentList.get(i).getIncidentId(), 0);
            }
        }

        return true;
    }

    public Map getPre() {
        Map map = new HashMap();
        //用户总人数
        int allUserCount = userDao.getAllCount();
        map.put("userCount", allUserCount);
        //用户充值总数
        int total = 0;
        List<UserRecharge> userRechargeList = userRechargeDao.findAll();
        if (userRechargeList != null) {
            for (int i = 0; i < userRechargeList.size(); i++) {
                total += userRechargeList.get(i).getPricing().getPricingMoney();
            }
            map.put("totalMoney", total);
        } else {
            map.put("totalMoney", 0);
        }
        //充值用户人数
        int rechargeUserCount = 0;
        Optional<List<Integer>> distinctUser = userRechargeDao.findDistinctUser();
        if (distinctUser.isPresent()) {
            rechargeUserCount = distinctUser.get().size();
        }
        //充值占总人数的比例
        float ret = (float) ((rechargeUserCount + 0.0) / allUserCount);
        String retS = new DecimalFormat("###.##").format(ret);
        map.put("ret", retS);
        //查看次数两个的卡片名称
//        List<Card> cardList = cardDao.findAll();
//        if(cardList != null){
//
//        }
        //查看事件详情次数前五的朝代名称
        return map;
    }


    //将所有的用户id加入布隆过滤器
    @Transactional
    public boolean addBloomFilter() {
        List<User> userList = userDao.findAll();
        List<Dynasty> dynastyList = dynastyDao.findAll();
        List<Incident> incidentList = incidentDao.findAll();
        List<Card> cardList = cardDao.findAll();
        List<Problem> problemList = problemDao.findAll();
        if (userList == null || dynastyList == null || incidentList == null || cardList == null || problemList == null) {
            return false;
        }
        for (int i = 0; i < userList.size(); i++) {
            auxiliaryBloomFilterUtil.userIdAdd(userList.get(i).getUserId());
        }
        for (int i = 0; i < dynastyList.size(); i++) {
            auxiliaryBloomFilterUtil.dynastyIdAdd(dynastyList.get(i).getDynastyId());
        }
        for (int i = 0; i < incidentList.size(); i++) {
            auxiliaryBloomFilterUtil.incidentAdd(incidentList.get(i).getIncidentId());
        }
        for (int i = 0; i < cardList.size(); i++) {
            auxiliaryBloomFilterUtil.cardIdAdd(cardList.get(i).getCardId());
        }
        for (int i = 0; i < problemList.size(); i++) {
            auxiliaryBloomFilterUtil.problemIdAdd(problemList.get(i).getProblemId());
        }
        return true;
    }

    // 建立所有事件的全文检索
    public void addIncidentIndex() {
        // 1、获取所有事件信息
        List<SearchIncident> searchIncidents = new LinkedList<>();
        // 查询所有的朝代
        List<Dynasty> dynasties = dynastyDao.findAll();
        // 如果有朝代
        if (!dynasties.isEmpty()) {
            for (Dynasty dynasty : dynasties) {
                // 通过朝代id查询所有事件
                Optional<List<Incident>> listOptional = incidentDao.getOrderIncidents(dynasty.getDynastyId());
                // 有数据
                if (!listOptional.isPresent()) {
                    continue;
                }
                Integer dynastyId = dynasty.getDynastyId();
                String dynastyName = dynasty.getDynastyName();
                //封装数据
                for (Incident incident : listOptional.get()) {
                    // 特定的事件
                    SearchIncident searchIncident = new SearchIncident(incident.getIncidentId(), incident.getIncidentName(), incident.getIncidentInfo(), dynastyId, dynastyName);
                    searchIncidents.add(searchIncident);
                }
            }
        }
        List<SolrInputDocument> docs = this.createDoc(searchIncidents);
        try {
            solrClient.add(docs);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 建立文档集合
    private List<SolrInputDocument> createDoc(List<SearchIncident> searchIncidents) {
        List<SolrInputDocument> documents = new ArrayList<>();
        for (SearchIncident searchIncident : searchIncidents) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchIncident.getIncidentId());
            document.addField("incidentName", searchIncident.getIncidentName());
            document.addField("incidentInfo", searchIncident.getIncidentInfo());
            document.addField("dynastyId", searchIncident.getDynastyId());
            document.addField("dynastyName", searchIncident.getDynastyName());
            documents.add(document);
        }
        return documents;
    }
}
