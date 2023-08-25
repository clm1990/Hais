package com.hais.hais1000.utils.httpStruct;

import lombok.Data;

@Data
public class ProjectResponStruct {
    private String projectId;
    private String projectName;
    private String projectAddress;
    private Integer projectState;
    private String projectDescribe;
    private String projectStartTime;
    private double chiWaterSupplyTemp;
    private double chTempColdSet;
    private double chTempHeatSet;
    private Integer chMode;
    private Integer chRunState;
    private String macAddr;
    private String projectSn;
}
