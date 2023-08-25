package com.hais.hais1000.comm.mqtt;

import com.hais.hais1000.dao.DataLogMapper;
import com.hais.hais1000.dao.ProjectMapper;
import com.hais.hais1000.mqttService.CloudReciveMessage;
import com.hais.hais1000.mqttService.ThirdReciveMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

@Slf4j
@Component
public class ThridMqttClient {

    public final static String MQTT_DATA_LOG = "/data/log";
    // 超时时间
    private final static int MQTT_TIMEOUT = 3;
    // 心跳时间
    private final static int MQTT_KEEPALIVE = 30;
    // 服务质量 0：最多一次的传输 1：至少一次的传输 2： 只有一次的传输
    private final static int QOS = 1;
    private static MqttClient client = null;
    private static MemoryPersistence memoryPersistence = null;

    @Value("${thirdmqtt.mqtturl}")
    private String mqtt_URL;
    @Value("${thirdmqtt.port}")
    private String mqtt_Port;
    @Value("${thirdmqtt.username}")
    private String MQTT_USERNAME;
    @Value("${thirdmqtt.password}")
    private String MQTT_PASSWORD;
    @Value("${thirdmqtt.topic}")
    private String MQTT_TOPIC;
    @Value("${thirdmqtt.clientid}")
    private String MQTT_CLIENTID;

    @Autowired
    private ThirdReciveMessage thirdReciveMessage;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    DataLogMapper dataLogMapper;

    public void initCloundConnet(){
        log.debug(mqtt_URL + mqtt_Port + MQTT_USERNAME + MQTT_PASSWORD + MQTT_TOPIC + MQTT_CLIENTID);
        // 判断是否需要进行下步连接动作
        if (!StringUtil.isEmpty(mqtt_URL)) {
            connect();
        }
    }

    /**
     * 重连
     */
    public void mqttRConnect() {
        // 判断是否需要进行下步连接动作
        if (!StringUtil.isEmpty(mqtt_URL)) {
            closeConnect();
            connect();
        }
    }

    public void closeConnect() {
        // 关闭存储方式
        if (null != memoryPersistence) {
            try {
                memoryPersistence.close();
            } catch (MqttPersistenceException e) {
                log.error("memoryPersistence close", e);
            }
        }

        // 关闭连接
        if (null != client) {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                    client.close();
                } catch (MqttException e) {
                    log.error("mqttClient close", e);
                }
            }
        }
    }

    public boolean mqttIsConnect() {
        if (null != client && client.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private synchronized void connect() {
        String[] strTopicFilter = getTopicFilter();

        if (StringUtil.isEmpty(mqtt_URL)) {
            return;
        }
        if (null != client && client.isConnected()) {
            return;
        }
        String mqttHost = "tcp://" + mqtt_URL + ":" + mqtt_Port;
        log.info("mqtt connect...host:" + mqttHost + "/r" + " mqtt connect...clientid:" + MQTT_CLIENTID);
        try {// 防止重复创建MQTTClient实例
            client = new MqttClient(mqttHost, MQTT_CLIENTID, memoryPersistence);
            log.info("client>>>connection>>>>:" + client);
            if (client != null && !client.isConnected()) {
                client.setCallback(
                        new ThridMqttTopicHandle(strTopicFilter, this, thirdReciveMessage));

                MqttConnectOptions options = getMqttConnectOptions();
                client.connect(options);

                log.info("mqtt connect success");
                // 订阅主题
                subscribe(strTopicFilter);
            }
        } catch (MqttException e) {
            log.error("MqttClient is error>>>>", e);
        }
    }

    private String[] getTopicFilter() {
        String[] strTopicFilter = MQTT_TOPIC.split(",");
/*
        if (strTopicFilter != null && strTopicFilter.length > 0) {
            for (int i = 0; i < strTopicFilter.length; i++) {
                String item = strTopicFilter[i];

                if (!item.endsWith("/")) {
                    item += "/";
                }

                item += Constant.Node.PROJECT_ID;
                strTopicFilter[i] = item;
            }
        }

 */
        return strTopicFilter;
    }

    /**
     * 设置Mqtt参数
     *
     * @return
     */
    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions option = new MqttConnectOptions();
        //option.setCleanSession(true);
        // 用户名
        option.setUserName(MQTT_USERNAME);
        // 密码
        option.setPassword(MQTT_PASSWORD.toCharArray());
        // 超时时间
        option.setConnectionTimeout(MQTT_TIMEOUT);
        // 心跳时间
        option.setKeepAliveInterval(MQTT_KEEPALIVE);

        //option.setAutomaticReconnect(true);
        //option.setCleanSession(false);

        option.setMaxInflight(1200);

        return option;
    }

    /**
     * 发布主题，用于通知 默认qos为1 非持久化
     *
     * @param topic 主题
     * @param data  消息
     */
    public Boolean publish(String topic, byte[] data) {
        Boolean result = true;

        MqttMessage message = new MqttMessage();
        message.setQos(QOS);
        message.setPayload(data);

        log.info("client.isConnected:" + (client != null));
        if (null == client || !client.isConnected()) {
            if (StringUtil.isEmpty(mqtt_URL))
                return false;
            //else {
            //    connect();
            //}
        }

        MqttTopic mqttTopic = client.getTopic(topic);
        if (null != mqttTopic) {
            try {
                MqttDeliveryToken publish = mqttTopic.publish(message);

//				String strMessage = message.toString();
//				strMessage = StringUtils.isEmptyOrWhitespace(strMessage) ? "" : strMessage.substring(0, 500);
                log.info(publish.isComplete() + "消息发布：【" + topic + "】");// - 【" + strMessage + "】");
            } catch (MqttException e) {
                log.error("mqtt publish error>>>>", e);
                result = false;
            }
        }

        return result;
    }

    /**
     * 订阅某个主题
     *
     * @param topics
     * @throws MqttException
     */
    private void subscribe(String[] topics) throws MqttException {
        client.subscribe(topics);
    }

    /**
     * 定时重连
     *
     **/
    public void ConnectStateCheck(){
        if(!mqttIsConnect()){
            log.info("物管连接断开，尝试重连");
            mqttRConnect();
        }else{
            log.info("物管连接正常");
        }
    }
}
