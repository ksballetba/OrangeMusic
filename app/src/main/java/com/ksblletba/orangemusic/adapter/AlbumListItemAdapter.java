package com.ksblletba.orangemusic.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.bean.AlbumListItem;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class AlbumListItemAdapter extends RecyclerView.Adapter<AlbumListItemAdapter.ViewHolder> {

    private Context mContext;
    private List<AlbumListItem> mAlbumListItemList;
    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView albumImage;
        TextView albumMusicName;
        TextView albumArtistName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            albumImage = view.findViewById(R.id.album_image);
            albumMusicName = view.findViewById(R.id.album_music_name);
            albumArtistName = view.findViewById(R.id.album_artist_name);
        }
    }

    public AlbumListItemAdapter(List<AlbumListItem> albumListItemList){
        mAlbumListItemList = albumListItemList;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null) {
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AlbumListItem albumListItem = mAlbumListItemList.get(position);
        holder.albumMusicName.setText(albumListItem.getMusicName());
        holder.albumArtistName.setText(albumListItem.getArtistName());
        Glide.with(mContext).load(albumListItem.getMusicAlbumImageId()).into(holder.albumImage);
        if(mOnItemClickListener!=null){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.cardView,pos);
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.cardView,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumListItemList.size();
    }
}
