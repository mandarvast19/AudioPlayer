package com.noob.audioplayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
        Intent intent = new Intent(Intent.ACTION_SEND);
        String appName = "org.telegram.messenger";
        intent.setData(Uri.parse("http://telegram.me/mandar19"));
        /*intent.setPackage(appName);
        intent.setPackage("org")*/
        try {
            this.startActivity(Intent.createChooser(intent, "Share with"));
        }
        catch(android.content.ActivityNotFoundException e){
            Toast.makeText(this, "Install Telegram", Toast.LENGTH_SHORT).show();
        }
    }
}
