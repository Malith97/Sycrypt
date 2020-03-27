package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddAccount extends AppCompatActivity  {

    private AppCompatSpinner spinnerType;
    private ArrayList<TypeItem> mItemList;
    private TypeAdapter mAdapter;

    AppCompatEditText AccountName , Username , Password , Weblink;

    TextView ViewPass;

    ImageButton GeneratePass , CopyBtn;


    Button Save;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initList();

        databaseHelper = new DatabaseHelper(this);

        AccountName = (AppCompatEditText)findViewById(R.id.accountname);
        Username = (AppCompatEditText)findViewById(R.id.accountusername);
        Password = (AppCompatEditText)findViewById(R.id.accountpassword);
        Weblink = (AppCompatEditText)findViewById(R.id.accountlink);

        GeneratePass = (ImageButton)findViewById(R.id.generateBtn);
        CopyBtn = (ImageButton)findViewById(R.id.copyBtn);

        ViewPass = (TextView)findViewById(R.id.viewPass);


        Save = (Button)findViewById(R.id.save);

        spinnerType = (AppCompatSpinner) findViewById(R.id.account_type);
        spinnerType.setPrompt("Select Type");

        mAdapter = new TypeAdapter(this, mItemList);
        spinnerType.setAdapter(mAdapter);

        CopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clipData = android.content.ClipData.newPlainText("Text Label", ViewPass.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),"Copied from Clipboard!",Toast.LENGTH_SHORT).show();
            }
        });

        GeneratePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPass.setText(generateString(10));
            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TypeItem clickedItem = (TypeItem) parent.getItemAtPosition(position);
                String TypeName = clickedItem.getTypeName();
                Toast.makeText(AddAccount.this, TypeName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public static String dateFromLong(long time) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US);
        return format.format(new Date(time));
    }

    private void saveData(){
        Calendar calendar = Calendar.getInstance();
        String accountname = AccountName.getText().toString();
        String accountusername = Username.getText().toString();
        String accountpassword = Password.getText().toString();
        String accountweblink = Weblink.getText().toString();
        int accounttype = spinnerType.getSelectedItemPosition();
        long timestamp = calendar.getTimeInMillis();



        long id = databaseHelper.insertInfo(
                ""+accountname,
                ""+accountusername,
                ""+accountpassword,
                +accounttype,
                ""+accountweblink,
                ""+timestamp
        );

        startActivity(new Intent(AddAccount.this,MainActivity.class));
        Toast.makeText(this, "Records Added Successfull", Toast.LENGTH_SHORT).show();
    }

    public void backToHome(View view) {
        Intent intent = new Intent(AddAccount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String generateString(int length){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWYXZqwertyuioplkjhgfdsazxcvbnm123456789/*-!@#$%&".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i=0;i<length;i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void initList() {
        mItemList = new ArrayList<>();
        mItemList.add(new TypeItem("Other", R.drawable.ic_other));
        mItemList.add(new TypeItem("Social", R.drawable.ic_social));
        mItemList.add(new TypeItem("Website", R.drawable.ic_web));
        mItemList.add(new TypeItem("Cards", R.drawable.ic_card));
        mItemList.add(new TypeItem("Mail", R.drawable.ic_mail));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddAccount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
