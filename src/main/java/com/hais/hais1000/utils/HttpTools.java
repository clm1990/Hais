package com.hais.hais1000.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpTools {
    static public String doGet(String url, String author) throws Exception{
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(1000).setSocketTimeout(1000).build();
        httpGet.setConfig(requestConfig);

        //设置类型 "application/x-www-form-urlencoded" "application/json"
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if(null != author){
            httpGet.setHeader("authorization", author);
        }



        //System.out.println("URL: " + httpGet.getURI());

        //httpClient实例化
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 执行请求并获取返回
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        //System.out.println("result code" + response.getStatusLine());

        // 显示结果
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
        String line = null;
        StringBuffer responseSB = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            responseSB.append(line);
        }
        //System.out.println("recv message:" + responseSB);
        reader.close();

        httpClient.close();

        return responseSB.toString();
    }
}
