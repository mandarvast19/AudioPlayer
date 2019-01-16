package com.noob.audioplayer;

import android.Manifest;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

BottomNavigationView b1;
Fragment f1;
Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=(BottomNavigationView)findViewById(R.id.botttom_nav);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        display();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,new SongsFragment()).commit();
    }

    public void display() {


        b1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()){
                    case R.id.songs:
                        selectedFragment=new SongsFragment();
                        break;
                    case R.id.albums:
                        selectedFragment=new AlbumFragment();
                        break;
                    case R.id.artists:
                        selectedFragment=new ArtistsFragment();
                        break;
                    case R.id.playlists:
                        selectedFragment=new PlaylistsFragment();
                        break;
                }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,selectedFragment).commit();

                return true;
            }
        });

    }


}
