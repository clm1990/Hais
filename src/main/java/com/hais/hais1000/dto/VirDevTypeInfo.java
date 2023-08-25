package com.hais.hais1000.dto;

import com.hais.hais1000.Hais1000Application;
import lombok.Data;
import org.springframework.boot.SpringApplication;

@Data
public class VirDevTypeInfo {
    private String virDevTypeID;
    private String virDevTypeName;
    private Integer virClass;
    private Integer section;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
