package com.hais.hais1000.service;

import com.hais.hais1000.dao.LoadControlFlowItemMapper;
import com.hais.hais1000.dao.LoadControlFlowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadControlFlowService {

    @Autowired
    LoadControlFlowMapper loadControlFlowMapper;

}
