package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EditAccount extends AppCompatActivity {

    private AppCompatSpinner spinnerType;
    private ArrayList<TypeItem> mItemList;
    private TypeAdapter mAdapter;

    AppCompatEditText AccountName , Username , Password , Weblink;

    AppCompatButton Save;

    private DatabaseHelper databaseHelper;

    int spinnerposition;

    private long accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        initList();

        databaseHelper = new DatabaseHelper(this);

        AccountName = (AppCompatEditText)findViewById(R.id.accountname);
        Username = (AppCompatEditText)findViewById(R.id.accountusername);
        Password = (AppCompatEditText)findViewById(R.id.accountpassword);
        Weblink = (AppCompatEditText)findViewById(R.id.accountlink);

        Save = (AppCompatButton)findViewById(R.id.save);

        spinnerType = (AppCompatSpinner) findViewById(R.id.account_type);
        spinnerType.setPrompt("Select Type");

        mAdapter = new TypeAdapter(this, mItemList);
        spinnerType.setAdapter(mAdapter);

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
        spinnerposition = model.getAccounttype();

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerposition, long id) {
                TypeItem clickedItem = (TypeItem) parent.getItemAtPosition(spinnerposition);
                String TypeName = clickedItem.getTypeName();
                //Toast.makeText(EditAccount.this, TypeName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerType.post(new Runnable() {
            @Override
            public void run() {
                spinnerType.setSelection(spinnerposition);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    private void updateData() {
        Calendar calendar = Calendar.getInstance();
        String accountname = AccountName.getText().toString();
        String accountusername = Username.getText().toString();
        String accountpassword = Password.getText().toString();
        String accountweblink = Weblink.getText().toString();
        int accounttype = spinnerType.getSelectedItemPosition();
        long timestamp = calendar.getTimeInMillis();

        Model updateAccount = new Model(accountname, accountusername, accountpassword, accountweblink, accounttype , timestamp);

        databaseHelper.updateAccountRecord(accountId, this, updateAccount);

        startActivity(new Intent(EditAccount.this,MainActivity.class));
        finish();
        Toast.makeText(this, "Records Added Successfull", Toast.LENGTH_SHORT).show();
    }

    private void initList() {
        mItemList = new ArrayList<>();
        mItemList.add(new TypeItem("Other", R.drawable.ic_other));
        mItemList.add(new TypeItem("Social", R.drawable.ic_social));
        mItemList.add(new TypeItem("Website", R.drawable.ic_web));
        mItemList.add(new TypeItem("Cards", R.drawable.ic_card));
        mItemList.add(new TypeItem("Mail", R.drawable.ic_mail));

    }

    public void backToHome(View view) {
        Intent intent = new Intent(EditAccount.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditAccount.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
