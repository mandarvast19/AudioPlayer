package com.noob.audioplayer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class CustomArtistsAdapter extends ArrayAdapter<String> {
    ArrayList<String> albumArt;
    ArrayList<String> albumName;
    Activity context;

    public CustomArtistsAdapter( Activity context, ArrayList<String>  albumName ) {
        super(context,R.layout.songartists,albumName);
        //this.albumArt = albumArt;
        this.albumName = albumName;
        this.context = context;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View v = convertView;
        Viewholderrtists viewHolder = null;
        if(v == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            v = layoutInflater.inflate(R.layout.songartists,null,true);
            viewHolder = new Viewholderrtists(v);
            v.setTag(viewHolder);

        }
        else{
            viewHolder = (Viewholderrtists) v.getTag();
        }
        viewHolder.albumname.setText(albumName.get(position).toString());
        //getAlbumArt(context,albumArt.get(position).toString(),viewHolder.artistart);
        return  v;
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

 class Viewholderrtists{
    TextView albumname;
    ImageView artistart;
    Viewholderrtists(View v) {
        albumname = (TextView) v.findViewById(R.id.albumname);
        artistart = (ImageView) v.findViewById(R.id.artistart);

    }
}
