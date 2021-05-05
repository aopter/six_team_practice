package com.sixpoints;

import com.sixpoints.constant.Constant;
import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.utils.BloomFilterHelper;
import com.sixpoints.utils.RedisBloomFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class   TimesequenceApplicationTests {

    @Autowired
    private DynastyDao dynastyDao;

    @Resource(name = "userIdBloomFilterHelper")
    private BloomFilterHelper bloomFilterHelper;

    @Resource
    private RedisBloomFilter redisBloomFilter;

    @Test
    void contextLoads() {
        System.out.printf(dynastyDao.findById(1).toString());
    }

    @Test
    void addToBloom(){
        redisBloomFilter.addByBloomFilter(bloomFilterHelper,"bloom",1);
    }

    @Test
    void isexit(){
        System.out.printf(redisBloomFilter.includeByBloomFilter(bloomFilterHelper, Constant.USER_ID_BLOOM_FILTER,8)+"");
    }
}
