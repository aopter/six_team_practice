package com.sixpoints.dynasty.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.DynastyListVO;
import com.sixpoints.entity.dynasty.DynastyVO;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class DynastyService {

    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //获取朝代id和名称列表
    public List<DynastyListVO> getDynastyList(){
        if(redisUtil.exists("dynasty-getDynastyList")){
            return (List<DynastyListVO>) redisUtil.get("dynasty-getDynastyList");
        }
        //创建返回数组
        List<DynastyListVO> listVOS = new LinkedList<>();
        //获取朝代信息
        List<Dynasty> dynasties = dynastyDao.findAll();
        //将信息封装入list中
        for(Dynasty dynasty : dynasties){
            DynastyListVO temp = new DynastyListVO(dynasty.getDynastyId(),dynasty.getDynastyName());
            listVOS.add(temp);
        }
        //存入redis中
        redisUtil.set("dynasty-getDynastyList",listVOS);
        //返回信息
        return listVOS;
    }

    //根据朝代的id值，查询朝代的详细
    public DynastyVO getDynastyDetailsById(int id){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(id)){
            return new DynastyVO();
        }
        //redis判断
        if(redisUtil.exists("dynasty-"+id)){
            return (DynastyVO) redisUtil.get("dynasty-"+id);
        }
        //查询对应的朝代信息
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(id);
        DynastyVO dynastyVO = null;
        //封装信息
        if(dynastyOptional.isPresent()){
            Dynasty dynasty = dynastyOptional.get();
            dynastyVO = new DynastyVO(dynasty.getDynastyId(),dynasty.getDynastyName(),dynasty.getDynastyTime(),dynasty.getDynastyInfo());
        }else{
            dynastyVO = new DynastyVO();
        }
        //存入redis
        redisUtil.set("dynasty-"+id,dynastyVO);
        //返回信息对象
        return dynastyVO;
    }
}
