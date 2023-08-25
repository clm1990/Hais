package com.hais.hais1000.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.PageBean;
import com.hais.hais1000.dao.*;
import com.hais.hais1000.dto.*;
import com.hais.hais1000.utils.HttpTools;
import com.hais.hais1000.utils.httpStruct.ProjectIsOnlineStruct;
import com.hais.hais1000.utils.httpStruct.ProjectResponStruct;
import org.apache.tomcat.util.buf.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProjectService {

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    VirDevTypeInfoMapper virDevTypeInfoMapper;

    @Autowired
    VirDevTypeParamInfoMapper virDevTypeParamInfoMapper;

    @Autowired
    VirDevInfoMapper virDevInfoMapper;

    @Autowired
    VirDevParamInfoMapper virDevParamInfoMapper;

    @Autowired
    VirDevParamInfoService virDevParamInfoService;

    public ProjectIsOnlineStruct getProjectOnLineInfo(){

        JSONObject jSONObject = new JSONObject();
        List<ProjectInfo> listProject = projectMapper.getAllProjectInfo();
        String respondStr = new String();

        try {
            respondStr = HttpTools.doGet("http://123.60.90.179:18082/info", null);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!respondStr.isEmpty()){
            jSONObject = JSONObject.parseObject(respondStr);
        }

        Integer onLineNum = 0;
        for(ProjectInfo p : listProject){
            if(!jSONObject.isEmpty() && jSONObject.get("content").toString().replace("-", ":").toUpperCase().contains(p.getMacAddr())){
                onLineNum++;
            }
        }

        ProjectIsOnlineStruct projectIsOnlineStruct = new ProjectIsOnlineStruct();
        projectIsOnlineStruct.setTotalProjectNum(listProject.size());
        projectIsOnlineStruct.setOnLineProjectNum(onLineNum);
        projectIsOnlineStruct.setLeaveLineProjectNum(listProject.size() - onLineNum);
        if(listProject.size() != 0){
            projectIsOnlineStruct.setOnLineProjectPercent(Double.parseDouble( String.format("%.1f", (double)onLineNum/listProject.size()*100)));
        }

        return projectIsOnlineStruct;
    }

    public ListPageUtil<ProjectResponStruct> getAllProjectInfo(int pageSize, int currentPage){

        PageBean<ProjectResponStruct> pageUtils = new PageBean<>();
        JSONObject jSONObject = new JSONObject();
        List<ProjectInfo> listProject = projectMapper.getAllProjectInfo();
        String respondStr = new String();

        try {
            respondStr = HttpTools.doGet("http://123.60.90.179:18082/info", null);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!respondStr.isEmpty()){
            jSONObject = JSONObject.parseObject(respondStr);
        }

        List<ProjectResponStruct> listProjectRespon = new ArrayList<ProjectResponStruct>();
        for(ProjectInfo p : listProject){
            ProjectResponStruct projectResponStruct = new ProjectResponStruct();
            projectResponStruct.setProjectId(p.getProjectId());
            projectResponStruct.setProjectName(p.getProjectName());
            projectResponStruct.setProjectAddress(p.getProjectAddress());
            projectResponStruct.setProjectDescribe(p.getProjectDescribe());
            projectResponStruct.setProjectStartTime(p.getStartTime());
            projectResponStruct.setMacAddr(p.getMacAddr());
            projectResponStruct.setProjectSn(p.getProjectSn());


            List<VirDevInfo> virDevInfoCHList = virDevInfoMapper.getVirDevInfo(p.getProjectId(), "00010001");
            List<VirDevParamInfo> virDevParamInfoCHList = new ArrayList<>();

            if(!virDevInfoCHList.isEmpty()){
                //virDevParamInfoCHList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfoCHList.get(0).getVirDevID());
                virDevParamInfoCHList = virDevParamInfoService.getVirDevParamInfo(virDevInfoCHList.get(0).getVirDevID());
            }

            if(!virDevParamInfoCHList.isEmpty()){
                projectResponStruct.setChMode(virDevParamInfoCHList.get(3).getParamValue().intValue());
                projectResponStruct.setChRunState(virDevParamInfoCHList.get(2).getParamValue().intValue());
                projectResponStruct.setChiWaterSupplyTemp(virDevParamInfoCHList.get(10).getParamValue());
                projectResponStruct.setChTempColdSet(virDevParamInfoCHList.get(19).getParamValue());
                projectResponStruct.setChTempHeatSet(virDevParamInfoCHList.get(20).getParamValue());
            }

            if(!jSONObject.isEmpty() && jSONObject.get("content").toString().replace("-", ":").toUpperCase().contains(p.getMacAddr())){
                projectResponStruct.setProjectState(1);
            }else{
                projectResponStruct.setProjectState(0);
            }


            listProjectRespon.add(projectResponStruct);
        }

        List<ProjectResponStruct> list = pageUtils.pageInfo(listProjectRespon, currentPage, pageSize);
        return new ListPageUtil<>(list, pageUtils.getRowsCount(), pageUtils.getPageSize(), pageUtils.getCurPage());

    }

    public ListPageUtil<ProjectResponStruct> getProjectInfo(int pageSize, int currentPage, String projectName, String macAddr, String projectAddress){

        PageBean<ProjectResponStruct> pageUtils = new PageBean<>();
        JSONObject jSONObject = new JSONObject();
        List<ProjectInfo> listProject = projectMapper.getProjectInfo(projectName, macAddr, projectAddress);
        String respondStr = new String();

        try {
            respondStr = HttpTools.doGet("http://123.60.90.179:18082/info", null);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!respondStr.isEmpty()){
            jSONObject = JSONObject.parseObject(respondStr);
        }

        List<ProjectResponStruct> listProjectRespon = new ArrayList<ProjectResponStruct>();
        for(ProjectInfo p : listProject){
            ProjectResponStruct projectResponStruct = new ProjectResponStruct();
            projectResponStruct.setProjectId(p.getProjectId());
            projectResponStruct.setProjectName(p.getProjectName());
            projectResponStruct.setProjectAddress(p.getProjectAddress());
            projectResponStruct.setProjectDescribe(p.getProjectDescribe());
            projectResponStruct.setProjectStartTime(p.getStartTime());
            projectResponStruct.setMacAddr(p.getMacAddr());
            projectResponStruct.setProjectSn(p.getProjectSn());

            List<VirDevInfo> virDevInfoCHList = virDevInfoMapper.getVirDevInfo(p.getProjectId(), "00010001");
            List<VirDevParamInfo> virDevParamInfoCHList = new ArrayList<>();

            if(!virDevInfoCHList.isEmpty()){
                //virDevParamInfoCHList = virDevParamInfoMapper.getVirDevParamInfo(virDevInfoCHList.get(0).getVirDevID());
                virDevParamInfoCHList = virDevParamInfoService.getVirDevParamInfo(virDevInfoCHList.get(0).getVirDevID());
            }

            if(!virDevParamInfoCHList.isEmpty()){
                projectResponStruct.setChMode(virDevParamInfoCHList.get(3).getParamValue().intValue());
                projectResponStruct.setChRunState(virDevParamInfoCHList.get(2).getParamValue().intValue());
                projectResponStruct.setChiWaterSupplyTemp(virDevParamInfoCHList.get(10).getParamValue());
                projectResponStruct.setChTempColdSet(virDevParamInfoCHList.get(19).getParamValue());
                projectResponStruct.setChTempHeatSet(virDevParamInfoCHList.get(20).getParamValue());
            }


            if(!jSONObject.isEmpty() && jSONObject.get("content").toString().replace("-", ":").toUpperCase().contains(p.getMacAddr())){
                projectResponStruct.setProjectState(1);
            }else{
                projectResponStruct.setProjectState(0);
            }


            listProjectRespon.add(projectResponStruct);
        }

        List<ProjectResponStruct> list = pageUtils.pageInfo(listProjectRespon, currentPage, pageSize);
        return new ListPageUtil<>(list, pageUtils.getRowsCount(), pageUtils.getPageSize(), pageUtils.getCurPage());

    }

    public void addProjectInfo(ProjectInfo projectInfo) throws Exception{
        projectInfo.setProjectId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        projectInfo.setMacAddr(projectInfo.getMacAddr().replace("-", ":").toUpperCase());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateStr = sdf.format(currentDate);
        projectInfo.setStartTime(currentDateStr);
        projectMapper.addProjectInfo(projectInfo);

        //添加资产（主机、电表）
        List<VirDevTypeInfo> virDevTypeInfoList = virDevTypeInfoMapper.getVirDevTypeInfo();
        Optional<VirDevTypeInfo> virDevTypeInfoCH = virDevTypeInfoList.stream().filter(e -> e.getVirDevTypeID().equals("00010001")).findFirst();
        if(!virDevTypeInfoCH.isPresent()){
            throw new Exception("没有主机资产类型");
        }

        Optional<VirDevTypeInfo> virDevTypeInfoELE = virDevTypeInfoList.stream().filter(e -> e.getVirDevTypeID().equals("00060001")).findFirst();
        if(!virDevTypeInfoELE.isPresent()){
            throw new Exception("没有电表资产类型");
        }

        List<VirDevTypeParamInfo> virDevTypeParamInfoCHList = virDevTypeParamInfoMapper.getVirDevTypeParamInfo(virDevTypeInfoCH.get().getVirDevTypeID());
        if(virDevTypeParamInfoCHList.isEmpty()){
            throw new Exception("没有主机资产类型参数");
        }

        List<VirDevTypeParamInfo> virDevTypeParamInfoELEList = virDevTypeParamInfoMapper.getVirDevTypeParamInfo(virDevTypeInfoELE.get().getVirDevTypeID());
        if(virDevTypeParamInfoELEList.isEmpty()){
            throw new Exception("没有电表资产类型参数");
        }

        //添加主机及主机参数
        VirDevInfo virDevInfoCh = new VirDevInfo();
        virDevInfoCh.setProjectID(projectInfo.getProjectId());
        virDevInfoCh.setVirDevTypeID(virDevTypeInfoCH.get().getVirDevTypeID());
        virDevInfoCh.setVirDevTypeName(virDevTypeInfoCH.get().getVirDevTypeName());
        virDevInfoCh.setVirDevID(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        virDevInfoCh.setVirDevName("空调主机");
        virDevInfoCh.setLogicalNodeID("");
        virDevInfoCh.setDescription("");
        virDevInfoCh.setVirDevState(0);
        virDevInfoCh.setPeculiarity(0);
        virDevInfoCh.setReserved1("");
        virDevInfoCh.setReserved2("");
        virDevInfoCh.setReserved3(0);

        List<VirDevParamInfo> virDevParamInfoCHList = new ArrayList<>();
        for(VirDevTypeParamInfo virDevTypeParamInfo : virDevTypeParamInfoCHList){
            VirDevParamInfo virDevParamInfo = new VirDevParamInfo();
            virDevParamInfo.setVirDevID(virDevInfoCh.getVirDevID());
            virDevParamInfo.setParamSeq(virDevTypeParamInfo.getParamSeq());
            virDevParamInfo.setParamName(virDevTypeParamInfo.getParamName());
            virDevParamInfo.setParamType(virDevTypeParamInfo.getParamType());
            virDevParamInfo.setParamUIType(virDevTypeParamInfo.getParamUIType());
            virDevParamInfo.setParamSign(virDevTypeParamInfo.getParamSign());
            virDevParamInfo.setParamValue(0.0);
            virDevParamInfo.setParamLevel(0);
            virDevParamInfo.setReserved1("");
            virDevParamInfo.setReserved2("");
            virDevParamInfo.setReserved3(0);
            virDevParamInfoCHList.add(virDevParamInfo);
        }

        virDevInfoMapper.addVirDevInfo(virDevInfoCh);
        //virDevParamInfoMapper.addVirDevParamInfoList(virDevParamInfoCHList);
        virDevParamInfoService.addVirDevParamInfoList(virDevParamInfoCHList);

        //添加电表及电表参数
        VirDevInfo virDevInfoELE = new VirDevInfo();
        virDevInfoELE.setProjectID(projectInfo.getProjectId());
        virDevInfoELE.setVirDevTypeID(virDevTypeInfoELE.get().getVirDevTypeID());
        virDevInfoELE.setVirDevTypeName(virDevTypeInfoELE.get().getVirDevTypeName());
        virDevInfoELE.setVirDevID(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        virDevInfoELE.setVirDevName("电表");
        virDevInfoELE.setLogicalNodeID("");
        virDevInfoELE.setDescription("");
        virDevInfoELE.setVirDevState(0);
        virDevInfoELE.setPeculiarity(0);
        virDevInfoELE.setReserved1("");
        virDevInfoELE.setReserved2("");
        virDevInfoELE.setReserved3(0);

        List<VirDevParamInfo> virDevParamInfoELEList = new ArrayList<>();
        for(VirDevTypeParamInfo virDevTypeParamInfo : virDevTypeParamInfoELEList){
            VirDevParamInfo virDevParamInfo = new VirDevParamInfo();
            virDevParamInfo.setVirDevID(virDevInfoELE.getVirDevID());
            virDevParamInfo.setParamSeq(virDevTypeParamInfo.getParamSeq());
            virDevParamInfo.setParamName(virDevTypeParamInfo.getParamName());
            virDevParamInfo.setParamType(virDevTypeParamInfo.getParamType());
            virDevParamInfo.setParamUIType(virDevTypeParamInfo.getParamUIType());
            virDevParamInfo.setParamSign(virDevTypeParamInfo.getParamSign());
            virDevParamInfo.setParamValue(0.0);
            virDevParamInfo.setParamLevel(0);
            virDevParamInfo.setReserved1("");
            virDevParamInfo.setReserved2("");
            virDevParamInfo.setReserved3(0);
            virDevParamInfoELEList.add(virDevParamInfo);
        }

        virDevInfoMapper.addVirDevInfo(virDevInfoELE);
        //virDevParamInfoMapper.addVirDevParamInfoList(virDevParamInfoELEList);
        virDevParamInfoService.addVirDevParamInfoList(virDevParamInfoELEList);

    }

    public void delProjectInfo(String projectId){
        projectMapper.delProjectInfo(projectId);

        //删除资产
        List<VirDevInfo> virDevInfoList = virDevInfoMapper.getVirDevInfo(projectId, null);

        if(!virDevInfoList.isEmpty()){
            virDevInfoMapper.delVirDevInfo(projectId);
            for(VirDevInfo virDevInfo : virDevInfoList){
                //virDevParamInfoMapper.delVirDevParamInfo(virDevInfo.getVirDevID());
                virDevParamInfoService.delVirDevParamInfo(virDevInfo.getVirDevID());
            }
        }

    }

    public void updateProjectInfo(ProjectInfo projectInfo){
        projectMapper.updateProjectInfo(projectInfo);
    }
}
