package com.noob.audioplayer;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistsFragment extends Fragment {
    FloatingActionButton add;
    ImageView error_playlist;
    TextView error_text;
    ArrayList<String> names;
    final static String TAG = "Playlists";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlists,container,false);
        add = v.findViewById(R.id.float_button);
        error_text = v.findViewById(R.id.error_text);
        error_playlist = v.findViewById(R.id.error_playlist);
        Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        names = new ArrayList<>();
        display();
        for(int i = 0;i<names.size();i++){
            Log.e(TAG, "onCreateView: "+names.get(i) );
        }




        return v;
    }
    public void display(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Create New Playlist");
                final EditText nameInput = new EditText(getActivity());
                nameInput.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(nameInput);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    String name_playlist;
                    public boolean playlistFlag;
                    Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name_playlist = nameInput.getText().toString();
                        //addNewPlaylist(getActivity(),name_playlist,myUri);
                        names.add(name_playlist);
                        Log.e(TAG, "onClick: " +name_playlist );
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


    }



    public void addNewPlaylist(Context context,String playlistName,Uri myUri){

        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Audio.Playlists.NAME,playlistName);
        resolver.insert(myUri,values);
    }

    public void addToPlayList(Context context,String audioId, long playList_id,int pos){
        Uri newUri = MediaStore.Audio.Playlists.Members.getContentUri("external",playList_id);
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,pos);
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,audioId);
        values.put(MediaStore.Audio.Playlists.Members.PLAYLIST_ID,playList_id);
        resolver.insert(newUri,values);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Cursor getPlayList(Context context, Uri myUri){
        ContentResolver resolver = context.getContentResolver();
        final String id = MediaStore.Audio.Playlists._ID;
        final String name = MediaStore.Audio.Playlists.NAME;
        final String[] columns = {id,name};
        final String criteria = null;
        return resolver.query(myUri,columns,criteria,null,name+"ASC");

    }
}