package com.synnlabz.sycryptr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.synnlabz.sycryptr.account.EditAccount;
import com.synnlabz.sycryptr.account.ViewAccount;
import com.synnlabz.sycryptr.database.DatabaseHelper;
import com.synnlabz.sycryptr.other.Model;

public class LoginDialog extends AppCompatActivity {

    private AppCompatEditText Password;
    private AppCompatButton BtnLogin;

    DatabaseHelper databaseHelper;
    private Context context;

    private long accountId;
    private int option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.login_dialog);

        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        databaseHelper = new DatabaseHelper(this);

        Password = (AppCompatEditText)findViewById(R.id.password);
        BtnLogin = (AppCompatButton)findViewById(R.id.submit);

        try {
            //get intent to get account id
            accountId = getIntent().getLongExtra("ACCOUNT_ID", 1);
            option = getIntent().getIntExtra("OPTION",0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Model model = databaseHelper.getModel(accountId);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = Password.getText().toString();
                String checkLogin = databaseHelper.checkpassword(password);

                if (option==1){
                    if (checkLogin!=null){
                        Intent i = new Intent(getBaseContext(), ViewAccount.class);
                        i.putExtra("ACCOUNT_ID", accountId);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Login is Failed",Toast.LENGTH_SHORT).show();
                    }
                }else if (option==2){
                    if (checkLogin!=null){
                        Intent i = new Intent(getBaseContext(), EditAccount.class);
                        i.putExtra("ACCOUNT_ID", accountId);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(),"Login is Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
    }

    private void goToViewAccount(long accountId){
        Intent i = new Intent(context, ViewAccount.class);
        i.putExtra("ACCOUNT_ID", accountId);
        context.startActivity(i);
    }

}
