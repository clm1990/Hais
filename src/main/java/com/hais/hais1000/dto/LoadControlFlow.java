package com.hais.hais1000.dto;

import lombok.Data;

@Data
public class LoadControlFlow {
    private String flowID;
    private String msgType;
    private Integer mid;
    private String cmd;
    private String serviceID;
    private String deviceID;
    private String eventTime;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
