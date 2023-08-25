package com.hais.hais1000.dao;

import com.hais.hais1000.dto.ControlLog;
import com.hais.hais1000.dto.DataLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ControlLogMapper {

    public List<ControlLog> getLogByMacAddr(@Param("macAddr") String macAddr);

    public void addControlLog(ControlLog controlLog);
}
