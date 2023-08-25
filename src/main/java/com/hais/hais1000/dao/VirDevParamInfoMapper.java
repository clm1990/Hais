package com.hais.hais1000.dao;

import com.hais.hais1000.dto.VirDevParamHisInfo;
import com.hais.hais1000.dto.VirDevParamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VirDevParamInfoMapper {
    public List<VirDevParamInfo> getVirDevParamInfo(@Param("virDevID")String virDevID);

    public void addVirDevParamInfo(VirDevParamInfo virDevParamInfo);

    public Integer addVirDevParamInfoList(List<VirDevParamInfo> virDevParamInfoList);

    public void delVirDevParamInfo(@Param("virDevID")String virDevID);

    public void updateVirDevParamValue(String virDevID, Integer paramSeq, double paramValue);

    public void updateVirDevParamValueList(@Param("virDevParamHisInfoList") List<VirDevParamHisInfo> virDevParamHisInfoList);
}
