package com.hais.hais1000.dto;

import lombok.Data;

@Data
public class VirDevParamInfo {
    private String virDevID;
    private Integer paramSeq;
    private String paramName;
    private Double paramValue;
    private Integer paramType;
    private Integer paramLevel;
    private Integer paramUIType;
    private Integer paramSign;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
