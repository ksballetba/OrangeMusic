package com.ksblletba.orangemusic.fragment;



import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ksblletba.orangemusic.MainActivity;
import com.ksblletba.orangemusic.PlayDetailActivity;
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
    private SearchView searchView;
    private String searchKey;
    private SharedPreferences pref;
    private final NetworkSong mNetSong=null;
    private Song currentSong=null;
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



    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchKey = query;
            Log.d("data", "onQueryTextSubmit: "+searchKey);
            musicListItemList.clear();
            getNetWorkSongs(searchKey);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

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
                    currentSong = songs.get(position);
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
            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            List<Song> songList = MediaUtils.getAudioList(getContext());
            if (currentSong == null) {
                currentSong = songList.get(0);
            }
            for (Song song : songList) {
                if (song.getTitle().equals(pref.getString("song_name", "")))
                    currentSong = song;
            }
            searchView = getActivity().findViewById(R.id.search_view);
            searchView.setOnQueryTextListener(onQueryTextListener);
            final SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh);
            musicRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int topRowVerticalPosition =
                            (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                    swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                }
            });
            swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getNetWorkSongs(searchKey);
                }
            });
            searchKey = ((SearchActivity) getActivity()).getSearchKey();
            getNetWorkSongs(searchKey);
        }

    }




    private void getNetWorkSongs(String songName) {
        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setRefreshing(true);
        String Url = "http://music.163.com/api/search/pc/?type=1&s="+songName+"&offset=0&limit=30";
        HttpUtils.sendOkHttpRequestbyPost(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final List<NetworkSong> netWorkSongs = NetWorkUtil.getNetWorkSong(responseText);
                for (NetworkSong netWorkSong : netWorkSongs) {
                    String picURL = netWorkSong.getAlbum().getPicUrl();
                    musicListItemList.add(new MusicListItem(netWorkSong.getName(),picURL,netWorkSong.getArtists().get(0).getName()));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
                        musicRecyclerview.setLayoutManager(linearLayoutManager);
                        adapter = new MusicListItemAdapter(musicListItemList);
                        musicRecyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new MusicListItemAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (PlayManager.getInstance(getContext()).isService()) {
                                    final NetworkSong networkSong = netWorkSongs.get(position);
                                    String playAdress = "https://api.imjad.cn/cloudmusic/?type=song&id=" + networkSong.getSongId() + "&br=128000";
                                    HttpUtils.sendOkHttpRequestbyGet(playAdress, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            final String respt = response.body().string();
                                            String playAdress = NetWorkUtil.getPlayAdress(respt);
                                            PlayManager.getInstance(getContext()).playNetSong(networkSong, playAdress);
                                            Log.d("data", "onClick: " + PlayManager.getInstance(getContext()).getmState());
                                            Log.d("data", "" + networkSong.getName());
//                                        Message msg =
                                        }
                                    });
                                    Intent intent = new Intent(getActivity(), PlayDetailActivity.class);
                                    Log.d("data", "+++" + PlayManager.getInstance(getContext()).getmNetSong());
                                    startActivity(intent);
                                } else {
                                    PlayManager.getInstance(getActivity()).dispatch(currentSong,"   ");
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
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
