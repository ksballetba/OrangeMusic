package com.ksblletba.orangemusic.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ksblletba.orangemusic.PlayListActivity;
import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.SearchActivity;
import com.ksblletba.orangemusic.adapter.PlayListAdapter;
import com.ksblletba.orangemusic.bean.PlayListItem;
import com.ksblletba.orangemusic.utils.HttpUtils;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListSearchFragment extends Fragment {


    @BindView(R.id.playlistsearch_recyclerview)
    RecyclerView playlistsearchRecyclerview;
    private SearchView searchView;
    private String searchKey;
    private List<PlayListItem> playListItems = new ArrayList<>();
    Unbinder unbinder;

    public PlayListSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list_search, container, false);
        playlistsearchRecyclerview = view.findViewById(R.id.playlistsearch_recyclerview);
        initView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchKey = query;
            Log.d("data", "onQueryTextSubmit: "+searchKey);
            playListItems.clear();
            getPlaylist(searchKey);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private void initView(){
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(onQueryTextListener);
        final SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh);
        playlistsearchRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                getPlaylist(searchKey);
            }
        });
        searchKey = ((SearchActivity) getActivity()).getSearchKey();
        getPlaylist(searchKey);
    }

    private void getPlaylist(String key){
        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setRefreshing(true);
        String Url = "http://music.163.com/api/search/pc/?type=1000&s="+key+"&offset=0&limit=30";
        HttpUtils.sendOkHttpRequestbyPost(Url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final List<PlayListItem> playListItems = NetWorkUtil.getPlaylist(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
                        PlayListAdapter adapter = new PlayListAdapter(playListItems);
                        playlistsearchRecyclerview.setLayoutManager(glm);
                        playlistsearchRecyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new PlayListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final PlayListItem sendPlaylist = playListItems.get(position);
                                Intent intent = new Intent(getActivity(), PlayListActivity.class);
                                intent.putExtra("playlist_title",sendPlaylist.getName());
                                intent.putExtra("playlist_id",sendPlaylist.getId());
                                intent.putExtra("playlist_image",sendPlaylist.getCoverImgUrl());
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                    }
                });
            }
        });
    }
}
