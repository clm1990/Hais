package com.hais.hais1000.dto;

import lombok.Data;

@Data
public class VirDevInfo {
    private String projectID;
    private String logicalNodeID;
    private String virDevTypeID;
    private String virDevTypeName;
    private String virDevID;
    private String virDevName;
    private String description;
    private Integer virDevState;
    private Integer peculiarity;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
