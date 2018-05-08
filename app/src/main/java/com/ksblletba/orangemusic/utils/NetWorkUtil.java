package com.ksblletba.orangemusic.utils;

import com.google.gson.Gson;
import com.ksblletba.orangemusic.bean.NetworkSong;
import com.ksblletba.orangemusic.manager.PlayManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/7.
 */

public class NetWorkUtil {

    /**
     * 网易音乐搜索API
     * http://s.music.163.com/search/get/?type=1&s=海阔天空&limit=10&offset=0
     * 获取方式：GET
     * 参数：
     * src: lofter //可为空
     * type: 1
     * filterDj: true|false //可为空
     * s: //关键词
     * limit: 10 //限制返回结果数
     * offset: 0 //偏移
     * callback: //为空时返回json，反之返回jsonp callback

     * @return
     * 注意废数字才用‘’符号，要不不能用，否则出错！！
     */

    public static List<NetworkSong> getNetWorkSong(String response){
        List<NetworkSong> networkSongs = new ArrayList<>();
        try{
            JSONObject all = new JSONObject(response);
            JSONObject result = all.getJSONObject("result");
            JSONArray songs = result.getJSONArray("songs");
            for(int i=0;i<songs.length();i++){
               JSONObject song = songs.getJSONObject(i);
               String songContent = song.toString();
               NetworkSong networkSong = new Gson().fromJson(songContent,NetworkSong.class);
               networkSong.setSongId(song.getInt("id"));
               networkSongs.add(networkSong);
            }


            return networkSongs;

        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getPlayAdress(String response){
        try{
            JSONObject all = new JSONObject(response);
            JSONObject data = all.getJSONArray("data").getJSONObject(0);
            String adress = data.getString("url");
            return adress;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }


}
