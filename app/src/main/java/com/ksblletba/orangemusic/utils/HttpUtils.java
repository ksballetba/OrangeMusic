package com.ksblletba.orangemusic.utils;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/5/7.
 */

public class HttpUtils {
    public static void sendOkHttpRequestbyPost(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestbyGet(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestforStream(String address,okhttp3.Callback callback){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            InputStream in = client.newCall(request).execute().body().byteStream();
            client.newCall(request).enqueue(callback);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
