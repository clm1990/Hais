package com.hais.hais1000.controller;

import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.ResponseData;
import com.hais.hais1000.dto.VirDevParamHisInfo;
import com.hais.hais1000.dto.VirDevParamInfo;
import com.hais.hais1000.service.VirDevInfoService;
import com.hais.hais1000.service.VirDevParamInfoService;
import com.hais.hais1000.utils.httpStruct.ProjectResponStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VirDevInfoController {

    @Autowired
    VirDevInfoService virDevInfoService;

    @Autowired
    VirDevParamInfoService virDevParamInfoService;

    @ResponseBody
    @GetMapping("/getVirDevInfo")
    public ResponseData getVirDevInfo(String projectID){

        if(StringUtil.isEmpty(projectID)){
            return new ResponseData(true, "-1", "参数错误",  null);
        }

        Map<String, List<VirDevParamInfo>> mapVirDevInfo = new HashMap<>();

        try {
            mapVirDevInfo = virDevInfoService.getVirDevRealInfo(projectID);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "查询失败",  null);
        }

        return new ResponseData(true, "0", "查询成功",  mapVirDevInfo);
    }

    @ResponseBody
    @GetMapping("/getVirDevParamHisInfo")
    public ResponseData getVirDevParamHisInfo(String projectID, String virDevID, String virParamSeq, String startTime, String endTime, String diffTime){
        if(StringUtil.isEmpty(projectID) || StringUtil.isEmpty(virDevID) || StringUtil.isEmpty(virParamSeq)  || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(endTime) ||  StringUtil.isEmpty(diffTime) ){
            return new ResponseData(true, "-1", "参数错误",  null);
        }

        List<VirDevParamHisInfo> virDevParamHisInfoList = new ArrayList<>();
        try {
            virDevParamHisInfoList = virDevInfoService.getVirDevHisInfo(projectID, virDevID, Integer.parseInt(virParamSeq), startTime, endTime, Integer.parseInt(diffTime));
        }catch (Exception exception){
            exception.printStackTrace();
            return new ResponseData(true, "-1", "暂无数据",  null);
        }

        return new ResponseData(true, "0", "查询成功",  virDevParamHisInfoList);
    }


    @ResponseBody
    @GetMapping("ctrlVirDevParamValue")
    public ResponseData ctrlVirDevParamValue(String projectID, String virDevId, Integer virParamSeq, double virParamValue){
        if(StringUtil.isEmpty(virDevId)){
            return new ResponseData(true, "-1", "参数错误",  null);
        }

        try {
            virDevInfoService.ctrlVirDevParamValue(projectID, virDevId, virParamSeq, virParamValue);
        }catch (Exception exception){
            return new ResponseData(true, "-1", "控制失败",  null);
        }

        return new ResponseData(true, "0", "控制成功",  null);
    }


    //@ResponseBody
    //@GetMapping("/getVirDevParamInfoBySeq")
    //public ResponseData getVirDevParamInfoBySeq(String virDevID, Integer virParamSeq){
    //    VirDevParamInfo virDevParamInfo = virDevParamInfoService.getVirDevParamInfoBySeq(virDevID, virParamSeq);

    //    return new ResponseData(true, "0", "查询成功",  virDevParamInfo);
    //}
}
