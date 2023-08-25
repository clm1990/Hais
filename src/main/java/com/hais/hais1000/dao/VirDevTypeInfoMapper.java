package com.hais.hais1000.dao;

import com.hais.hais1000.dto.VirDevTypeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VirDevTypeInfoMapper {
    public List<VirDevTypeInfo> getVirDevTypeInfo();
}
