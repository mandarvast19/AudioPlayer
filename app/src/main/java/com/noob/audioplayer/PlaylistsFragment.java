package com.noob.audioplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaylistsFragment extends Fragment {
    FloatingActionButton add;
    ImageView error_playlist;
    TextView error_text;
    ListView playlists_list;
    ArrayList<String> names;
    final static String TAG = "Playlists";
    private MainActivity myContext;
    ArrayList<Long> playlistId;
    ArrayList<String> playIdString;
    PlaylistAdapter pad;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlists,container,false);
        add = v.findViewById(R.id.float_button);
        //error_text = v.findViewById(R.id.error_text);
        //error_playlist = v.findViewById(R.id.error_playlist);
        playlists_list = v.findViewById(R.id.list_playlists);
        playlistId = new ArrayList<>();
        playIdString = new ArrayList<>();
        Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        names = new ArrayList<>();
        playlistId =  new ArrayList<>();
        display();
        Context c = getActivity().getApplicationContext();
        ContentResolver resolver = getActivity().getContentResolver();


        Cursor cursor = getPlayList(myUri);
        if (cursor!=null && cursor.moveToFirst()){
            do {
                Long id = cursor.getLong(0);
                String name = cursor.getString(1);
                String playString = String.valueOf(id);
                playIdString.add(playString);
                names.add(name);


                playlistId.add(id);
            }while ( cursor.moveToNext());
        }
        if(names != null) {
            /*error_playlist.setVisibility(View.INVISIBLE);
            error_text.setVisibility(View.INVISIBLE);*/
            PlaylistAdapter pad = new PlaylistAdapter(getActivity(),names,playlistId);
            playlists_list.setAdapter(pad);
            playlists_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getActivity(),DisplayPlaylists.class)
                    .putExtra("pos",position).putStringArrayListExtra("playlistNames",names)
                    .putExtra("playlistId",playlistId));
                }
            });
        }
        else{
            //error_playlist.setVisibility(View.VISIBLE);
            //error_text.setVisibility(View.VISIBLE);
        }


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (MainActivity)activity;
        super.onAttach(activity);
    }

    public void display(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create New Playlist");
        final EditText nameInput = new EditText(getActivity());
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(nameInput);
        nameInput.setHint("Enter name");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            String name_playlist;
            Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name_playlist = nameInput.getText().toString();
                addNewPlaylist(getActivity(),name_playlist,myUri);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(PlaylistsFragment.this).attach(PlaylistsFragment.this).commit();
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
    public Cursor getPlayList( Uri myUri){
        ContentResolver resolver = getActivity().getContentResolver();
        final String id = MediaStore.Audio.Playlists._ID;
        final String name = MediaStore.Audio.Playlists.NAME;
        final String[] columns = {id,name};
        final String criteria = null;
        return resolver.query(myUri,columns,criteria,null,name);

    }
    public void deletePlaylist(Context context,long playlistId,Uri myUri){
        ContentResolver contentResolver = context.getContentResolver();
        String where = MediaStore.Audio.Playlists._ID + "=?";
        String playId = String.valueOf(playlistId);
        Toast.makeText(context, "Playlist Id : Fun"+playId, Toast.LENGTH_SHORT).show();
        String[] whereval = {playId};
        contentResolver.delete(myUri,where,whereval);
    }
}