package com.noob.audioplayer;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.io.File;
import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder>  implements SectionTitleProvider{
    private ArrayList<String> sSongNamesList;
    private ArrayList<String> sSongArtistsList;
    private ArrayList<String> sSongTimeList;
    private ArrayList<String> sAlbumArt;
    Context mycontext;
    private OnItemClickListener sListener;

    @Override
    public String getSectionTitle(int position) {
        //return getSectionTitle(position);
        return null;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemCLickListener(OnItemClickListener listener){
        sListener = listener;
    }

    public static class SongsViewHolder extends RecyclerView.ViewHolder{
        ImageView simageView;
        TextView songName;
        TextView songArtist;
        TextView songTime;
        ImageView listMenu;

        public SongsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            simageView = itemView.findViewById(R.id.albumArt);
            songName = itemView.findViewById(R.id.songName);
            songArtist = itemView.findViewById(R.id.songArtist);
            songTime = itemView.findViewById(R.id.songTime);
            listMenu = itemView.findViewById(R.id.listMenu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }




    public SongsAdapter(Context scontext,ArrayList<String> songNamesList,ArrayList<String> songArtistsList,ArrayList<String> songTimeList,ArrayList<String> salbumArt){
        sSongNamesList = songNamesList;
        sSongArtistsList=songArtistsList;
        sSongTimeList=songTimeList;
        sAlbumArt = salbumArt;
        mycontext = scontext;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        SongsViewHolder svh = new SongsViewHolder(v, sListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        String songName = sSongNamesList.get(position);
        String songArtist = sSongArtistsList.get(position);
        String songTime = sSongTimeList.get(position);
        holder.songName.setText(songName);
        holder.songArtist.setText(songArtist);
        holder.songTime.setText(songTime);
        String albumid = sAlbumArt.get(position);
        getAlbumArt(mycontext,albumid,holder.simageView);
        try {
            holder.listMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.listMenu:
                            PopupMenu popupMenu = new PopupMenu(mycontext, v);
                            popupMenu.getMenuInflater().inflate(R.menu.listmenu, popupMenu.getMenu());
                            popupMenu.show();
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.delete:
                                            Toast.makeText(mycontext, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            });

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sSongNamesList.size();
    }

    public void getAlbumArt(Context context, String albumPath, ImageView simageView){
        Uri artUri = Uri.parse("content://media/external/audio/albumart");
        Uri imageUrl = Uri.withAppendedPath(artUri,String.valueOf(albumPath));
        if(imageUrl!=null){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .override(100,100)
                    .centerCrop()
                    .error(R.drawable.ic_music_note_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(simageView);
        }
    }
}
