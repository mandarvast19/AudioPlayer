package com.noob.audioplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.noob.audioplayer.PlaylistsFragment.TAG;

public class PlaylistAdapter extends ArrayAdapter<String> {
    ArrayList<String> playlistLists;
    ArrayList<Long> playlistIdList;
    Activity context;

    public PlaylistAdapter( Activity context, ArrayList<String>  playlists, ArrayList<Long> playlistId ) {
        super(context,R.layout.playlistsadapter,playlists);
        this.playlistLists = playlists;
        this.playlistIdList = playlistId;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Viewholderplaylist viewHolder = null;
        if (v == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            v = layoutInflater.inflate(R.layout.playlistsadapter, null, true);
            viewHolder = new Viewholderplaylist(v);
            v.setTag(viewHolder);

        } else {
            viewHolder = (Viewholderplaylist) v.getTag();
        }
        viewHolder.playName.setText(playlistLists.get(position).toString());
        try {
            viewHolder.three_dot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.three_dotplaylist:
                            PopupMenu popupMenu = new PopupMenu(context, v);
                            popupMenu.getMenuInflater().inflate(R.menu.playlist_menu, popupMenu.getMenu());
                            popupMenu.show();
                            final long playId = playlistIdList.get(position);
                            final Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.delete_playlist:
                                            Toast.makeText(context, "PlaylistId"+String.valueOf(playId), Toast.LENGTH_SHORT).show();
                                            deletePlaylist(context,playId,myUri);
                                            //Toast.makeText(context, "Playlist Deleted", Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                }
                            });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public void deletePlaylist(Context context,long playlistId,Uri myUri){
        ContentResolver contentResolver = context.getContentResolver();
        String where = MediaStore.Audio.Playlists._ID + "=?";
        String playId = String.valueOf(playlistId);
        String[] whereval = {playId};
        contentResolver.delete(myUri,where,whereval);
    }
}

class Viewholderplaylist{
    TextView playName;
    ImageView three_dot;
    Viewholderplaylist(View v) {
        playName = (TextView) v.findViewById(R.id.playlist_namesList);
        three_dot = (ImageView) v.findViewById(R.id.three_dotplaylist);

    }
}
