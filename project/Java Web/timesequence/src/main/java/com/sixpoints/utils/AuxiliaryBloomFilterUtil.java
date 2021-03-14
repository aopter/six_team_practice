package com.sixpoints.utils;

import com.sixpoints.constant.Constant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AuxiliaryBloomFilterUtil {

    @Resource(name = "userIdBloomFilterHelper")
    private BloomFilterHelper userIdBloomFilterHelper;

    @Resource(name = "dynastyIdBloomFilterHelper")
    private BloomFilterHelper dynastyIdBloomFilterHelper;

    @Resource(name = "incidentIdBloomFilterHelper")
    private BloomFilterHelper incidentIdBloomFilterHelper;

    @Resource(name = "cardIdBloomFilterHelper")
    private BloomFilterHelper cardIdBloomFilterHelper;

    @Resource(name = "problemIdBloomFilterHelper")
    private BloomFilterHelper problemIdBloomFilterHelper;

    @Resource
    private RedisBloomFilter redisBloomFilter;

    public void userIdAdd(int userId){
        redisBloomFilter.addByBloomFilter(userIdBloomFilterHelper, Constant.USER_ID_BLOOM_FILTER,userId);
    }

    public void dynastyIdAdd(int dynastyId){
        redisBloomFilter.addByBloomFilter(dynastyIdBloomFilterHelper, Constant.DYNASTY_ID_BLOOM_FILTER,dynastyId);
    }

    public void incidentAdd(int incidentId){
        redisBloomFilter.addByBloomFilter(incidentIdBloomFilterHelper, Constant.INCIDENT_ID_BLOOM_FILTER,incidentId);
    }

    public void cardIdAdd(int cardId){
        redisBloomFilter.addByBloomFilter(cardIdBloomFilterHelper, Constant.CARD_ID_BLOOM_FILTER,cardId);
    }

    public void problemIdAdd(int problemId){
        redisBloomFilter.addByBloomFilter(problemIdBloomFilterHelper,Constant.PROBLEM_ID_BLOOM_FILTER,problemId);
    }

    public boolean userIdIsExist(int userId){
        return redisBloomFilter.includeByBloomFilter(userIdBloomFilterHelper, Constant.USER_ID_BLOOM_FILTER,userId);
    }

    public boolean dynastyIdIsExist(int dynastyId){
        return redisBloomFilter.includeByBloomFilter(dynastyIdBloomFilterHelper, Constant.DYNASTY_ID_BLOOM_FILTER,dynastyId);
    }

    public boolean incidentIdIsExist(int incidentId){
        return redisBloomFilter.includeByBloomFilter(incidentIdBloomFilterHelper, Constant.INCIDENT_ID_BLOOM_FILTER,incidentId);
    }

    public boolean cardIdIsExist(int cardId){
        return redisBloomFilter.includeByBloomFilter(cardIdBloomFilterHelper, Constant.CARD_ID_BLOOM_FILTER,cardId);
    }

    public boolean problemIsExist(int problemId){
        return redisBloomFilter.includeByBloomFilter(problemIdBloomFilterHelper,Constant.PROBLEM_ID_BLOOM_FILTER,problemId);
    }
}
