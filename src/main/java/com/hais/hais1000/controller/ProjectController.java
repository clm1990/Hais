package com.hais.hais1000.controller;

import com.hais.hais1000.comm.ListPageUtil;
import com.hais.hais1000.comm.ResponseData;
import com.hais.hais1000.dto.ProjectInfo;
import com.hais.hais1000.service.ProjectService;
import com.hais.hais1000.utils.HttpTools;
import com.hais.hais1000.utils.JWTUtil;
import com.hais.hais1000.utils.httpStruct.ProjectIsOnlineStruct;
import com.hais.hais1000.utils.httpStruct.ProjectResponStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @ResponseBody
    @GetMapping("/getProjectOnLineInfo")
    public ResponseData getProjectOnLineInfo(){
        ProjectIsOnlineStruct projectOnline = new ProjectIsOnlineStruct();

        try {
            projectOnline = projectService.getProjectOnLineInfo();
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "添加失败", "null");
        }

        return new ResponseData(true, "0", "添加成功", projectOnline);
    }

    @ResponseBody
    @GetMapping("/getProjectInfo")
    public ResponseData getAllProjectInfo(int pageSize, int currentPage){
        ListPageUtil<ProjectResponStruct> list = null;
        try {
            list = projectService.getAllProjectInfo(pageSize, currentPage);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "查询失败",  null);
        }

        return new ResponseData(true, "0", "查询成功",  list);
    }

    @ResponseBody
    @GetMapping("/getProjectInfoByParam")
    public ResponseData getProjectInfo(int pageSize, int currentPage, String projectName, String macAddr, String projectAddress){
        ListPageUtil<ProjectResponStruct> list = null;

        try {
            list = projectService.getProjectInfo(pageSize, currentPage, projectName, macAddr, projectAddress);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "查询失败",  null);
        }

        return new ResponseData(true, "0", "查询成功",  list);
    }

    @ResponseBody
    @PostMapping("/addProjectInfo")
    public ResponseData addProjectInfo(@RequestBody ProjectInfo projectInfo){
        try {
            projectService.addProjectInfo(projectInfo);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "添加失败", "null");
        }

        return new ResponseData(true, "0", "添加成功", "null");
    }

    @ResponseBody
    @PostMapping("/updateProjectInfo")
    public ResponseData updateProjectInfo(@RequestBody ProjectInfo projectInfo){
        System.out.println("############" + projectInfo);

        try {
            projectService.updateProjectInfo(projectInfo);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "修改失败", "null");
        }

        return new ResponseData(true, "0", "修改成功", "null");
    }

    @ResponseBody
    @GetMapping("/delProjectInfo")
    public ResponseData delProjectInfo(String projectID){
        System.out.println("#########" + projectID);
        try {
            projectService.delProjectInfo(projectID);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseData(true, "-1", "删除失败", "null");
        }

        return new ResponseData(true, "0", "删除成功", "null");
    }
}
