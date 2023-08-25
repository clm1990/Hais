package com.hais.hais1000.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DataLog {
    private String macAddr;
    private String log;
    private LocalDateTime eventTime;

}
