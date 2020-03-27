package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class About extends AppCompatActivity {

    ImageButton Back;
    AppCompatButton Share , Contact , Page , Github;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Back = (ImageButton)findViewById(R.id.backBtn);

        Share = (AppCompatButton)findViewById(R.id.share);
        Contact = (AppCompatButton)findViewById(R.id.contact);
        Page = (AppCompatButton)findViewById(R.id.visitpage);
        Github = (AppCompatButton)findViewById(R.id.githubfork);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAplication();
                Toast.makeText(getApplicationContext(), "Thanks for Sharing ", Toast.LENGTH_LONG).show();
            }
        });

        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = "Regarding your App";
                String message = "Thanks for the App";
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","mileperuma@gmail.com",null));
                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent.putExtra(Intent.EXTRA_TEXT,message);
                startActivity(Intent.createChooser(intent,"Choose an Email Client"));
            }
        });

        Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW",Uri.parse("http://www.facebook.com/synnlabz"));
                startActivity(intent);
            }
        });

        Github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW",Uri.parse("http://www.github.com/Malith97"));
                startActivity(intent);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(About.this, Settings.class);
        startActivity(intent);
        finish();
    }

    public void shareAplication(){
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent (Intent.ACTION_SEND);

        intent.setType("*/*");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent,"Share Sycryptr via"));
    }
}
