package com.hais.hais1000.dto;

import lombok.Data;

@Data
public class UserInfo {
    private String userID;
    private String userName;
    private String password;
    private String userLevel;
    private String jurisdiction;
    private String lastLoginTime;
    private String reserved1;
    private String reserved2;
    private Integer reserved3;
}
