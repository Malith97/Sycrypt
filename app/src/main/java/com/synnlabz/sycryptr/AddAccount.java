package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import se.simbio.encryption.Encryption;

public class AddAccount extends AppCompatActivity  {

    private AppCompatSpinner spinnerType;
    private ArrayList<TypeItem> mItemList;
    private TypeAdapter mAdapter;
    private DatabaseHelper databaseHelper;

    AppCompatEditText AccountName , Username , Password , Weblink;
    AppCompatEditText inputText , inputPassword;
    TextView ViewPass , outputText;
    ImageButton GeneratePass , CopyBtn;
    AppCompatButton  encBtn , decBtn;
    Button Save;
    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initList();

        databaseHelper = new DatabaseHelper(this);

        //inputText = (AppCompatEditText)findViewById(R.id.inputText);
        //inputPassword = (AppCompatEditText)findViewById(R.id.password);
        //outputText = (AppCompatTextView)findViewById(R.id.outputText);
        //encBtn = (AppCompatButton)findViewById(R.id.encBtn);
        //decBtn = (AppCompatButton)findViewById(R.id.decBtn);

        /*
        encBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String Username = inputText.getText().toString();
                    String Password = inputText.getText().toString();
                    outputString = encrypt(Username,Password);
                    outputText.setText(outputString);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = decrypt(outputString,inputPassword.getText().toString());
                    outputText.setText(outputString);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/

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
    private String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private String encrypt(String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = cipher.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;

    }

    public static String dateFromLong(long time) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US);
        return format.format(new Date(time));
    }

    private void saveData(){
        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);

        Calendar calendar = Calendar.getInstance();
        String accountname = AccountName.getText().toString();
        String accountusername = encryption.encryptOrNull(Username.getText().toString());
        String accountpassword = encryption.encryptOrNull(Password.getText().toString());
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
