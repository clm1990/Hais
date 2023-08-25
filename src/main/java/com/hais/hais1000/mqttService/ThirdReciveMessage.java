package com.hais.hais1000.mqttService;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.hais.hais1000.comm.mqtt.CloudMqttClient;
import com.hais.hais1000.dao.LoadControlFlowItemMapper;
import com.hais.hais1000.dao.LoadControlFlowMapper;
import com.hais.hais1000.dao.ProjectMapper;
import com.hais.hais1000.dto.ControlLog;
import com.hais.hais1000.dto.LoadControlFlow;
import com.hais.hais1000.dto.LoadControlFlowItem;
import com.hais.hais1000.dto.ProjectInfo;
import com.hais.hais1000.service.ControlLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ThirdReciveMessage {

    @Autowired
    ControlLogService controlLogService;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    LoadControlFlowMapper loadControlFlowMapper;

    @Autowired
    LoadControlFlowItemMapper loadControlFlowItemMapper;


    public void dataLogHandle(byte[] msg){

        String ctllog = new String(msg, StandardCharsets.UTF_8);
        log.info("Client 接收消息：" + ctllog);

        JSONObject jsonObject = JSON.parseObject(ctllog);

        //插入日志表
        String sn = jsonObject.getJSONObject("paras").getJSONArray("commands").getJSONObject(0).get("sn").toString();

        if(StringUtil.isEmpty(sn)) return;

        ProjectInfo projectInfo = projectMapper.getProjectInfoBySn(sn);

        Date date = new Date();
        ControlLog controlLog = new ControlLog();
        controlLog.setMacAddr(projectInfo.getMacAddr());
        controlLog.setEventTime(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        controlLog.setLog(ctllog);
        controlLogService.addControlLog(controlLog);


        //转发指令
        LoadControlFlowItem loadControlFlowItem = null;
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

        /*
        try {
            cloudMqttClient.publish("/v1/devices/command", msg);
        }catch (Exception e){
            e.printStackTrace();
        }
         */
    }
}
