package com.hais.hais1000.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ControlLog {
    private String macAddr;
    private String log;
    private LocalDateTime eventTime;
}
