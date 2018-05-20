package com.ksblletba.orangemusic.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.adapter.PlayListAdapter;
import com.ksblletba.orangemusic.bean.PlayListItem;
import com.ksblletba.orangemusic.utils.HttpUtils;
import com.ksblletba.orangemusic.utils.NetWorkUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends Fragment {
    @BindView(R.id.playlist_recyclerview)
    RecyclerView playlistRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.playlist_recyclerview2)
    RecyclerView playlistRecyclerview2;
    @BindView(R.id.playlist_recyclerview3)
    RecyclerView playlistRecyclerview3;
    private PlayListAdapter adapter;


    public PlayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        playlistRecyclerview = view.findViewById(R.id.playlist_recyclerview);
        playlistRecyclerview2 = view.findViewById(R.id.playlist_recyclerview2);
        playlistRecyclerview3 = view.findViewById(R.id.playlist_recyclerview3);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initView() {
        String playlistUrl = "https://api.imjad.cn/cloudmusic/?type=search&search_type=1000&s=民谣&offset=1&limit=10";
        HttpUtils.sendOkHttpRequestbyPost(playlistUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("data", "竟然tm");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<PlayListItem> playListItems = NetWorkUtil.getPlaylist(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                        adapter = new PlayListAdapter(playListItems);
                        playlistRecyclerview.setLayoutManager(glm);
                        playlistRecyclerview.setAdapter(adapter);
                    }
                });
            }
        });
        String playlistUrl2 = "https://api.imjad.cn/cloudmusic/?type=search&search_type=1000&s=华语&offset=1&limit=10";
        HttpUtils.sendOkHttpRequestbyPost(playlistUrl2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("data", "竟然tm");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<PlayListItem> playListItems = NetWorkUtil.getPlaylist(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                        adapter = new PlayListAdapter(playListItems);
                        playlistRecyclerview2.setLayoutManager(glm);
                        playlistRecyclerview2.setAdapter(adapter);
                    }
                });
            }
        });

        String playlistUrl3 = "https://api.imjad.cn/cloudmusic/?type=search&search_type=1000&s=欧美&offset=1&limit=10";
        HttpUtils.sendOkHttpRequestbyPost(playlistUrl3, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("data", "竟然tm");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<PlayListItem> playListItems = NetWorkUtil.getPlaylist(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                        adapter = new PlayListAdapter(playListItems);
                        playlistRecyclerview3.setLayoutManager(glm);
                        playlistRecyclerview3.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
