package com.hais.hais1000.dto;

import lombok.Data;

@Data
public class VirDevTypeParamInfo {
    private String virDevTypeID;
    private Integer paramSeq;
    private String paramName;
    private Integer paramType;
    private Integer paramIsKey;
    private Integer paramUIType;
    private Integer paramSign;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
