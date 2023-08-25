package com.hais.hais1000.service;

import com.hais.hais1000.dao.UserInfoMapper;
import com.hais.hais1000.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    public UserInfo getUserInfo(String userName){
        return userInfoMapper.getUserInfo(userName);
    }

    public List<UserInfo> getAllUserInfo(){
        return userInfoMapper.getAllUserInfo();
    }
}
