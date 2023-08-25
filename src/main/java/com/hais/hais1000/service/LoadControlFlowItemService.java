package com.hais.hais1000.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.hais.hais1000.comm.mqtt.CloudMqttClient;
import com.hais.hais1000.dao.*;
import com.hais.hais1000.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoadControlFlowItemService {

    @Autowired
    LoadControlFlowItemMapper loadControlFlowItemMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    VirDevInfoMapper virDevInfoMapper;

    @Autowired
    VirDevParamInfoMapper virDevParamInfoMapper;

    @Autowired
    LoadControlFlowMapper loadControlFlowMapper;

    @Autowired
    CloudMqttClient cloudMqttClient;

    public void LoadControlStartTaskRun(){
        Date date = new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        List<LoadControlFlowItem> loadControlFlowItems = loadControlFlowItemMapper.getLoadControlFlowItemBySatrtTime(sdf.format(date));

        if(loadControlFlowItems.isEmpty()) return;

        for (LoadControlFlowItem loadControlFlowItem : loadControlFlowItems){

            JSONObject jsonObject = new JSONObject();
            Double callValue = 0.0;
            jsonObject.put("msgType", "cloudReq");
            jsonObject.put("mid", 11111);
            jsonObject.put("cmd", "commands");
            LoadControlFlow loadControlFlow = loadControlFlowMapper.getLoadControlFlowByID(loadControlFlowItem.getFlowID());
            if(loadControlFlow == null){
                loadControlFlowItemMapper.updateStateLoadControlFlowItem(99, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
                return;
            }
            jsonObject.put("serviceId", loadControlFlow.getServiceID());
            jsonObject.put("deviceId", loadControlFlow.getDeviceID());

            JSONObject jsonObjectParas = new JSONObject();
            JSONObject jsonObjectCommand = new JSONObject();
            jsonObjectCommand.put("start_time", loadControlFlowItem.getStartTime());
            jsonObjectCommand.put("end_time", loadControlFlowItem.getEndTime());
            jsonObjectCommand.put("sn", loadControlFlowItem.getSn());
            jsonObjectCommand.put("call_type", "5");

            ProjectInfo projectInfo = projectMapper.getProjectInfoBySn(loadControlFlowItem.getSn());
            if(projectInfo == null){
                loadControlFlowItemMapper.updateStateLoadControlFlowItem(99, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
                return;
            }

            List<VirDevInfo> virDevInfoList = virDevInfoMapper.getVirDevInfo(projectInfo.getProjectId(), "00010001");
            if(virDevInfoList.isEmpty()){
                loadControlFlowItemMapper.updateStateLoadControlFlowItem(99, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
                return;
            }

            List<VirDevParamInfo> virDevParamInfoList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfoList.get(0).getVirDevID());
            if(virDevParamInfoList.isEmpty()){
                loadControlFlowItemMapper.updateStateLoadControlFlowItem(99, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
                return;
            }

            callValue =  virDevParamInfoList.get(19).getParamValue();
            if(loadControlFlowItem.getCallType().equals("1")){
                callValue -= Math.ceil(loadControlFlowItem.getCallValue()/20) ;
            }else if(loadControlFlowItem.getCallType().equals("2")){
                callValue += Math.ceil(loadControlFlowItem.getCallValue()/20) ;
            }else if(loadControlFlowItem.getCallType().equals("5")){
                callValue = loadControlFlowItem.getCallValue();
            }else {
                return;
            }
            jsonObjectCommand.put("call_value", callValue.toString());
            jsonObjectCommand.put("cid", loadControlFlowItem.getCid());

            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append("[");
            cmdBuilder.append(JSON.toJSONString(jsonObjectCommand));
            cmdBuilder.append("]");

            jsonObjectParas.put("commands", cmdBuilder.toString());
            jsonObject.put("paras", jsonObjectParas);

            System.out.println(jsonObject);
            /*
            try {
                cloudMqttClient.publish("/v1/devices/command", JSON.toJSONBytes(jsonObject));
            }catch (Exception e){
                e.printStackTrace();
            }

             */

            loadControlFlowItemMapper.updateStartValueLoadControlFlowItem(virDevParamInfoList.get(19).getParamValue(), loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
            loadControlFlowItemMapper.updateStateLoadControlFlowItem(1, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
        }
    }

    public void LoadControlEndTaskRun(){
        Date date = new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        List<LoadControlFlowItem> loadControlFlowItems = loadControlFlowItemMapper.getLoadControlFlowItemByEndTime(sdf.format(date));

        if(loadControlFlowItems.isEmpty()) return;

        for (LoadControlFlowItem loadControlFlowItem : loadControlFlowItems){

            JSONObject jsonObject = new JSONObject();
            Double callValue = 0.0;
            LoadControlFlow loadControlFlow = loadControlFlowMapper.getLoadControlFlowByID(loadControlFlowItem.getFlowID());
            if(loadControlFlow == null){
                loadControlFlowItemMapper.updateStateLoadControlFlowItem(99, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
                return;
            }

            jsonObject.put("serviceId", loadControlFlow.getServiceID());
            jsonObject.put("deviceId", loadControlFlow.getDeviceID());
            jsonObject.put("msgType", loadControlFlow.getMsgType());
            jsonObject.put("mid", loadControlFlow.getMid());
            jsonObject.put("cmd", loadControlFlow.getCmd());

            JSONObject jsonObjectParas = new JSONObject();
            JSONObject jsonObjectCommand = new JSONObject();
            jsonObjectCommand.put("start_time", loadControlFlowItem.getStartTime());
            jsonObjectCommand.put("end_time", loadControlFlowItem.getEndTime());
            jsonObjectCommand.put("sn", loadControlFlowItem.getSn());
            jsonObjectCommand.put("call_type", "5");
            jsonObjectCommand.put("call_value", loadControlFlowItem.getStartValue());
            jsonObjectCommand.put("cid", loadControlFlowItem.getCid());

            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append("[");
            cmdBuilder.append(JSON.toJSONString(jsonObjectCommand));
            cmdBuilder.append("]");

            jsonObjectParas.put("commands", cmdBuilder.toString());
            jsonObject.put("paras", jsonObjectParas);

            System.out.println(jsonObject);
            /*
            try {
                cloudMqttClient.publish("/v1/devices/command", JSON.toJSONBytes(jsonObject));
            }catch (Exception e){
                e.printStackTrace();
            }

             */

            loadControlFlowItemMapper.updateStateLoadControlFlowItem(2, loadControlFlowItem.getFlowID(), loadControlFlowItem.getCid());
        }
    }
}
