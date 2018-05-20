package com.ksblletba.orangemusic;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.ksblletba.orangemusic.fragment.MusicListFragment;
import com.ksblletba.orangemusic.fragment.PlayListSearchFragment;
import com.ksblletba.orangemusic.manager.PlayManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_tool_bar)
    Toolbar searchToolBar;
    @BindView(R.id.search_tab_layout)
    TabLayout searchTabLayout;
    @BindView(R.id.search_app_bar_layout)
    AppBarLayout searchAppBarLayout;
    @BindView(R.id.search_view_pager)
    ViewPager searchViewPager;
    @BindView(R.id.search_coordinator_layout)
    CoordinatorLayout searchCoordinatorLayout;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private ActionBar actionBar;
    private List<Fragment> fragments = new ArrayList<>();
    private MusicListFragment musicListFragment = new MusicListFragment();
    private PlayListSearchFragment playListSearchFragment = new PlayListSearchFragment();

    public String getSearchKey() {
        return searchKey;
    }

    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(searchToolBar);
        LinearLayout searchViewFrame = (LinearLayout) searchView.findViewById(R.id.search_edit_frame);
        searchKey = getIntent().getStringExtra("search_key");
        searchView.setQuery(searchKey, false);
        ((LinearLayout.LayoutParams) searchViewFrame.getLayoutParams()).leftMargin = 0;
        initViewPager();
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewPager() {
        fragments.add(musicListFragment);
        fragments.add(playListSearchFragment);
        searchViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        searchTabLayout.setupWithViewPager(searchViewPager);
        searchTabLayout.getTabAt(0).setText("单曲");
        searchTabLayout.getTabAt(1).setText("歌单");
    }
}
