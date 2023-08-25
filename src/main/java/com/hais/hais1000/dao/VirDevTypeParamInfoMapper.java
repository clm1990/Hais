package com.hais.hais1000.dao;

import com.hais.hais1000.dto.VirDevTypeParamInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VirDevTypeParamInfoMapper {
    public List<VirDevTypeParamInfo> getVirDevTypeParamInfo(String virDevTypeID);
}
