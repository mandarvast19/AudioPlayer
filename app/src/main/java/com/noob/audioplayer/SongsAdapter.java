package com.noob.audioplayer;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder>  implements SectionTitleProvider,Filterable {
    private ArrayList<String> sSongNamesList;
    private ArrayList<String> sSongArtistsList;
    private ArrayList<String> sSongTimeList;
    private ArrayList<String> sAlbumArt;
    private ArrayList<String> songNamesFull;
    private Context mycontext;
    private OnItemClickListener sListener;
    private static final String TAG = "SongsAdapter";


    @Override
    public String getSectionTitle(int position) {
        //return getSectionTitle(position);
        return null;
    }

    @Override
    public Filter getFilter() {
        return songFilter;
    }
    private Filter songFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(songNamesFull);

            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.e(TAG, "Filterpattern: "+filterPattern );
                for (String item : songNamesFull){
                    if (item.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                        Log.e(TAG, "performFiltering: "+item );
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sSongNamesList.clear();
            sSongNamesList.addAll((List)results.values);
            for (String a : sSongNamesList){
                Log.e(TAG, "publishResults: "+ a );
            }
            notifyDataSetChanged();
        }
    };

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
        songNamesFull = new ArrayList<>(songNamesList);
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        SongsViewHolder svh = new SongsViewHolder(v, sListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, final int position) {
        String songName = sSongNamesList.get(position);
        String songArtist = sSongArtistsList.get(position);
        String songTime = sSongTimeList.get(position);
        holder.songName.setText(songName);
        holder.songArtist.setText(songArtist);
        holder.songTime.setText(songTime);
        final String albumid = sAlbumArt.get(position);
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
                                            break;
                                        case R.id.addPlaylist:
                                            /*PlaylistsFragment fragment = new PlaylistsFragment();
                                            ((PlaylistsFragment) fragment).addToPlayList(mycontext,albumid,,position);*/
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
