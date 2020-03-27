package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ViewAccount extends AppCompatActivity {

    private TextView AccountName ,Username , Password , Weblink , AccessTime;
    private ImageButton GoLink;

    private DatabaseHelper databaseHelper;
    private Context context;

    private long accountId , time;

    private String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        AccountName = (TextView)findViewById(R.id.accountName);
        Username = (TextView)findViewById(R.id.account_username);
        Password = (TextView)findViewById(R.id.account_password);
        Weblink = (TextView)findViewById(R.id.account_link);
        AccessTime = (TextView)findViewById(R.id.account_access);
        GoLink = (ImageButton)findViewById(R.id.btnlink);

        databaseHelper = new DatabaseHelper(this);

        try {
            //get intent to get account id
            accountId = getIntent().getLongExtra("ACCOUNT_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();

        Model model = databaseHelper.getModel(accountId);
        AccountName.setText(model.getAccountname());
        Username.setText(model.getAccountusername());
        Password.setText(model.getAccountpassword());

        String url = model.getAccountlink();

        if(url.isEmpty()){
            Weblink.setText("URL is not available");
            Weblink.setTextColor(Color.parseColor("#979A9A"));
            Weblink.setTextSize(15);
        }else {
            Weblink.setText(model.getAccountlink());
        }

        GoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.facebook.com/synnlabz"));
                startActivity(intent);
            }
        });

        time = model.getUpdatetimestamp();
        calendar.setTimeInMillis(time);

        Token = +calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" at "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE);
        AccessTime.setText(Token);
    }

    public void backToHome(View view) {
        Intent intent = new Intent(ViewAccount.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewAccount.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
