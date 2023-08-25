package com.hais.hais1000.dao;

import com.hais.hais1000.dto.VirDevParamHisInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VirDevParamHisInfoMapper {

    public Integer checkTableExistsWithSchema(String tableName);

    public void createHisTable(@Param("tableName") String tableName);

    public void addVirDevHisInfo(@Param("tableName") String tableName, @Param("virDevParamHisInfo") VirDevParamHisInfo virDevParamHisInfo);

    public void addListVirDevHisInfo(@Param("tableName") String tableName, @Param("listVirDevParamHisInfo") List<VirDevParamHisInfo> listVirDevParamHisInfo);

    public VirDevParamHisInfo getVirDevHisInfoLimit(@Param("projectID") String projectID, @Param("virDevID") String virDevID, @Param("virParamSeq") Integer virParamSeq, @Param("startTime") String startTime);

    public List<VirDevParamHisInfo> getVirDevHisInfo(@Param("projectID") String projectID, @Param("virDevID") String virDevID, @Param("virParamSeq") Integer virParamSeq, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
