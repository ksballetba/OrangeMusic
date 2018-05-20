package com.ksblletba.orangemusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ksblletba.orangemusic.adapter.MusicListItemAdapter;
import com.ksblletba.orangemusic.bean.MusicListItem;
import com.ksblletba.orangemusic.bean.NetworkSong;
import com.ksblletba.orangemusic.bean.PlayListSong;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.utils.HttpUtils;
import com.ksblletba.orangemusic.utils.MediaUtils;
import com.ksblletba.orangemusic.utils.NetWorkUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PlayListActivity extends AppCompatActivity {


    @BindView(R.id.playlist_act_image)
    ImageView playlistActImage;
    @BindView(R.id.playlist_toolbar)
    Toolbar playlistToolbar;
    @BindView(R.id.playlist_toolbar_layout)
    AppBarLayout playlistToolbarLayout;
    @BindView(R.id.playlist_song_list)
    RecyclerView playlistSongList;
    @BindView(R.id.playlist_fab)
    FloatingActionButton playlistFab;
    @BindView(R.id.playlist_swipe)
    SwipeRefreshLayout playlistSwipe;
    private Long playlistId;
    private String playlistTitle;
    private String playlistImage;
    private Bitmap playlistBitmapd;
    private MusicListItemAdapter adapter;
    private List<MusicListItem> musicListItems = new ArrayList<>();
    private SharedPreferences pref;
    private Song currentSong =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        ButterKnife.bind(this);
        setSupportActionBar(playlistToolbar);
        ActionBar actionBar = getSupportActionBar();
        playlistTitle = getIntent().getStringExtra("playlist_title");
        playlistImage = getIntent().getStringExtra("playlist_image");
        playlistId = getIntent().getLongExtra("playlist_id", 0);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(playlistTitle);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        playlistFab.setOnClickListener(viewOnClickListener);
        Glide.with(this).load(playlistImage).into(playlistActImage);
        initView(playlistId);
        playlistSwipe.setColorSchemeResources(R.color.colorAccent);
        playlistSongList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                playlistSwipe.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        playlistSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView(playlistId);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void BulrImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap playlistBitmapc = MediaUtils.getBitmapFromURL(playlistImage);
                playlistBitmapd = MediaUtils.rsBlur(getApplicationContext(),playlistBitmapc,40);
                Log.d("data", "onCreate: "+playlistBitmapd);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Glide.with(PlayListActivity.this).load(playlistBitmapd).into(playlistActImage);
                        playlistActImage.setImageBitmap(playlistBitmapd);
                    }
                });
            }
        }).start();

    }

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playlist_fab:
                    break;
            }
        }
    };

    private void initView(Long playlistId) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        List<Song> songList = MediaUtils.getAudioList(this);
        if (currentSong == null) {
            currentSong = songList.get(0);
        }
        for (Song song : songList) {
            if (song.getTitle().equals(pref.getString("song_name", "")))
                currentSong = song;
        }
        String sendUrl = "https://api.imjad.cn/cloudmusic/?type=playlist&id=" + playlistId;
        playlistSwipe.setRefreshing(true);
        HttpUtils.sendOkHttpRequestbyGet(sendUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<PlayListSong> songs = NetWorkUtil.getNetSongsByPlaylist(responseText);
                for (PlayListSong song : songs) {
                    musicListItems.add(new MusicListItem(song.getName(), song.getAl().getPicUrl(), song.getAr().get(0).getName()));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridLayoutManager layoutManager = new GridLayoutManager(PlayListActivity.this, 1);
                        adapter = new MusicListItemAdapter(musicListItems);
                        playlistSongList.setLayoutManager(layoutManager);
                        playlistSongList.setAdapter(adapter);
                        playlistSwipe.setRefreshing(false);
                        adapter.setOnItemClickListener(new MusicListItemAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (PlayManager.getInstance(PlayListActivity.this).isService()) {
                                final PlayListSong networkSong = songs.get(position);
                                String playAdress = "https://api.imjad.cn/cloudmusic/?type=song&id=" + networkSong.getId() + "&br=128000";
                                HttpUtils.sendOkHttpRequestbyGet(playAdress, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String respt = response.body().string();
                                        String playAdress = NetWorkUtil.getPlayAdress(respt);
                                        PlayManager.getInstance(PlayListActivity.this).playNetSong(networkSong, playAdress);
                                        Log.d("data", "onClick: " + PlayManager.getInstance(PlayListActivity.this).getmState());
                                        Log.d("data", "" + networkSong.getName());
                                    }
                                });
                                Intent intent = new Intent(PlayListActivity.this, PlayDetailActivity.class);
                                Log.d("data", "+++" + PlayManager.getInstance(PlayListActivity.this).getmNetSong());
                                startActivity(intent);
                            } else {
                                PlayManager.getInstance(PlayListActivity.this).dispatch(currentSong,"   ");
                            }
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                PlayManager.getInstance(PlayListActivity.this).dispatch(currentSong," ");
                            }
                        });
                    }
                });

            }
        });
    }
}

