package com.hais.hais1000.service;

import com.hais.hais1000.dao.VirDevParamInfoMapper;
import com.hais.hais1000.dto.VirDevParamHisInfo;
import com.hais.hais1000.dto.VirDevParamInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "virDevParamInfoCache")
@Service
public class VirDevParamInfoService {
    @Autowired
    VirDevParamInfoMapper virDevParamInfoMapper;

    public List<VirDevParamInfo> getVirDevParamInfo(String virDevID){
        return virDevParamInfoMapper.getVirDevParamInfo(virDevID);
    }

    public Integer addVirDevParamInfoList(List<VirDevParamInfo> virDevParamInfoList){
        return virDevParamInfoMapper.addVirDevParamInfoList(virDevParamInfoList);
    }

    public void delVirDevParamInfo(String virDevID){
        virDevParamInfoMapper.delVirDevParamInfo(virDevID);
    }

    public void updateVirDevParamValue(String virDevID, Integer paramSeq, double paramValue){
        virDevParamInfoMapper.updateVirDevParamValue(virDevID, paramSeq, paramValue);
    }

    public void updateVirDevParamValueList(List<VirDevParamHisInfo> virDevParamHisInfoList){
        virDevParamInfoMapper.updateVirDevParamValueList(virDevParamHisInfoList);
    }
    /*
    public List<VirDevParamInfo> getVirDevParamInfo(String virDevID){
        List<VirDevParamInfo> virDevParamInfoList = new ArrayList<>();

        List<VirDevParamInfo> virDevParamInfoTmp = virDevParamInfoMapper.getVirDevParamInfo(virDevID);

        for(VirDevParamInfo virDevParamInfo: virDevParamInfoTmp){
            VirDevParamInfo virDevParamInfotmp = getVirDevParamInfoBySeq(virDevParamInfoTmp, virDevParamInfo.getVirDevID(), virDevParamInfo.getParamSeq());
            if(virDevParamInfotmp != null){
                virDevParamInfoList.add(virDevParamInfotmp);
            }
        }

        return virDevParamInfoList;
    }

    public Integer addVirDevParamInfoList(List<VirDevParamInfo> virDevParamInfoList){
        return virDevParamInfoMapper.addVirDevParamInfoList(virDevParamInfoList);
    }

    public void delVirDevParamInfo(String virDevID){
        List<VirDevParamInfo> virDevParamInfoTmp = virDevParamInfoMapper.getVirDevParamInfo(virDevID);
        for(VirDevParamInfo virDevParamInfo: virDevParamInfoTmp){
            delVirDevParamInfoBySeq(virDevParamInfo.getVirDevID(), virDevParamInfo.getParamSeq());
        }
        virDevParamInfoMapper.delVirDevParamInfo(virDevID);
    }

    @CachePut(key = "#result.virDevID+#result.paramSeq")
    public VirDevParamInfo updateVirDevParamValue(String virDevID, Integer paramSeq, double paramValue){

        Optional<VirDevParamInfo> virDevParamInfo = getVirDevParamInfo(virDevID).stream().filter(e -> e.getParamSeq().equals(paramSeq)).findFirst();

        if(virDevParamInfo.isPresent()){
            VirDevParamInfo devParamInfo = virDevParamInfo.get();
            devParamInfo.setParamValue(paramValue);
            System.out.println("更新缓存："+virDevID+"--"+paramSeq+"--"+paramValue);
            return devParamInfo;
        }

        return null;
    }

    @CacheEvict(key = "#virDevID+#paramSeq")
    public void delVirDevParamInfoBySeq(String virDevID, Integer paramSeq){
        System.out.println("删除缓存："+virDevID+"---"+paramSeq);
    }

    @Cacheable(key = "#virDevID+#paramSeq")
    public VirDevParamInfo getVirDevParamInfoBySeq(List<VirDevParamInfo> virDevParamInfoList,String virDevID, Integer paramSeq){
        Optional<VirDevParamInfo> virDevParamInfo = virDevParamInfoList.stream().filter(e->e.getParamSeq().equals(paramSeq)).findFirst();
        if(virDevParamInfo.isPresent()){
            System.out.println("加入缓存："+virDevID+"--"+paramSeq);
            return virDevParamInfo.get();
        }

        return null;
    }
*/

}
