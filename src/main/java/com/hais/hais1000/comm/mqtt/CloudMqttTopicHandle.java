package com.hais.hais1000.comm.mqtt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.hais.hais1000.dto.DataLog;
import com.hais.hais1000.mqttService.CloudReciveMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CloudMqttTopicHandle  implements MqttCallback {
    private String[] topicFilters;
    private CloudMqttClient mqtt;
    private CloudReciveMessage cloudReciveMessage;
    private ThridMqttClient thridMqttClient;

    public CloudMqttTopicHandle(String[] topicFilters, CloudMqttClient mqtt, ThridMqttClient thridMqttClient, CloudReciveMessage cloudReciveMessage) {
        this.topicFilters = topicFilters;
        this.mqtt = mqtt;
        this.thridMqttClient = thridMqttClient;
        this.cloudReciveMessage = cloudReciveMessage;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("设备离线-------" + throwable.getMessage());
        mqtt.mqttRConnect();
        log.info("连接物管mqtt server成功-------");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("Client 接收消息主题1>>>>" + topic);
        if (topicFilters != null && topicFilters.length > 0 && Arrays.asList(topicFilters).contains(topic)) {
            log.info("Client 接收消息主题>>>>" + topic);
            log.info("Client 接收消息Qos>>>>" + message.getQos());

            try {

                switch (topic) {
                    case CloudMqttClient.MQTT_DATA_LOG:
                        cloudReciveMessage.dataLogHandle(message.getPayload());
                        dataLogHandle(message.getPayload());
                        break;
                    default:
                        break;
                }


            } catch (Exception e) {
                log.error("板卡执行消息发送错误>>>>{}", e);
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void dataLogHandle(byte[] msg){
        try {

            String dataLog = new String(msg, StandardCharsets.UTF_8);

            byte[] bytes = dataLog.getBytes();
            JSONObject data = JSON.parseObject(bytes);
            //System.out.println(data);

            /************************************************************/
            JSONObject device = data.getJSONObject("data").getJSONArray("devices").getJSONObject(0);
            JSONObject service = device.getJSONArray("services").getJSONObject(0);
            JSONObject bodydata = service.getJSONObject("data").getJSONArray("body").getJSONObject(0);


            if(bodydata.get("pt") != null)
                bodydata.fluentPut("pt", bodydata.get("pt").toString());
            if(bodydata.get("ep") != null)
                bodydata.fluentPut("ep", bodydata.get("ep").toString());
            if(bodydata.get("totw") != null)
                bodydata.fluentPut("totw", bodydata.get("totw").toString());
            if(bodydata.get("ua") != null)
                bodydata.fluentPut("ua", bodydata.get("ua").toString());
            if(bodydata.get("ub") != null)
                bodydata.fluentPut("ub", bodydata.get("ub").toString());
            if(bodydata.get("uc") != null)
                bodydata.fluentPut("uc", bodydata.get("uc").toString());
            if(bodydata.get("ia") != null)
                bodydata.fluentPut("ia", bodydata.get("ia").toString());
            if(bodydata.get("ib") != null)
                bodydata.fluentPut("ib", bodydata.get("ib").toString());
            if(bodydata.get("ic") != null)
                bodydata.fluentPut("ic", bodydata.get("ic").toString());
            if(bodydata.get("ChiWaterSupplyTemp") != null)
                bodydata.fluentPut("ChiWaterSupplyTemp", bodydata.get("ChiWaterSupplyTemp").toString());
            if(bodydata.get("point_faul") != null)
                bodydata.fluentPut("point_faul", bodydata.get("point_faul").toString());
            if(bodydata.get("online") != null)
                bodydata.fluentPut("online", bodydata.get("online").toString());
            if(bodydata.get("temp_heat_set") != null)
                bodydata.fluentPut("temp_heat_set", bodydata.get("temp_heat_set").toString());
            if(bodydata.get("temp_cold_set") != null)
                bodydata.fluentPut("temp_cold_set", bodydata.get("temp_cold_set").toString());

            String sn = bodydata.getString("sn");
            JSONObject devices = new JSONObject();
            JSONArray datas = new JSONArray();

            JSONObject datadata = new JSONObject();
            datadata.put("type", "dots-central");
            datadata.put("body", bodydata);

            JSONObject services = new JSONObject();
            JSONObject servicesDatas = new JSONObject();
            servicesDatas.put("data", datadata);
            servicesDatas.put("eventTime", service.getString("eventTime"));
            servicesDatas.put("serviceId", service.getString("serviceId"));

            JSONArray d = new JSONArray();
            d.add(servicesDatas);
            services.put("services", d);
            services.put("deviceId", sn);

            datas.add(services);
            devices.put("devices", datas);

            String topic = "/v1/devices/" + "YWY-ZK0001" + "/datas";
            //System.out.println(topic);
            //System.out.println(devices.toString());
            thridMqttClient.publish(topic, devices.toString().getBytes());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
