package com.ksblletba.orangemusic.utils;

import com.google.gson.Gson;
import com.ksblletba.orangemusic.bean.NetworkSong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class NetWorkUtil {

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
               networkSong.setSongid(song.getInt("id"));
               networkSongs.add(networkSong);
            }


            return networkSongs;

        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }


}
