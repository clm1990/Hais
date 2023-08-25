package com.hais.hais1000.comm.mqtt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.hais.hais1000.mqttService.CloudReciveMessage;
import com.hais.hais1000.mqttService.ThirdReciveMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class ThridMqttTopicHandle   implements MqttCallback {
    private ThridMqttClient mqtt;
    private ThirdReciveMessage thirdReciveMessage;


    public ThridMqttTopicHandle(String[] topicFilters, ThridMqttClient mqtt, ThirdReciveMessage thirdReciveMessage) {
        this.thirdReciveMessage = thirdReciveMessage;
        this.mqtt = mqtt;
    }
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("设备离线-------" + throwable.getMessage());
        mqtt.mqttRConnect();
        log.info("连接物管mqtt server成功-------");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("Client 接收消息主题>>>>" + topic);
        log.info("Client 接收消息Qos>>>>" + message.getQos());

        try {
            switch (topic) {
                case "/v1/devices/YWY-ZK0001/command":
                    thirdReciveMessage.dataLogHandle(message.getPayload());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("板卡执行消息发送错误>>>>", e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}
