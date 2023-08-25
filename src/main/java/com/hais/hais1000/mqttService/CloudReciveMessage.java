package com.hais.hais1000.mqttService;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import com.github.pagehelper.util.StringUtil;
import com.hais.hais1000.dao.*;
import com.hais.hais1000.dto.*;
import com.hais.hais1000.mqttService.dto.MqttDataLog;
import com.hais.hais1000.service.VirDevParamInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CloudReciveMessage {

    @Autowired
    DataLogMapper dataLogMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    VirDevInfoMapper virDevInfoMapper;

    @Autowired
    VirDevParamInfoMapper virDevParamInfoMapper;

    @Autowired
    VirDevParamHisInfoMapper virDevParamHisInfoMapper;

    @Autowired
    VirDevParamInfoService virDevParamInfoService;

    //@Autowired
    //ThridMqttClient thridMqttClient;
    private final double diff = 0.001f;


    //更行实时数据库
    private void updateRealDB(String projectID, MqttDataLog mqttDataLog){
        if(StringUtil.isEmpty(projectID)) return;

        List<VirDevParamHisInfo> virDevParamHisInfoList = new ArrayList<>();

        List<VirDevInfo> virDevInfoCHList = virDevInfoMapper.getVirDevInfo(projectID, "00010001");
        for(VirDevInfo virDevInfo : virDevInfoCHList){
            //List<VirDevParamInfo> virDevParamInfoList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfo.getVirDevID());
            List<VirDevParamInfo> virDevParamInfoList = virDevParamInfoService.getVirDevParamInfo(virDevInfo.getVirDevID());
            if(virDevParamInfoList.isEmpty()) continue;

            if(Math.abs(virDevParamInfoList.get(2).getParamValue() - mqttDataLog.getOnline()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 10, mqttDataLog.getChiWaterSupplyTemp());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 10, mqttDataLog.getChiWaterSupplyTemp());
                VirDevParamHisInfo virRunStateDevParamHisInfo = new VirDevParamHisInfo();
                virRunStateDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virRunStateDevParamHisInfo.setParamSeq(2);
                virRunStateDevParamHisInfo.setParamValue((double)mqttDataLog.getOnline());
                virRunStateDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virRunStateDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(10).getParamValue() - mqttDataLog.getChiWaterSupplyTemp()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 10, mqttDataLog.getChiWaterSupplyTemp());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 10, mqttDataLog.getChiWaterSupplyTemp());
                VirDevParamHisInfo virChiWaterDevParamHisInfo = new VirDevParamHisInfo();
                virChiWaterDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virChiWaterDevParamHisInfo.setParamSeq(10);
                virChiWaterDevParamHisInfo.setParamValue(mqttDataLog.getChiWaterSupplyTemp());
                virChiWaterDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virChiWaterDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(19).getParamValue() - mqttDataLog.getTempColdSet()) > diff){
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 19, mqttDataLog.getTempColdSet());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 19, mqttDataLog.getTempColdSet());
                VirDevParamHisInfo virTempColdDevParamHisInfo = new VirDevParamHisInfo();
                virTempColdDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virTempColdDevParamHisInfo.setParamSeq(19);
                virTempColdDevParamHisInfo.setParamValue(mqttDataLog.getTempColdSet());
                virTempColdDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virTempColdDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(20).getParamValue() - mqttDataLog.getTempHeatSet()) > diff){
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 20, mqttDataLog.getTempHeatSet());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 20, mqttDataLog.getTempHeatSet());
                VirDevParamHisInfo virTempHeatDevParamHisInfo = new VirDevParamHisInfo();
                virTempHeatDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virTempHeatDevParamHisInfo.setParamSeq(20);
                virTempHeatDevParamHisInfo.setParamValue(mqttDataLog.getTempHeatSet());
                virTempHeatDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virTempHeatDevParamHisInfo);
            }

        }

        List<VirDevInfo> virDevInfoELEList = virDevInfoMapper.getVirDevInfo(projectID, "00060001");
        for(VirDevInfo virDevInfo : virDevInfoELEList){
            //List<VirDevParamInfo> virDevParamInfoList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfo.getVirDevID());
            List<VirDevParamInfo> virDevParamInfoList = virDevParamInfoService.getVirDevParamInfo(virDevInfo.getVirDevID());
            if(virDevParamInfoList.isEmpty()) continue;

            if(Math.abs(virDevParamInfoList.get(7).getParamValue() - mqttDataLog.getIa()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 7, mqttDataLog.getIa());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 7, mqttDataLog.getIa());
                VirDevParamHisInfo virIaDevParamHisInfo = new VirDevParamHisInfo();
                virIaDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virIaDevParamHisInfo.setParamSeq(7);
                virIaDevParamHisInfo.setParamValue(mqttDataLog.getIa());
                virIaDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virIaDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(8).getParamValue() - mqttDataLog.getIb()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 8, mqttDataLog.getIb());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 8, mqttDataLog.getIb());
                VirDevParamHisInfo virIbDevParamHisInfo = new VirDevParamHisInfo();
                virIbDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virIbDevParamHisInfo.setParamSeq(8);
                virIbDevParamHisInfo.setParamValue(mqttDataLog.getIb());
                virIbDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virIbDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(9).getParamValue() - mqttDataLog.getIc()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 9, mqttDataLog.getIc());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 9, mqttDataLog.getIc());
                VirDevParamHisInfo virIcDevParamHisInfo = new VirDevParamHisInfo();
                virIcDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virIcDevParamHisInfo.setParamSeq(9);
                virIcDevParamHisInfo.setParamValue(mqttDataLog.getIc());
                virIcDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virIcDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(4).getParamValue() - mqttDataLog.getUa()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 4, mqttDataLog.getUa());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 4, mqttDataLog.getUa());
                VirDevParamHisInfo virUaDevParamHisInfo = new VirDevParamHisInfo();
                virUaDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virUaDevParamHisInfo.setParamSeq(4);
                virUaDevParamHisInfo.setParamValue(mqttDataLog.getUa());
                virUaDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virUaDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(5).getParamValue() - mqttDataLog.getUb()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 5, mqttDataLog.getUb());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 5, mqttDataLog.getUb());
                VirDevParamHisInfo virUbDevParamHisInfo = new VirDevParamHisInfo();
                virUbDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virUbDevParamHisInfo.setParamSeq(5);
                virUbDevParamHisInfo.setParamValue(mqttDataLog.getUb());
                virUbDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virUbDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(6).getParamValue() - mqttDataLog.getUc()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 6, mqttDataLog.getUc());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 6, mqttDataLog.getUc());
                VirDevParamHisInfo virUcDevParamHisInfo = new VirDevParamHisInfo();
                virUcDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virUcDevParamHisInfo.setParamSeq(6);
                virUcDevParamHisInfo.setParamValue(mqttDataLog.getUc());
                virUcDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virUcDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(12).getParamValue() - mqttDataLog.getEp()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 12, mqttDataLog.getEp());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 12, mqttDataLog.getEp());
                VirDevParamHisInfo virEpDevParamHisInfo = new VirDevParamHisInfo();
                virEpDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virEpDevParamHisInfo.setParamSeq(12);
                virEpDevParamHisInfo.setParamValue(mqttDataLog.getEp());
                virEpDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virEpDevParamHisInfo);
            }

            if(Math.abs(virDevParamInfoList.get(2).getParamValue() - mqttDataLog.getTotw()) > diff) {
                //virDevParamInfoMapper.updateVirDevParamValue(virDevInfo.getVirDevID(), 2, mqttDataLog.getTotw());
                //virDevParamInfoService.updateVirDevParamValue(virDevInfo.getVirDevID(), 2, mqttDataLog.getTotw());
                VirDevParamHisInfo virTotwDevParamHisInfo = new VirDevParamHisInfo();
                virTotwDevParamHisInfo.setVirDevID(virDevInfo.getVirDevID());
                virTotwDevParamHisInfo.setParamSeq(2);
                virTotwDevParamHisInfo.setParamValue(mqttDataLog.getTotw());
                virTotwDevParamHisInfo.setUpdateTime(mqttDataLog.getTime());
                virDevParamHisInfoList.add(virTotwDevParamHisInfo);
            }
        }

        if(!virDevParamHisInfoList.isEmpty()){
            //批量修改
            virDevParamInfoService.updateVirDevParamValueList(virDevParamHisInfoList);
            //批量插入
            insertHistoryDB(projectID, virDevParamHisInfoList);
        }
    }

    //入日志数据库
    private void insertLogDB(String macAddr, String data){

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

        dataLogMapper.addDataLog(new DataLog(macAddr, data.replace("\\", ""), date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
    }

    //入历史数据库
    private void insertHistoryDB(String projectID, List<VirDevParamHisInfo> virDevParamHisInfoList){

        if(virDevParamHisInfoMapper.checkTableExistsWithSchema(projectID) == 0){
            virDevParamHisInfoMapper.createHisTable(projectID);
        }

        virDevParamHisInfoMapper.addListVirDevHisInfo(projectID, virDevParamHisInfoList);
    }

    public void dataLogHandle(byte[] msg){
        try {
            String dataLog = new String(msg, StandardCharsets.UTF_8);
            byte[] bytes = dataLog.getBytes();
            JSONObject data = JSON.parseObject(bytes);

            //获取项目信息
            String macAddr = data.get("macAddr").toString().replace("-", ":").toUpperCase();
            Optional<ProjectInfo> projectInfo = projectMapper.getProjectInfo(null, macAddr, null).stream().findFirst();
            if (!projectInfo.isPresent()) return;

            //解析上报数据
            JSONObject bodydata = data.getJSONObject("data").getJSONArray("devices").getJSONObject(0).getJSONArray("services").getJSONObject(0).getJSONObject("data").getJSONArray("body").getJSONObject(0);
            MqttDataLog mqttDataLogDTO = new MqttDataLog();
            if(bodydata.get("pt") != null) mqttDataLogDTO.setPt(bodydata.getDouble("pt"));
            if(bodydata.get("ep") != null) mqttDataLogDTO.setEp(bodydata.getDouble("ep"));
            if(bodydata.get("totw") != null) mqttDataLogDTO.setTotw(bodydata.getDouble("totw"));
            if(bodydata.get("ua") != null) mqttDataLogDTO.setUa(bodydata.getDouble("ua"));
            if(bodydata.get("ub") != null) mqttDataLogDTO.setUb(bodydata.getDouble("ub"));
            if(bodydata.get("uc") != null) mqttDataLogDTO.setUc(bodydata.getDouble("uc"));
            if(bodydata.get("ia") != null) mqttDataLogDTO.setIa(bodydata.getDouble("ia"));
            if(bodydata.get("ib") != null) mqttDataLogDTO.setIb(bodydata.getDouble("ib"));
            if(bodydata.get("ic") != null) mqttDataLogDTO.setIc(bodydata.getDouble("ic"));
            if(bodydata.get("ChiWaterSupplyTemp") != null)  mqttDataLogDTO.setChiWaterSupplyTemp(bodydata.getDouble("ChiWaterSupplyTemp"));
            if(bodydata.get("point_faul") != null) mqttDataLogDTO.setPointFaul(bodydata.getDouble("point_faul"));
            if(bodydata.get("online") != null) mqttDataLogDTO.setOnline(bodydata.getInteger("online"));
            if(bodydata.get("temp_heat_set") != null) mqttDataLogDTO.setTempHeatSet(bodydata.getDouble("temp_heat_set"));
            if(bodydata.get("temp_cold_set") != null) mqttDataLogDTO.setTempColdSet(bodydata.getDouble("temp_cold_set"));
            if(bodydata.get("sn") != null) mqttDataLogDTO.setSn(bodydata.getString("sn"));
            if(bodydata.get("time") != null) mqttDataLogDTO.setTime(bodydata.getString("time"));

            //实时数据库
            updateRealDB(projectInfo.get().getProjectId(), mqttDataLogDTO);

            //日志数据库
            insertLogDB(macAddr, dataLog);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
