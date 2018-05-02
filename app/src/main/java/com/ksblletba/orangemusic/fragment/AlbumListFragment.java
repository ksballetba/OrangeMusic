package com.ksblletba.orangemusic.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.AlbumActivity;
import com.ksblletba.orangemusic.MainActivity;
import com.ksblletba.orangemusic.PlayDetailActivity;
import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.adapter.AlbumListItemAdapter;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.AlbumListItem;
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
public class AlbumListFragment extends Fragment {
    private List<AlbumListItem> albumListItemList = new ArrayList<>();
    private AlbumListItemAdapter adapter;
    private LinearLayout linearLayout;



    @BindView(R.id.album_recyclerview)
    RecyclerView albumRecyclerview;
    Unbinder unbinder;

    public AlbumListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);
        albumRecyclerview = view.findViewById(R.id.album_recyclerview);
        linearLayout = view.findViewById(R.id.album_bg);
//        initViewPager();
        AlbumListFragmentPermissionsDispatcher.initViewWithPermissionCheck(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    private void initViewPager(){
//        AlbumListItem albumListItem=new AlbumListItem("如风过境",R.drawable.music,"哎哟蔚蔚");
//        for (int i = 0; i < 20; i++) {
//            albumListItemList.add(albumListItem);
//        }
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
//        adapter = new AlbumListItemAdapter(albumListItemList);
//        albumRecyclerview.setLayoutManager(layoutManager);
//        albumRecyclerview.setAdapter(adapter);
//    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initView() {
        final List<Album> albums = MediaUtils.getAlbumList(getActivity());


        for (Album album : albums) {
            albumListItemList.add(new AlbumListItem(album.getAlbum(),album.getAlbumArt(),album.getArtist()));

//            linearLayout.setBackgroundColor(Color.BLACK);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        adapter = new AlbumListItemAdapter(albumListItemList);
        albumRecyclerview.setLayoutManager(layoutManager);
        albumRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new AlbumListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("album_art",albums.get(position).getAlbumArt());
                editor.putString("album_name",albums.get(position).getAlbum());
                editor.putInt("album_id",albums.get(position).getId());
                editor.apply();
                luanchAlbumAct();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void luanchAlbumAct(){
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AlbumListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
