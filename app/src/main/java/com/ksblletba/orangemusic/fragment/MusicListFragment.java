package com.ksblletba.orangemusic.fragment;


import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ksblletba.orangemusic.MainActivity;
import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.adapter.MusicListItemAdapter;
import com.ksblletba.orangemusic.bean.MusicListItem;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MusicListFragment extends Fragment {

//    @BindView(R.id.music_listview)
//    ListView musicListview;
    private MusicListItemAdapter adapter;
    private List<MusicListItem> musicListItemList = new ArrayList<>();
    private List<String> musicNameList = new ArrayList<>();


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
                Uri currentSongArt = MediaUtils.getAlbumArtUri(currentSong.getAlbumId());
                ma.setMusicInfo(currentSongArt,currentSong.getTitle(),currentSong.getArtist());
                ma.setCurrentSong(currentSong);
                PlayManager pm = PlayManager.getInstance(getContext());
                pm.dispatch(currentSong,"tick");
                ma.onPlayStateChange(PlayManager.getInstance(getContext()).isPlaying());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        musicRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MusicListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
