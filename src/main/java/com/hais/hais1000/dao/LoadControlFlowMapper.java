package com.hais.hais1000.dao;

import com.hais.hais1000.dto.LoadControlFlow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoadControlFlowMapper {
    public LoadControlFlow getLoadControlFlowByID(String flowID);

    public void addLoadControlFlow(LoadControlFlow loadControlFlow);
}
