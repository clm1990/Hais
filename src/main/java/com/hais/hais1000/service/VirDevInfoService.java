package com.hais.hais1000.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hais.hais1000.comm.mqtt.CloudMqttClient;
import com.hais.hais1000.dao.ProjectMapper;
import com.hais.hais1000.dao.VirDevInfoMapper;
import com.hais.hais1000.dao.VirDevParamHisInfoMapper;
import com.hais.hais1000.dao.VirDevParamInfoMapper;
import com.hais.hais1000.dto.*;
import com.hais.hais1000.utils.TimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.OpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VirDevInfoService {

    @Autowired
    VirDevInfoMapper virDevInfoMapper;

    @Autowired
    VirDevParamInfoMapper virDevParamInfoMapper;

    @Autowired
    VirDevParamHisInfoMapper virDevParamHisInfoMapper;

    @Autowired
    VirDevParamInfoService virDevParamInfoService;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    CloudMqttClient cloudMqttClient;

    public Map<String, List<VirDevParamInfo>> getVirDevRealInfo(String projectID) throws Exception{
        List<VirDevInfo> virDevInfoCHList = virDevInfoMapper.getVirDevInfo(projectID, "00010001");
        List<VirDevParamInfo> virDevParamInfoCHList = new ArrayList<>();

        if(!virDevInfoCHList.isEmpty()){
            //virDevParamInfoCHList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfoCHList.get(0).getVirDevID());
            virDevParamInfoCHList = virDevParamInfoService.getVirDevParamInfo(virDevInfoCHList.get(0).getVirDevID());
        }

        List<VirDevInfo> virDevInfoELEList = virDevInfoMapper.getVirDevInfo(projectID, "00060001");
        List<VirDevParamInfo> virDevParamInfoELEList = new ArrayList<>();

        if(!virDevInfoELEList.isEmpty()){
            //virDevParamInfoELEList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfoELEList.get(0).getVirDevID());
            virDevParamInfoELEList = virDevParamInfoService.getVirDevParamInfo(virDevInfoELEList.get(0).getVirDevID());
        }

        Map<String, List<VirDevParamInfo>> projectVirMap = new HashMap<>();

        projectVirMap.put("CH", virDevParamInfoCHList);
        projectVirMap.put("ELE", virDevParamInfoELEList);

        return projectVirMap;
    }

    public List<VirDevParamHisInfo> getVirDevHisInfo(String projectID, String virDevID, Integer virParamSeq, String startTime, String endTime, Integer diffTime) throws Exception{
        List<VirDevParamHisInfo> virDevParamHisInfoListResult = new ArrayList<>();

        VirDevParamHisInfo virDevParamHisInfoFirst = virDevParamHisInfoMapper.getVirDevHisInfoLimit(projectID, virDevID, virParamSeq,startTime);
        if(virDevParamHisInfoFirst == null){
            VirDevParamHisInfo virDevParamHisInfoFIrstNull = new VirDevParamHisInfo();
            virDevParamHisInfoFIrstNull.setVirDevID(virDevID);
            virDevParamHisInfoFIrstNull.setParamSeq(virParamSeq);
            virDevParamHisInfoFIrstNull.setParamValue(0.0);
            virDevParamHisInfoFirst = virDevParamHisInfoFIrstNull;
        }
        virDevParamHisInfoFirst.setUpdateTime(startTime);
        virDevParamHisInfoListResult.add(virDevParamHisInfoFirst);

        List<VirDevParamHisInfo> virDevParamHisInfoList = virDevParamHisInfoMapper.getVirDevHisInfo(projectID, virDevID, virParamSeq, startTime, endTime);

        long startTimeStamp = TimeFormat.dateToStamp(startTime)/1000;
        long endTimeStamp = TimeFormat.dateToStamp(endTime)/1000;
        long cycleTimeStamp = startTimeStamp;

        Integer num = (int)Math.ceil((endTimeStamp - startTimeStamp)/(diffTime*60));

        for(int i = 0; i < num; i++){

            cycleTimeStamp += diffTime*60;
            String cycleTimeStr = TimeFormat.stampToDate(cycleTimeStamp*1000);

            List<VirDevParamHisInfo> virParamList = virDevParamHisInfoList.stream().filter(e->(e.getUpdateTime().compareTo(startTime) > 0  && e.getUpdateTime().compareTo(cycleTimeStr) <= 0)).collect(Collectors.toList());

            VirDevParamHisInfo virParam = new VirDevParamHisInfo();

            if(virParamList.isEmpty()){
                virParam.setVirDevID(virDevID);
                virParam.setParamSeq(virParamSeq);
                virParam.setParamValue(virDevParamHisInfoFirst.getParamValue());
                virParam.setUpdateTime(cycleTimeStr);
            }else{
                virParam.setVirDevID(virDevID);
                virParam.setParamSeq(virParamSeq);
                virParam.setParamValue(virParamList.get(virParamList.size()-1).getParamValue());
                virParam.setUpdateTime(cycleTimeStr);
            }

            virDevParamHisInfoListResult.add(virParam);
        }

        return virDevParamHisInfoListResult;
    }

    public void ctrlVirDevParamValue(String projectID, String virDevId, Integer virParamSeq, Double virParamValue)throws Exception{

        ProjectInfo projectInfo = projectMapper.getProjectInfoByID(projectID);
        if(projectInfo == null){
            throw new Exception("找不到对应的项目信息");
        }

        List<VirDevParamInfo> listVirDevParamInfo = virDevParamInfoMapper.getVirDevParamInfo(virDevId);
        if(listVirDevParamInfo.size() < virParamSeq){
            throw new Exception("找不到对应的资产参数");
        }

        if(virParamSeq == 28){
            Random r = new Random();//（方法一）先生成一个对象 r，
            int num = r.nextInt(100000);

            String uuid = UUID.randomUUID().toString().replace("-", "");

            Date date = new Date();
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgType", "cloudReq");
            jsonObject.put("mid", num);
            jsonObject.put("cmd", "commands");
            jsonObject.put("serviceId", "AAA");
            jsonObject.put("deviceId", "AAA");

            JSONObject jsonObjectParas = new JSONObject();
            JSONObject jsonObjectCommand = new JSONObject();
            jsonObjectCommand.put("start_time", sdf.format(date));
            jsonObjectCommand.put("end_time", sdf.format(date));
            jsonObjectCommand.put("sn", projectInfo.getProjectSn());
            jsonObjectCommand.put("call_type", "5");
            jsonObjectCommand.put("call_value", virParamValue.toString());
            jsonObjectCommand.put("cid", uuid);

            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append("[");
            cmdBuilder.append(JSON.toJSONString(jsonObjectCommand));
            cmdBuilder.append("]");

            jsonObjectParas.put("commands", cmdBuilder.toString());
            jsonObject.put("paras", jsonObjectParas);

            System.out.println(jsonObject);
            try {
                cloudMqttClient.publish("/v1/devices/command", JSON.toJSONBytes(jsonObject));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(virParamSeq == 29){

        }else {
            throw new Exception("资产参数不可控");
        }
    }
}
