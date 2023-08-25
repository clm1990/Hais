package com.hais.hais1000.service;

import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.PageBean;
import com.hais.hais1000.dao.ControlLogMapper;
import com.hais.hais1000.dto.ControlLog;
import com.hais.hais1000.dto.DataLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ControlLogService {

    @Autowired
    ControlLogMapper controlLogMapper;

    public ListPageUtil<ControlLog> getLogByMacAddr(String macAddr, int pageSize, int currentPage){

        PageBean<ControlLog> pageUtils = new PageBean<>();
        List<ControlLog> list = pageUtils.pageInfo(controlLogMapper.getLogByMacAddr(macAddr), currentPage, pageSize);

        return new ListPageUtil<>(list, pageUtils.getRowsCount(), pageUtils.getPageSize(), pageUtils.getCurPage());
    }

    public void addControlLog(ControlLog dataLog){
        controlLogMapper.addControlLog(dataLog);
    }
}
