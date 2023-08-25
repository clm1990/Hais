package com.hais.hais1000.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.hais.hais1000.comm.ResponseData;
import com.hais.hais1000.dto.UserInfo;
import com.hais.hais1000.service.UserInfoService;
import com.hais.hais1000.utils.JWTUtil;
import com.hais.hais1000.utils.httpStruct.UserInfoStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;



    @ResponseBody
    @PostMapping("/login")
    public ResponseData userLogin(@RequestBody UserInfoStruct user){

        String tokenStr = new String();

        System.out.println("#########"+ user);

        try {
            UserInfo userInfo = userInfoService.getUserInfo(user.getUserName());
            if(userInfo == null){
                return new ResponseData(true, "-1", "用户不存在", "");
            }

            if(!userInfo.getPassword().equals(user.getPassWord())){
                return new ResponseData(true, "-1", "密码错误", "");
            }

            Map<String, String> userMap = new HashMap<>();
            userMap.put("userName", user.getUserName());
            userMap.put("passWord", user.getPassWord());
            tokenStr = JWTUtil.createToken(userMap);


        }catch (Exception exception){
            return new ResponseData(true, "-1", "登录失败", "");
        }

        return new ResponseData(true, "0", "登录成功", tokenStr);
    }

    //public ResponseData getUserInfo(){
    //    return new ResponseData(true, "0", "登录成功", token);
    //}
}
