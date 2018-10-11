package com.bonaparte.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.karakal.commons.http.HttpClientUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by yangmingquan on 2018/10/11.
 */
public class HttpUtil {
    private static Logger logger = Logger.getLogger(HttpUtil.class);

    public static RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(50000).setSocketTimeout(150000).build();


    public static Object post(String url, Map<String, String> params, Map<String, String> headers){
        Object body = null;
        try{
            logger.info("开始调用接口"+url);
            String res = HttpClientUtil.post(url, params, headers, defaultRequestConfig).toString();
            logger.info("接口返回值为:"+res);
            JSONObject resultJson = JSON.parseObject(res);
            if(resultJson.getInteger("status") != 1){
                throw new RuntimeException("调用接口："+url+"失败！");
            }
            body = resultJson.get("data");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("调用接口："+url+"失败！");
        }
        return body;
    }

    public static JSONObject get(String url, Map<String, String> params, Map<String, String> headers){
        JSONObject resultJson = null;
        try{
            String path = StringTools.spliceUrl(url, params);
            logger.info("开始调用接口"+path);
            String searchRes = HttpClientUtil.get(path, headers).toString();
            logger.info("接口返回值为:"+searchRes);
            resultJson = JSON.parseObject(searchRes);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("调用接口："+url+"失败！");
        }
        return resultJson;
    }

    public static JSONArray getList(String url, Map<String, String> params, Map<String, String> headers){
        JSONArray resultJson = null;
        try{
            String path = StringTools.spliceUrl(url, params);
            logger.info("开始调用接口"+path);
            String searchRes = HttpClientUtil.get(path, headers).toString();
            logger.info("接口返回值为:"+searchRes);
            resultJson = JSON.parseArray(searchRes);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("调用接口："+url+"失败！");
        }
        return resultJson;
    }

    public static Object post(String url, Map<String, String> params) throws ClientProtocolException, IOException, URISyntaxException {
        HttpClient httpclient = new HttpClient();
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
        for(String key : params.keySet()){
            post.addParameter(key, params.get(key));
        }
        httpclient.executeMethod(post);
        String info = new String(post.getResponseBody(),"gbk");
        logger.info("发送短信接口返回值为"+info);
        return info;
    }
}
