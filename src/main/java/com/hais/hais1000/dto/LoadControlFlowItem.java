package com.hais.hais1000.dto;

import lombok.Data;

@Data
public class LoadControlFlowItem {
    private String flowID;
    private double startValue;
    private String startTime;
    private String endTime;
    private Integer flowState;
    private String callType;
    private double callValue;
    private String cid;
    private String Sn;
    private String eventTime;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
