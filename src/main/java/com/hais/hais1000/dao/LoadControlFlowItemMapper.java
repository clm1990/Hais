package com.hais.hais1000.dao;

import com.hais.hais1000.dto.LoadControlFlowItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoadControlFlowItemMapper {
    public List<LoadControlFlowItem> getLoadControlFlowItemBySatrtTime(@Param("time") String time);

    public List<LoadControlFlowItem> getLoadControlFlowItemByEndTime(@Param("time") String time);

    public void updateStateLoadControlFlowItem(@Param("flowState") Integer flowState, @Param("flowID") String flowID, @Param("cid") String cid);

    public void updateStartValueLoadControlFlowItem(@Param("startValue") Double startValue, @Param("flowID") String flowID, @Param("cid") String cid);

    public void addLoadControlFlowItem(@Param("loadControlFlowItems") List<LoadControlFlowItem> loadControlFlowItems);
}
