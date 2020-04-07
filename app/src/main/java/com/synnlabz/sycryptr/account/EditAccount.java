package com.synnlabz.sycryptr.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.synnlabz.sycryptr.database.DatabaseHelper;
import com.synnlabz.sycryptr.MainActivity;
import com.synnlabz.sycryptr.other.Model;
import com.synnlabz.sycryptr.R;
import com.synnlabz.sycryptr.other.TypeItem;
import com.synnlabz.sycryptr.adapters.TypeAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import se.simbio.encryption.Encryption;

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

        databaseHelper = new DatabaseHelper(this);

        AccountName = (AppCompatEditText)findViewById(R.id.accountname);
        Username = (AppCompatEditText)findViewById(R.id.accountusername);
        Password = (AppCompatEditText)findViewById(R.id.accountpassword);
        Weblink = (AppCompatEditText)findViewById(R.id.accountlink);

        Save = (AppCompatButton)findViewById(R.id.save);

        spinnerType = (AppCompatSpinner) findViewById(R.id.account_type);
        spinnerType.setPrompt("Select Type");

        initList();

        mAdapter = new TypeAdapter(this, mItemList);
        spinnerType.setAdapter(mAdapter);

        try {
            accountId = getIntent().getLongExtra("ACCOUNT_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);

        Model model = databaseHelper.getModel(accountId);
        AccountName.setText(model.getAccountname());
        Username.setText(encryption.decryptOrNull(model.getAccountusername()));
        Password.setText(encryption.decryptOrNull(model.getAccountpassword()));

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
        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        String accountname = AccountName.getText().toString();
        String accountusername = encryption.encryptOrNull(Username.getText().toString());
        String accountpassword = encryption.encryptOrNull(Password.getText().toString());
        String accountweblink = Weblink.getText().toString();
        int accounttype = spinnerType.getSelectedItemPosition();
        long timestamp = calendar.getTimeInMillis();

        Model updateAccount = new Model(accountname, accountusername, accountpassword, accountweblink, accounttype , timestamp);

        databaseHelper.updateAccountRecord(accountId, this, updateAccount);

        startActivity(new Intent(EditAccount.this, MainActivity.class));
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
