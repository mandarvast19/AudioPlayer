package com.noob.audioplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String>  {
    ArrayList<String> songsNameList;
    ArrayList<String> songArtists;
    ArrayList<String> songTimeList;
    ArrayList<String> albumPath;
    ArrayList<String> songName;

    Activity context;


    public CustomAdapter( Activity context, ArrayList<String> songsNameList,ArrayList<String> songArtists,ArrayList<String> songTimeList, ArrayList<String> albumPath) {
        super(context, R.layout.song_item,songsNameList);
        this.context = context;
        this.songsNameList = songsNameList;
        this.songArtists = songArtists;
        this.songTimeList = songTimeList;
        this.albumPath = albumPath;
        songName = new ArrayList<>(songsNameList);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder = null;
        if(v==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            v = layoutInflater.inflate(R.layout.song_item,null,true);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) v.getTag();

        }
        viewHolder.songName.setText(songsNameList.get(position).toString());
        viewHolder.songArtist.setText(songArtists.get(position).toString());
        viewHolder.songTime.setText(songTimeList.get(position).toString());
        String album = albumPath.get(position).toString();
        if(album.equals(null)){
            viewHolder.albumArt.setImageResource(R.drawable.ic_music_note_black_24dp);
            viewHolder.albumArt.setMaxHeight(30);
            viewHolder.albumArt.setMaxWidth(30);
        }
        else {
            Context context = getContext();
            getAlbumArt(context,viewHolder,album);
        }
        //viewHolder.albumArt.setImageResource(R.mipmap.ic_launcher);
        try {
            viewHolder.listMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()){
                        case R.id.listMenu:
                            PopupMenu popupMenu = new PopupMenu(context,v);
                            popupMenu.getMenuInflater().inflate(R.menu.listmenu,popupMenu.getMenu());
                            popupMenu.show();
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch(item.getItemId()){
                                        case R.id.delete:
                                            Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
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
        return v;



    }

    public void getAlbumArt(Context context,ViewHolder viewHolder,String albumPath){
        Uri artUri = Uri.parse("content://media/external/audio/albumart");
        Uri imageUrl = Uri.withAppendedPath(artUri,String.valueOf(albumPath));
        if(imageUrl!=null){
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .override(20)
                    .centerCrop()
                    .error(R.drawable.ic_music_note_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.albumArt);
        }
    }

    /*@Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }*/

    class ViewHolder {
        TextView songName;
        TextView songArtist;
        TextView songTime;
        ImageView albumArt;
        ImageView listMenu;
        ViewHolder(View v){
            songName = (TextView) v.findViewById(R.id.songName);
            songArtist = (TextView) v.findViewById(R.id.songArtist);
            songTime = (TextView) v.findViewById(R.id.songTime);
            albumArt = (ImageView) v.findViewById(R.id.albumArt);
            listMenu = (ImageView) v.findViewById(R.id.listMenu);
        }
    }
}
