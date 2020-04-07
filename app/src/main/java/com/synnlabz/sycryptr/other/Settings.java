package com.synnlabz.sycryptr.other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.synnlabz.sycryptr.MainActivity;
import com.synnlabz.sycryptr.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Settings extends AppCompatActivity {

    AppCompatButton ResetPassword , ExportDB , About;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ResetPassword = (AppCompatButton)findViewById(R.id.resetPassword);
        ExportDB = (AppCompatButton)findViewById(R.id.exportDatabase);
        About = (AppCompatButton)findViewById(R.id.about);

        ExportDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB();
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, com.synnlabz.sycryptr.ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, About.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void backToHome(View view) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void exportDB(){
        String SAMPLE_DB_NAME = "Sycrypter.db";
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.synnlabz.sycryptr" +"/databases/"+SAMPLE_DB_NAME;
        String backupDBPath = SAMPLE_DB_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Your Database is Exported !!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void importDB(){
        String dir=Environment.getExternalStorageDirectory().getAbsolutePath();
        File sd = new File(dir);
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String backupDBPath = "/data/com.synnlabz.sycryptr/databases/Sycrypter.db";
        String currentDBPath = "Sycrypter.db";
        File currentDB = new File(sd, currentDBPath);
        File backupDB = new File(data, backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Your Database is Imported !!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
