package com.hais.hais1000.utils.httpStruct;

import lombok.Data;

@Data
public class ProjectIsOnlineStruct {
    private Integer totalProjectNum;
    private Integer onLineProjectNum;
    private Integer leaveLineProjectNum;
    private double onLineProjectPercent;
}
