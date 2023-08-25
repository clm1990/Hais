package com.hais.hais1000.utils.httpStruct;

import lombok.Data;

import java.util.List;

@Data
public class ProjectInfo {
    ProjectIsOnlineStruct projectIsOnlineInfo;
    List<ProjectResponStruct> projectListInfo;
}
