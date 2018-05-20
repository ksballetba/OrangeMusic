package com.ksblletba.orangemusic;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.adapter.AlbumListItemAdapter;
import com.ksblletba.orangemusic.adapter.MusicListItemAdapter;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.MusicListItem;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AlbumActivity extends AppCompatActivity {

    @BindView(R.id.album_act_image)
    ImageView albumActImage;
    @BindView(R.id.ablum_toolbar)
    Toolbar ablumToolbar;
    @BindView(R.id.ablum_toolbar_layout)
    AppBarLayout ablumToolbarLayout;
    @BindView(R.id.ablum_song_list)
    RecyclerView ablumSongListView;
    @BindView(R.id.album_fab)
    FloatingActionButton albumFab;
    private ActionBar actionBar;
    private List<MusicListItem> albumSongList = new ArrayList<>();
    private MusicListItemAdapter adapter;
    private List<Song> albumSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        setSupportActionBar(ablumToolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        albumFab.setOnClickListener(viewOnClickListener);
        AlbumActivityPermissionsDispatcher.initAlbumSongListWithPermissionCheck(this);
        ablumToolbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ablumToolbarLayout.setEnabled(verticalOffset >= 0 ? true : false);
            }
        });
    }



    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.album_fab:
                    Song song = albumSongs.get(0);
                    PlayManager.getInstance(v.getContext()).dispatch(song,"fsgd");
                    Intent intent = new Intent(AlbumActivity.this,PlayDetailActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initAlbumSongList() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String albumArt = pref.getString("album_art",null);
        String albumName = pref.getString("album_name",null);
        int albumId = pref.getInt("album_id",0);
        albumSongs = MediaUtils.getAlbumSongList(this,albumId);
        for (Song song : albumSongs) {
            Uri albumMiniArt = MediaUtils.getAlbumArtUri(song.getAlbumId());
            albumSongList.add(new MusicListItem(song.getTitle(),albumMiniArt,song.getArtist()));
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        adapter = new MusicListItemAdapter(albumSongList);
        ablumSongListView.setLayoutManager(layoutManager);
        ablumSongListView.setAdapter(adapter);
        actionBar.setTitle(albumName);
        adapter.setOnItemClickListener(new MusicListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Song song = albumSongs.get(position);
                PlayManager.getInstance(view.getContext()).dispatch(song,"fdsa");
                Intent intent = new Intent(AlbumActivity.this,PlayDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        Glide.with(this).load(albumArt).into(albumActImage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AlbumActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
