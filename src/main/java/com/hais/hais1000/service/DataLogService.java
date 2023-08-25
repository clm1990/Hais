package com.hais.hais1000.service;

import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.PageBean;
import com.hais.hais1000.dao.DataLogMapper;
import com.hais.hais1000.dto.DataLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLogService {

    @Autowired
    DataLogMapper dataLogMapper;

    public ListPageUtil<DataLog> getLogByMacAddr(String macAddr, int pageSize, int currentPage){

        PageBean<DataLog> pageUtils = new PageBean<>();
        List<DataLog> list = pageUtils.pageInfo(dataLogMapper.getLogByMacAddr(macAddr), currentPage, pageSize);

        return new ListPageUtil<>(list, pageUtils.getRowsCount(), pageUtils.getPageSize(), pageUtils.getCurPage());
    }

    public void addDataLog(DataLog dataLog){
        dataLogMapper.addDataLog(dataLog);
    }
}
