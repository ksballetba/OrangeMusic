package com.ksblletba.orangemusic;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.adapter.PlayListAdapter;
import com.ksblletba.orangemusic.bean.Album;
import com.ksblletba.orangemusic.bean.NetworkSong;
import com.ksblletba.orangemusic.bean.PlayListItem;
import com.ksblletba.orangemusic.bean.PlayListSong;
import com.ksblletba.orangemusic.bean.Song;
import com.ksblletba.orangemusic.fragment.AlbumListFragment;
import com.ksblletba.orangemusic.fragment.MusicListFragment;
import com.ksblletba.orangemusic.fragment.PlayListFragment;
import com.ksblletba.orangemusic.manager.PlayManager;
import com.ksblletba.orangemusic.manager.ruler.Rule;
import com.ksblletba.orangemusic.service.PlayService;
import com.ksblletba.orangemusic.utils.HttpUtils;
import com.ksblletba.orangemusic.utils.MediaUtils;
import com.ksblletba.orangemusic.utils.NetWorkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity implements PlayManager.Callback,PlayManager.ProgressCallback {
    @BindView(R.id.main_tool_bar)
    Toolbar mainToolBar;
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_app_bar_layout)
    AppBarLayout mainAppBarLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.main_coordinator_layout)
    CoordinatorLayout mainCoordinatorLayout;
    @BindView(R.id.music_mini_thump)
    ImageView musicMiniThump;
    @BindView(R.id.main_mini_title)
    TextView mainMiniTitle;
    @BindView(R.id.main_mini_artist_album)
    TextView mainMiniArtistAlbum;
    @BindView(R.id.music_mini_info)
    LinearLayout musicMiniInfo;
    @BindView(R.id.music_mini_option_previous)
    Button musicMiniOptionPrevious;
    @BindView(R.id.music_mini_option_play)
    Button musicMiniOptionPlay;
    @BindView(R.id.music_mini_option_next)
    Button musicMiniOptionNext;
    @BindView(R.id.muxic_mini_option)
    LinearLayout muxicMiniOption;
    @BindView(R.id.music_mini_panel)
    RelativeLayout musicMiniPanel;
    @BindView(R.id.nav_view_tab)
    NavigationView navViewTab;
    @BindView(R.id.draw_layout)
    DrawerLayout drawLayout;
    @BindView(R.id.music_mini_cardview)
    CardView musicMiniCardview;
    private ImageView navHeadImage;
    private TextView navHeadSongName;
    private TextView navHeadArtist;
    private int navMenuIndex = 0;
    private ActionBar actionBar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MusicListFragment musicListFragment = new MusicListFragment();
    private AlbumListFragment albumListFragment = new AlbumListFragment();
    private PlayListFragment playListFragment = new PlayListFragment();
    private Song currentSong = null;
    private List<Song> songList;
    private SearchView searchView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private NetworkSong currentNetSong;

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navHeadImage = navViewTab.getHeaderView(0).findViewById(R.id.nav_headimage);
        navHeadSongName = navViewTab.getHeaderView(0).findViewById(R.id.nav_headsongname);
        navHeadArtist = navViewTab.getHeaderView(0).findViewById(R.id.nav_headartist);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivityPermissionsDispatcher.initSongInifoWithPermissionCheck(this);
        setSupportActionBar(mainToolBar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        musicMiniOptionPlay.setOnClickListener(viewClistener);
        musicMiniOptionNext.setOnClickListener(viewClistener);
        musicMiniOptionPrevious.setOnClickListener(viewClistener);
        navViewTab.setNavigationItemSelectedListener(navItemSlistener);
        musicMiniPanel.setOnClickListener(viewClistener);
    }

    @Override
    protected void onResume() {
        if (PlayManager.getInstance(this).isService()) {
            if (PlayManager.getInstance(this).isPlayInNet()) {
                currentNetSong = PlayManager.getInstance(this).getmNetSong();
            } else if (PlayManager.getInstance(this).getCurrentSong()!=null) {
                currentSong = PlayManager.getInstance(this).getCurrentSong();
            }
        }
        PlayManager.getInstance(this).registerCallback(this);
        PlayManager.getInstance(this).registerProgressCallback(this);
        if(!PlayManager.getInstance(this).isPlayInNet())
           MainActivityPermissionsDispatcher.setMusicInfoWithPermissionCheck(this, currentSong);
        onPlayStateChange(PlayManager.getInstance(this).isPlaying());
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(launcherIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayManager.getInstance(this).unregisterCallback(this);

        PlayManager.getInstance(this).unregisterProgressCallback(this);
    }


    @Override
    protected void onDestroy() {
        editor = pref.edit();
        editor.clear();
        editor.putString("song_name", currentSong.getTitle());
        Log.d("data", "###" + currentSong.getTitle());
        editor.apply();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_btn);
        //通过MenuItem得到SearchView
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("发现好音乐");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent =  new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("search_key",query);
                startActivity(intent);
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        LinearLayout searchViewFrame = (LinearLayout) searchView.findViewById(R.id.search_edit_frame);
        ((LinearLayout.LayoutParams)searchViewFrame.getLayoutParams()).leftMargin=0;
        return super.onCreateOptionsMenu(menu);
    }


    //
    private NavigationView.OnNavigationItemSelectedListener navItemSlistener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_music:
                    navMenuIndex = 0;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_music_list:
                    navMenuIndex = 1;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_folder:
                    navMenuIndex = 2;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_favorite:
                    navMenuIndex = 3;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
                case R.id.nav_setting:
                    navMenuIndex = 4;
                    actionBar.setTitle(navViewTab.getMenu().getItem(navMenuIndex).getTitle());
                    break;
            }
            drawLayout.closeDrawers();
            return true;
        }
    };


    private void initViewPager() {

        fragmentList.add(musicListFragment);
        fragmentList.add(albumListFragment);
        fragmentList.add(playListFragment);
        mainViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mainViewPager.setOffscreenPageLimit(3);
        mainTabLayout.setupWithViewPager(mainViewPager);
        mainTabLayout.getTabAt(0).setText("歌曲");
        mainTabLayout.getTabAt(1).setText("专辑");
        mainTabLayout.getTabAt(2).setText("歌单");
    }

    private View.OnClickListener viewClistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.music_mini_panel:
                    launchPlayActivity();
                    break;
                case R.id.music_mini_option_play:
                    if (PlayManager.getInstance(v.getContext()).isService()) {
                        PlayManager pm = PlayManager.getInstance(v.getContext());
                        if(pm.isPlayInNet()){
                            pm.PlayPause();
                        } else {
                            PlayManager.getInstance(v.getContext()).dispatch();
                        }
                    } else
                        PlayManager.getInstance(v.getContext()).dispatch(currentSong, "fasd");
                    Log.d("data", "onClick: " + PlayManager.getInstance(v.getContext()).isPlaying());
                    break;
                case R.id.music_mini_option_next:
                    PlayManager.getInstance(v.getContext()).next();
//                    demo();
                    break;

                case R.id.music_mini_option_previous:
                    PlayManager.getInstance(v.getContext()).previous();
                    break;
            }
        }
    };

    private void launchPlayActivity() {
        Intent intent = new Intent(this, PlayDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("current_song", currentSong);
        Log.d("data", "launchPlayActivity: " + currentSong);
        intent.putExtras(bundle);
        Pair ShareImage = new Pair<>(musicMiniThump, ViewCompat.getTransitionName(musicMiniThump));
        Pair ShareTextMusic = new Pair<>(mainMiniTitle, ViewCompat.getTransitionName(mainMiniTitle));
        Pair ShareTextArtist = new Pair<>(mainMiniArtistAlbum, ViewCompat.getTransitionName(mainMiniArtistAlbum));
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ShareImage, ShareTextMusic, ShareTextArtist);
        ActivityCompat.startActivity(this, intent, transitionActivityOptions.toBundle());
    }

    public void onPlayStateChange(boolean state) {
        if (state) {
            musicMiniOptionPlay.setBackgroundResource(R.drawable.ic_pause_net_24dp);
        } else {
            musicMiniOptionPlay.setBackgroundResource(R.drawable.ic_play_arrow_net_24dp);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void setMusicInfo(Song song) {
        mainMiniTitle.setText(song.getTitle());
        mainMiniArtistAlbum.setText(song.getArtist());
        navHeadSongName.setText(song.getTitle());
        navHeadArtist.setText(song.getArtist());
        Uri currentSongArt = MediaUtils.getAlbumArtUri(song.getAlbumId());
        Glide.with(this).load(currentSongArt).into(musicMiniThump);
        Glide.with(this).load(currentSongArt).into(navHeadImage);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void initSongInifo() {
        initViewPager();
        List<Song> songList = MediaUtils.getAudioList(this);
        PlayManager.getInstance(this).setmCurrentList(songList);
        if (currentSong == null) {
            currentSong = songList.get(0);
        }
        for (Song song : songList) {
            if (song.getTitle().equals(pref.getString("song_name", "")))
                currentSong = song;
        }
        MainActivityPermissionsDispatcher.setMusicInfoWithPermissionCheck(this, currentSong);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onPlayListPrepared(List<Song> songs) {

    }

    @Override
    public void onAlbumListPrepared(List<Album> albums) {

    }

    @Override
    public void onPlayRuleChanged(Rule rule) {

    }

    @Override
    public void onPlayStateChanged(int state, Song song) {
        switch (state) {
            case PlayService.STATE_INITIALIZED:
                MainActivityPermissionsDispatcher.setMusicInfoWithPermissionCheck(this, song);
                break;
            case PlayService.STATE_STARTED:
            case PlayService.STATE_STARTED_NET:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_PAUSED:
            case PlayService.STATE_PAUSED_NET:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_STOPPED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_COMPLETED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_RELEASED:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
            case PlayService.STATE_ERROR:
                onPlayStateChange(PlayManager.getInstance(this).isPlaying());
                break;
        }
    }

    @Override
    public void onProgress(int progress, int duration) {

    }

    @Override
    public void setMusicInfoNet(NetworkSong song) {
        mainMiniTitle.setText(song.getName());
        navHeadSongName.setText(song.getName());
        mainMiniArtistAlbum.setText(song.getArtists().get(0).getName());
        navHeadArtist.setText(song.getArtists().get(0).getName());
        Glide.with(this).load(song.getAlbum().getPicUrl()).into(musicMiniThump);
        Glide.with(this).load(song.getAlbum().getPicUrl()).into(navHeadImage);
    }

    @Override
    public void setMusicInfoNet(PlayListSong song) {
        mainMiniTitle.setText(song.getName());
        navHeadSongName.setText(song.getName());
        mainMiniArtistAlbum.setText(song.getAr().get(0).getName());
        navHeadArtist.setText(song.getAr().get(0).getName());
        Glide.with(this).load(song.getAl().getPicUrl()).into(musicMiniThump);
        Glide.with(this).load(song.getAl().getPicUrl()).into(navHeadImage);
    }

    private void demo(){
        String playlistUrl = "https://api.imjad.cn/cloudmusic/?type=search&search_type=1000&s=民谣&offset=1&limit=10";
        HttpUtils.sendOkHttpRequestbyPost(playlistUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("data", "竟然tm");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final String demo = NetWorkUtil.demo(responseText);
                final List<PlayListItem> playListItems = NetWorkUtil.getPlaylist(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if(playListItems == null)
//                            Log.d("data", "onResponse: ");
//                        else
//                            Log.d("data", "run: "+playListItems.get(0).getName());
                            Log.d("data", "run: "+playListItems.get(8).getName());
                    }
                });

            }
        });
    }
}
