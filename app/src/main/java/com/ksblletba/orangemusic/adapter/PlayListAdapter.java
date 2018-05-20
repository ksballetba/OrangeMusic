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
import com.ksblletba.orangemusic.bean.PlayListItem;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    private Context mContext;
    private List<PlayListItem> mPlayLists;
    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardview;
        ImageView playlistImage;
        TextView playlistName;

        public ViewHolder(View view){
            super(view);
            mCardview = view.findViewById(R.id.playlist_cardview);
            playlistImage = view.findViewById(R.id.playlist_image);
            playlistName = view.findViewById(R.id.playlist_name);
        }
    }

    public PlayListAdapter(List<PlayListItem> playListItems){
        mPlayLists = playListItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.playlist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PlayListItem playListItem = mPlayLists.get(position);
        Glide.with(mContext).load(playListItem.getCoverImgUrl()).into(holder.playlistImage);
        holder.playlistName.setText(playListItem.getName());

        if(mOnItemClickListener!=null){
            holder.mCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(holder.mCardview,pos);
                }
            });
            holder.mCardview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mOnItemClickListener.onItemLongClick(holder.mCardview,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
}
