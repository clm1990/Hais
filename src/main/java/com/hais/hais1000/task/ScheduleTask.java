package com.hais.hais1000.task;

import com.hais.hais1000.comm.mqtt.CloudMqttClient;
import com.hais.hais1000.comm.mqtt.ThridMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduleTask {

    @Autowired
    CloudMqttClient cloudMqttClient;

    @Autowired
    ThridMqttClient thridMqttClient;

    @Scheduled(cron = "0 */1 * * * ?")
    public void CloudMqttConnectStateCheck(){
        cloudMqttClient.ConnectStateCheck();
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void ThridMqttConnectStateCheck(){
        thridMqttClient.ConnectStateCheck();
    }

}
