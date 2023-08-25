package com.hais.hais1000.controller;

//import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.ResponseData;
import com.hais.hais1000.dto.DataLog;
import com.hais.hais1000.service.DataLogService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DataLogController {

    @Autowired
    DataLogService dataLogService;

    @ResponseBody
    @GetMapping("/getDatalog")
    public ResponseData getLogByMacAddr(String macAddr, int pageSize, int currentPage){
        ListPageUtil<DataLog> list = null;
        try {
            list = dataLogService.getLogByMacAddr(macAddr.replace("-", ":").toUpperCase(), pageSize, currentPage);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "查询失败", list);
        }

        return new ResponseData(true, "0", "查询成功", list);
    }
}
