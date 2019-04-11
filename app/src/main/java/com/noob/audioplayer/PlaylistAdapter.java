package com.noob.audioplayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter<String> {
    ArrayList<String> playlistNames;
    Activity context;

    public PlaylistAdapter(Activity context, ArrayList<String> playlistNamesList) {
        super(context, R.layout.playlistsadapter,playlistNamesList);
        this.context = context;
        this.playlistNames = playlistNamesList;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View v = convertView;
        Viewholderplaylists viewholderplaylists = null;
        if (v == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            layoutInflater.inflate(R.layout.playlistsadapter,null,false);
            viewholderplaylists = new Viewholderplaylists(v);
            v.setTag(viewholderplaylists);
        }
        else {
            viewholderplaylists = (Viewholderplaylists) v.getTag();
        }
        viewholderplaylists.playlistName.setText(playlistNames.get(position));

        return v;
    }
    class Viewholderplaylists{
        TextView playlistName;
         public Viewholderplaylists(View v){
            playlistName = v.findViewById(R.id.playlist_name);
        }
    }
}
