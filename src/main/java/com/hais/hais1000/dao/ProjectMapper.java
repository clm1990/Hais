package com.hais.hais1000.dao;

import com.hais.hais1000.dto.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {

    public List<ProjectInfo> getAllProjectInfo();

    public void addProjectInfo(ProjectInfo projectInfo);

    public void delProjectInfo(String projectId);

    public void updateProjectInfo(ProjectInfo projectInfo);

    public ProjectInfo getProjectInfoBySn(String sn);

    public ProjectInfo getProjectInfoByID(String projectID);

    public List<ProjectInfo> getProjectInfo(@Param("projectName") String projectName, @Param("macAddr")  String macAddr,@Param("projectAddress") String projectAddress);
}
