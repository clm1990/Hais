package com.hais.hais1000;

import com.hais.hais1000.comm.mqtt.CloudMqttClient;
import com.hais.hais1000.comm.mqtt.ThridMqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hais1000Init implements CommandLineRunner {

    @Autowired
    CloudMqttClient mqttClient;

    @Autowired
    ThridMqttClient thridMqttClient;

    @Override
    public void run(String... args) throws Exception {
        mqttClient.initCloundConnet();
        thridMqttClient.initCloundConnet();
    }
}
