package com.hais.hais1000.mqttService.dto;

import lombok.Data;

@Data
public class MqttDataLog {
    private double pt;
    private double totw;
    private double ep;
    private double ua;
    private double ub;
    private double uc;
    private double ia;
    private double ib;
    private double ic;
    private double chiWaterSupplyTemp;
    private double tempColdSet;
    private double tempHeatSet;
    private double pointFaul;
    private Integer online;
    private String sn;
    private String time;
}
