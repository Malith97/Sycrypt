package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewAccount extends AppCompatActivity {

    private TextView AccountName ,Username , Password , Weblink , AccessTime;

    private DatabaseHelper databaseHelper;
    private Context context;

    private long accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        AccountName = (TextView)findViewById(R.id.accountName);
        Username = (TextView)findViewById(R.id.account_username);
        Password = (TextView)findViewById(R.id.account_password);
        Weblink = (TextView)findViewById(R.id.account_link);
        AccessTime = (TextView)findViewById(R.id.account_access);

        databaseHelper = new DatabaseHelper(this);

        try {
            //get intent to get account id
            accountId = getIntent().getLongExtra("ACCOUNT_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Model model = databaseHelper.getModel(accountId);
        AccountName.setText(model.getAccountname());
        Username.setText(model.getAccountusername());
        Password.setText(model.getAccountpassword());
        Weblink.setText(model.getAccountlink());
        AccessTime.setText(model.getUpdatetimestamp());

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
