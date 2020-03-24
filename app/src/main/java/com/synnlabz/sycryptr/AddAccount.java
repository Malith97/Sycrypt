package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddAccount extends AppCompatActivity  {

    private AppCompatSpinner spinnerType;
    private ArrayList<TypeItem> mItemList;
    private TypeAdapter mAdapter;

    AppCompatEditText AccountName , Username , Password , Type;

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

        Save = (Button)findViewById(R.id.save);

        spinnerType = (AppCompatSpinner) findViewById(R.id.account_type);
        spinnerType.setPrompt("Select Type");

        mAdapter = new TypeAdapter(this, mItemList);
        spinnerType.setAdapter(mAdapter);

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
                getData();
            }
        });
    }

    private void getData(){
        String accountname = AccountName.getText().toString();
        String accountusername = Username.getText().toString();
        String accountpassword = Password.getText().toString();
        long timestamp = System.currentTimeMillis();

        long id = databaseHelper.insertInfo(
                ""+accountname,
                ""+accountusername,
                ""+accountpassword
        );

        startActivity(new Intent(AddAccount.this,MainActivity.class));
        Toast.makeText(this, "Records Added Successfull", Toast.LENGTH_SHORT).show();
    }

    public void backToHome(View view) {
        Intent intent = new Intent(AddAccount.this, MainActivity.class);
        startActivity(intent);
    }

    private void initList() {
        mItemList = new ArrayList<>();
        mItemList.add(new TypeItem("Other", R.drawable.cat_recent));
        mItemList.add(new TypeItem("Social", R.drawable.cat_social));
        mItemList.add(new TypeItem("Website", R.drawable.cat_website));
        mItemList.add(new TypeItem("Cards", R.drawable.cat_cards));
        mItemList.add(new TypeItem("Device", R.drawable.cat_device));
        mItemList.add(new TypeItem("Mail", R.drawable.cat_mail));
    }
}
