package com.noob.audioplayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ContactsPage extends AppCompatActivity {
    ImageView gmail;
    ImageView telegram;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_page);
        gmail = findViewById(R.id.gmail);
        telegram = findViewById(R.id.telegram);
        Context context = getApplicationContext();
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengmail();
            }
        });

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntentTg();
            }
        });

    }

    private void opengmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/html");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {"mandarvast30@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Report Bugs/Feedback ");
        startActivity(Intent.createChooser(intent,"Send Feedback"));
    }

    private void openIntentTg() {
        
        String appName = "org.telegram.plus";
        boolean isAppInstalled = isAppAvail(getApplicationContext(),appName);
        if(isAppInstalled) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage(appName);
            intent.putExtra(Intent.EXTRA_TEXT,"Hello");
            intent.setData(Uri.parse("http://telegram.me/mandar19"));
            this.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else{
            Toast.makeText(this, "Install Telegram", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isAppAvail(Context context,String appname){
        PackageManager pm = context.getPackageManager();
        try{
            pm.getPackageInfo(appname, PackageManager.GET_ACTIVITIES);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}
