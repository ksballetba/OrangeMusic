package com.ksblletba.orangemusic.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ksblletba.orangemusic.R;
import com.ksblletba.orangemusic.bean.MusicListItem;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class MusicListItemAdapter extends RecyclerView.Adapter<MusicListItemAdapter.ViewHolder> {

    private Context mContext;
    private List<MusicListItem> mMusicList;
    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView musicAlbumImage;
        TextView musicName;
        TextView artistName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            musicName = view.findViewById(R.id.music_name);
            artistName = view.findViewById(R.id.artist_name);
            musicAlbumImage = view.findViewById(R.id.music_album_image);
        }
    }


    public MusicListItemAdapter(List<MusicListItem> MusicList){
        mMusicList = MusicList;
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
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MusicListItem musicListItem = mMusicList.get(position);
        holder.musicName.setText(musicListItem.getMusicName());
        holder.artistName.setText(musicListItem.getArtistName());
        if (musicListItem.getMusicAlbumImageId()!=null) {
            Glide.with(mContext).load(musicListItem.getMusicAlbumImageId()).into(holder.musicAlbumImage);
        } else if(musicListItem.getMusicAlbumImageURL()!=null){
            Glide.with(mContext).load(musicListItem.getMusicAlbumImageURL()).into(holder.musicAlbumImage);
        }


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
        return mMusicList.size();
    }


}
