package com.hais.hais1000.dao;

import com.hais.hais1000.dto.DataLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataLogMapper {
    public List<DataLog> getLogByMacAddr(@Param("macAddr") String macAddr);

    public void addDataLog(DataLog dataLog);
}
