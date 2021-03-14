package com.sixpoints.dynasty.service;

import com.sixpoints.dynasty.dao.DynastyDao;
import com.sixpoints.entity.dynasty.Dynasty;
import com.sixpoints.entity.dynasty.DynastyListVO;
import com.sixpoints.utils.AuxiliaryBloomFilterUtil;
import com.sixpoints.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DynastyServiceToWeb {
    @Resource
    private DynastyDao dynastyDao;

    @Resource
    private DynastyService dynastyService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AuxiliaryBloomFilterUtil auxiliaryBloomFilterUtil;

    //查询朝代的列表
    public List<DynastyListVO> getDynastyList(){
        return dynastyService.getDynastyList();
    }

    //修改朝代的信息
    public boolean modifyDynasty(Dynasty dynasty){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(dynasty.getDynastyId())){
            return false;
        }
        Optional<Dynasty> orginDynastyOptional = dynastyDao.findById(dynasty.getDynastyId());
        if(!orginDynastyOptional.isPresent()){
            return false;
        }
        Dynasty orginDynasty = orginDynastyOptional.get();
        orginDynasty.setDynastyName(dynasty.getDynastyName());
        orginDynasty.setDynastyTime(dynasty.getDynastyTime());
        orginDynasty.setDynastyInfo(dynasty.getDynastyInfo());
        orginDynasty.setDynastyCreator(dynasty.getDynastyCreator());
        //修改朝代信息
        Dynasty save = dynastyDao.save(orginDynasty);
        //修改成功删除缓存中的数据
        if(save != null){
            //清除朝代列表缓存
            if(redisUtil.exists("dynasty-getDynastyList")){
                redisUtil.remove("dynasty-getDynastyList");
            }
            if(redisUtil.exists("web-dynasty-"+dynasty.getDynastyId())){
                redisUtil.remove("web-dynasty-"+dynasty.getDynastyId());
            }
            if(redisUtil.exists("dynasty-"+dynasty.getDynastyId())){
                redisUtil.remove("dynasty-"+dynasty.getDynastyId());
            }
            //返回结果
            return true;
        }
        //返回结果
        return false;
    }

    //添加朝代
    public boolean createDynasty(Dynasty dynasty){
        dynasty.setDynastyCreationTime(System.currentTimeMillis());
        Dynasty save = dynastyDao.save(dynasty);
        if(save != null){
            if(redisUtil.exists("dynasty-getDynastyList")){
                redisUtil.remove("dynasty-getDynastyList");
            }
            auxiliaryBloomFilterUtil.dynastyIdAdd(save.getDynastyId());
            return true;
        }
        return false;
    }

    //根据朝代的id值，查询朝代的详细
    public Dynasty getDynastyDetails(int id){
        if(!auxiliaryBloomFilterUtil.dynastyIdIsExist(id)){
            return new Dynasty();
        }
        //redis判断
        if(redisUtil.exists("web-dynasty-"+id)){
            return (Dynasty) redisUtil.get("web-dynasty-"+id);
        }
        //查询对应的朝代信息
        Optional<Dynasty> dynastyOptional = dynastyDao.findById(id);
        Dynasty dynasty = null;
        //封装信息
        if(dynastyOptional.isPresent()){
            dynasty = dynastyOptional.get();
        }else{
            dynasty = new Dynasty();
        }
        //存入redis
        redisUtil.set("web-dynasty-"+id,dynasty);
        //返回信息对象
        return dynasty;
    }
}
