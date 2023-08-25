package com.hais.hais1000.dao;

import com.hais.hais1000.dto.VirDevInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface VirDevInfoMapper {

    public List<VirDevInfo> getVirDevInfo(String projectID, String virDevTypeID);

    public VirDevInfo getVirDevInfoByID(String projectID, String virDevID);

    public void addVirDevInfo(VirDevInfo virDevInfo);

    public void delVirDevInfo(String projectID);
}
