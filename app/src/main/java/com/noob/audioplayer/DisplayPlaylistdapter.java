package com.noob.audioplayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.concurrent.TimeUnit;

public class DisplayPlaylistdapter extends RecyclerView.Adapter<DisplayPlaylistdapter.PlaylistViewHolder>  {
    private ArrayList<String> songsNamePlaylist;
    private ArrayList<String> songsArtistsPlaylist;
    private ArrayList<String> sSongTimeList;
    private ArrayList<String> sAlbumArt;
    private ArrayList<String> songNamesFull;
    private ArrayList<String> audioIdList;
    private Context mycontext;
    private OnItemClickListener sListener;
    private static final String TAG = "DisplayPlaylistAdapter";
    ArrayAdapter<String> ad;





    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemCLickListener(OnItemClickListener listener){
        sListener = listener;
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder{
        TextView songsName;
        TextView songsArtist;
        TextView songsTime;
        ImageView albumrt;
        ImageView listmenuPlaylist;

        public PlaylistViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            songsName = itemView.findViewById(R.id.playlistName);
            songsArtist = itemView.findViewById(R.id.songArtistPlaylist);
            songsTime = itemView.findViewById(R.id.songTimePlaylist);
            albumrt = itemView.findViewById(R.id.albumArtPlaylist);
            listmenuPlaylist = itemView.findViewById(R.id.listMenuPlaylist);
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




    public DisplayPlaylistdapter(Context scontext,ArrayList<String> songNamesList,ArrayList<String> songArtistsList
            ,ArrayList<String> albumArtList, ArrayList<String> songTimeList){
        songsNamePlaylist = songNamesList;
        songsArtistsPlaylist=songArtistsList;
        mycontext = scontext;
        sAlbumArt = albumArtList;
        sSongTimeList=songTimeList;


        /*audioIdList = sAudioIdList;
        songNamesFull = new ArrayList<>(songNamesList);*/
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.displaylistsadapter,parent,false);
        PlaylistViewHolder svh = new PlaylistViewHolder(v, sListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, final int position) {
        String songName = songsNamePlaylist.get(position);
        String songArtist = songsArtistsPlaylist.get(position);
        String songTime = sSongTimeList.get(position);
        holder.songsName.setText(songName);
        holder.songsArtist.setText(songArtist);
        final String albumid = sAlbumArt.get(position);
        //final String audioId = audioIdList.get(position);
        getAlbumArt(mycontext,albumid,holder.albumrt);
        holder.songsTime.setText(songTime);
        /*long millisecs = Long.valueOf(songTime);
        long hrs = TimeUnit.MILLISECONDS.toHours(millisecs);
        long mins = TimeUnit.MILLISECONDS.toMinutes(millisecs);
        long secs = TimeUnit.MILLISECONDS.toSeconds(millisecs);
        if (hrs>0) {
            @SuppressLint("DefaultLocale")
            String hms = String.format("%02d:%02d:%02d",
                    hrs,
                    mins-hrs,
                    secs-mins);
            holder.songsTime.setText(hms);
        }
        else{
            @SuppressLint("DefaultLocale")
            String hms = String.format("%02d:%02d",
                    mins-hrs,
                    secs-mins);
            holder.songsTime.setText(hms);
        }*/

    }

    private void deleteAudio(String audioId) {
        Long audio_id = Long.valueOf(audioId);
        Uri myUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audio_id);
        ContentResolver contentResolver = mycontext.getContentResolver();
        contentResolver.delete(myUri,null,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showDialog(final String audioId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mycontext);
        builder.setTitle("Add to Playlist");
        final ArrayList<String> playNames = new ArrayList<>();
        final ArrayList<Long> playIdList = new ArrayList<>();
        ListView list_dialog = new ListView(mycontext);
        list_dialog.setDividerHeight(0);
        builder.setView(list_dialog);
        playNames.add("Create New Playlist");
        long i = 123456;
        playIdList.add(i);
        Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor co = getPlayList(myUri);
        if(co != null && co.moveToFirst()){
            do {
                String name = co.getString(1);
                Long playid = co.getLong(0);
                playNames.add(name);
                playIdList.add(playid);
            }
            while (co.moveToNext());
        }
        final AlertDialog dialog = builder.create();
        list_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaylistsFragment playlistsFragment = new PlaylistsFragment();
                long playId = playIdList.get(position);
                if(playId == 123456){
                    showDialogCreatePlaylist(audioId);
                }
                else {
                    ((PlaylistsFragment) playlistsFragment).addToPlayList(mycontext, audioId, playId, position);
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        ArrayAdapter<String> pada = new ArrayAdapter<>(mycontext,android.R.layout.simple_list_item_1,playNames);
        list_dialog.setAdapter(pada);
    }

    private void showDialogCreatePlaylist(final String audioId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mycontext);
        builder.setTitle("Create New Playlist");
        final EditText nameInput = new EditText(mycontext);
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(nameInput);
        nameInput.setHint("Enter name");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            String name_playlist;
            Uri myUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name_playlist = nameInput.getText().toString();
                PlaylistsFragment playlistsFragment = new PlaylistsFragment();
                ((PlaylistsFragment)playlistsFragment).addNewPlaylist(mycontext, name_playlist, myUri);
                showDialog(audioId);
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

    @Override
    public int getItemCount() {
        return songsNamePlaylist.size();
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
    public Cursor getPlayList( Uri myUri){
        ContentResolver resolver = mycontext.getContentResolver();
        final String id = MediaStore.Audio.Playlists._ID;
        final String name = MediaStore.Audio.Playlists.NAME;
        final String[] columns = {id,name};
        final String criteria = null;
        return resolver.query(myUri,columns,criteria,null,name);

    }
}
