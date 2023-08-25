package com.hais.hais1000.dao;

import com.hais.hais1000.dto.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    public UserInfo getUserInfo(@Param("userName") String userName);

    public List<UserInfo> getAllUserInfo();
}
