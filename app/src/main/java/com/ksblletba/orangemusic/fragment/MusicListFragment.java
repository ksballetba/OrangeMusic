package com.ksblletba.orangemusic.fragment;


import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ksblletba.orangemusic.MainActivity;
import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.SearchActivity;
import com.ksblletba.orangemusic.adapter.MusicListItemAdapter;
import com.ksblletba.orangemusic.bean.MusicListItem;
import com.ksblletba.orangemusic.bean.NetworkSong;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.utils.HttpUtils;
import com.ksblletba.orangemusic.utils.MediaUtils;
import com.ksblletba.orangemusic.utils.NetWorkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MusicListFragment extends Fragment {

    private MusicListItemAdapter adapter;
    private List<MusicListItem> musicListItemList = new ArrayList<>();
    private List<String> musicNameList = new ArrayList<>();
    public static String SEARCH_FOREURL="https://v1.hitokoto.cn/nm/search/";
    public static String SEARCH_TYPEURL="?type=";
    public static String SEARCH_LIMITURL="&offset=0&limit=1";


    @BindView(R.id.music_recyclerview)
    RecyclerView musicRecyclerview;
    Unbinder unbinder;

    public MusicListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        musicRecyclerview = view.findViewById(R.id.music_recyclerview);
//        initRecyclerView();
        MusicListFragmentPermissionsDispatcher.initViewWithPermissionCheck(this);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initView() {
        if (getActivity()instanceof MainActivity) {
            final List<Song> songs = MediaUtils.getAudioList(getActivity());
            for (Song song : songs) {
                Uri albumArt = MediaUtils.getAlbumArtUri(song.getAlbumId());
                musicListItemList.add(new MusicListItem(song.getTitle(),albumArt,song.getArtist()));
            }

            Log.d("data", "+++"+songs.get(0).getArtist());
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
            musicRecyclerview.setLayoutManager(linearLayoutManager);
            adapter = new MusicListItemAdapter(musicListItemList);
            adapter.setOnItemClickListener(new MusicListItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Song currentSong = songs.get(position);
                    MainActivity ma = (MainActivity) getActivity();
                    ma.setCurrentSong(currentSong);
                    PlayManager pm = PlayManager.getInstance(getContext());
                    pm.dispatch(currentSong,"tick");
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            musicRecyclerview.setAdapter(adapter);
        } else if(getActivity()instanceof SearchActivity){
//            initSearchList((((SearchActivity) getActivity())).getSearchKey());
            SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setRefreshing(true);
            getNetWorkSongId("海阔天空");
        }

    }

    private void initSearchList(String key){
        String searchAdress = "https://v1.hitokoto.cn/nm/search/?t海阔天空ype=SONG&offset=0&limit=10";

        HttpUtils.sendOkHttpRequest(searchAdress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                List<NetworkSong> networkSongs=NetWorkUtil.getNetWorkSong(responseText);
                for (NetworkSong networkSong : networkSongs) {
                    musicListItemList.add(new MusicListItem(networkSong.getName()," ",networkSong.getArtists().get(0).getName()));
                    Log.d("data", "###"+networkSong.getName());
                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
//                        musicRecyclerview.setLayoutManager(linearLayoutManager);
//                        adapter = new MusicListItemAdapter(musicListItemList);
//                        musicRecyclerview.setAdapter(adapter);
//                    }
//                });

            }
        });


    }

    void getNetWorkSongId(String songName) {
        String Url = "https://v1.hitokoto.cn/nm/search/" + songName + "?type=SONG&offset=0&limit=10";
        HttpUtils.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();


                List<NetworkSong> netWorkSongs = NetWorkUtil.getNetWorkSong(responseText);
                final NetworkSong demo = netWorkSongs.get(0);
                Log.d("data", "onResponse: " + demo.getSongid());
                Log.d("data", "onResponse: " + demo.getName());
                for (NetworkSong netWorkSong : netWorkSongs) {
                    musicListItemList.add(new MusicListItem(netWorkSong.getName()," ",netWorkSong.getArtists().get(0).getName()));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
                        musicRecyclerview.setLayoutManager(linearLayoutManager);
                        adapter = new MusicListItemAdapter(musicListItemList);
                        musicRecyclerview.setAdapter(adapter);
                        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MusicListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
