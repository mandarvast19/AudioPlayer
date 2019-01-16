package com.noob.audioplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SongsFragment extends Fragment {
    ListView l1;
    String[] items;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_songs,container,false);
        l1=(ListView) view.findViewById(R.id.list_songs);

        runtimePermission();

        return view;
    }

    public void runtimePermission(){
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
}




    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for(File singleFile: files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findSong(singleFile));
            }
            else {
                if (singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")) {

                    arrayList.add(singleFile);

                }

            }
        }
        return arrayList;

    }



    void display(){
        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());
        items =new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){
            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter myAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,items);
        l1.setAdapter(myAdapter);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songname = l1.getItemAtPosition(i).toString();
                startActivity(new Intent(getActivity(),Main2Activity.class)
                .putExtra("songs",mySongs).putExtra("songname",songname)
                .putExtra("pos",i));


            }
        });


    }
}
