package com.hais.hais1000.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.ResponseData;
import com.hais.hais1000.dao.LoadControlFlowItemMapper;
import com.hais.hais1000.dao.LoadControlFlowMapper;
import com.hais.hais1000.dao.ProjectMapper;
import com.hais.hais1000.dto.*;
import com.hais.hais1000.service.ControlLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ControlLogController {

    @Autowired
    ControlLogService controlLogService;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    LoadControlFlowMapper loadControlFlowMapper;

    @Autowired
    LoadControlFlowItemMapper loadControlFlowItemMapper;

    @ResponseBody
    @GetMapping("/getControlLog")
    public ResponseData getLogByMacAddr(String macAddr, int pageSize, int currentPage){
        ListPageUtil<ControlLog> list = null;
        try {
            list = controlLogService.getLogByMacAddr(macAddr.replace("-", ":").toUpperCase(), pageSize, currentPage);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "查询失败", list);
        }

        return new ResponseData(true, "0", "查询成功", list);
    }

    @ResponseBody
    @GetMapping("/addControlLog")
    public ResponseData addControlLog(){
        String str = "{\"msgType\":\"cloudReq\",\"mid\":51036,\"cmd\":\"commands\",\"paras\":{\"commands\":[{\"start_time\":\"2023-08-0916:16:00\",\"end_time\":\"2023-08-0916:18:00\",\"sn\":\"ownwaysn088\",\"call_type\":\"2\",\"call_value\":\"10\",\"cid\":\"066f148ad6514cb58199da8611705fac\"}]},\"serviceId\":\"fhzz\",\"deviceId\":\"D663137757wEUAm\"}";

        Date date = new Date();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");



        ControlLog controlLog = new ControlLog();
        controlLog.setMacAddr("06:CF:7B:DB:9D:2B");
        controlLog.setEventTime(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        controlLog.setLog(str);
        controlLogService.addControlLog(controlLog);


        JSONObject jsonObject = JSON.parseObject(str);

        //转发指令
        List<LoadControlFlowItem> loadControlFlowItems = new ArrayList<>();
        LoadControlFlow loadControlFlow = new LoadControlFlow();
        String flowID = UUID.randomUUID().toString().replace("-", "").toUpperCase();


        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        loadControlFlow.setFlowID(flowID);
        loadControlFlow.setMsgType(jsonObject.getString("msgType"));
        loadControlFlow.setMid(jsonObject.getInteger("mid"));
        loadControlFlow.setCmd(jsonObject.getString("cmd"));
        loadControlFlow.setServiceID(jsonObject.getString("serviceId"));
        loadControlFlow.setDeviceID(jsonObject.getString("deviceId"));
        loadControlFlow.setEventTime(sdf.format(date));

        JSONObject paras = jsonObject.getJSONObject("paras");
        JSONArray commands = paras.getJSONArray("commands");
        int num = commands.size();
        for(int i=0; i<num; i++){
            JSONObject command = commands.getJSONObject(i);
            System.out.println(flowID);
            LoadControlFlowItem loadControlFlowItem = new LoadControlFlowItem();
            loadControlFlowItem.setFlowID(flowID);
            loadControlFlowItem.setFlowState(0);
            loadControlFlowItem.setStartTime(command.getString("start_time"));
            loadControlFlowItem.setEndTime(command.getString("end_time"));
            loadControlFlowItem.setCallType(command.getString("call_type"));
            loadControlFlowItem.setCallValue(command.getDouble("call_value"));
            loadControlFlowItem.setSn(command.getString("sn"));
            loadControlFlowItem.setCid(command.getString("cid"));
            loadControlFlowItem.setEventTime(sdf.format(date));
            loadControlFlowItems.add(loadControlFlowItem);
        }

        loadControlFlowMapper.addLoadControlFlow(loadControlFlow);
        if(!loadControlFlowItems.isEmpty()) {
            loadControlFlowItemMapper.addLoadControlFlowItem(loadControlFlowItems);
        }

        return new ResponseData(true, "0", "查询成功", "");
    }
}
