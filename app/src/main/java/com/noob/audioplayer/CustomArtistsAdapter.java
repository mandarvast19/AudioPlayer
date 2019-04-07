package com.noob.audioplayer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        return  v;
    }
}

 class Viewholderrtists{
    TextView albumname;
    Viewholderrtists(View v) {
        albumname = (TextView) v.findViewById(R.id.albumname);

    }
}
