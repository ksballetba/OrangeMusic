package com.ksblletba.orangemusic.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.adapter.MusicListItemAdapter;
import com.ksblletba.orangemusic.bean.MusicListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
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
        initRecyclerView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecyclerView() {
        MusicListItem musicListItem = new MusicListItem("如风过境", R.drawable.music, "哎哟蔚蔚");
        for (int i = 0; i < 20; i++) {
            musicListItemList.add(musicListItem);
        }
        Log.d("data", "+++"+musicListItemList.size());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        musicRecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new MusicListItemAdapter(musicListItemList);
        musicRecyclerview.setAdapter(adapter);
    }
}
