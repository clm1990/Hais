package com.hais.hais1000.task;

import com.hais.hais1000.service.LoadControlFlowItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class LoadContorlScheduleTask {
    @Autowired
    LoadControlFlowItemService loadControlFlowItemService;

    @Scheduled(cron = "*/30 * * * * ?")
    public void LoadControlStartTaskRun(){
        loadControlFlowItemService.LoadControlStartTaskRun();
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void LoadControlEndTaskRun(){
        loadControlFlowItemService.LoadControlEndTaskRun();
    }
}
