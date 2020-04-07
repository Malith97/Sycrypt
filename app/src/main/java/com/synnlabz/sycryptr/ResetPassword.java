package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.synnlabz.sycryptr.database.DatabaseHelper;
import com.synnlabz.sycryptr.other.Settings;

import de.greenrobot.event.EventBus;

public class ResetPassword extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    EditText Password , Repassword;
    Button Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        databaseHelper = new DatabaseHelper(this);

        Password = (EditText)findViewById(R.id.password);
        Repassword = (EditText)findViewById(R.id.repassword);
        Reset = (Button)findViewById(R.id.reset);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Password.getText().toString();
                String repassword = Repassword.getText().toString();

                if (password.equals("") || repassword.equals("")) {
                    Toast.makeText(getApplicationContext(),"Files are Empty",Toast.LENGTH_SHORT).show();
                }else if(password.equals(repassword)){
                    if (databaseHelper.updatePassword(password)){
                        Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                        startActivity(intent);
                        ResetPassword.this.finish();
                        Toast.makeText(getApplicationContext(),"Password Reset is Successfull",Toast.LENGTH_SHORT).show();
                    }
                }else if(!password.equals(repassword)) {
                    Toast.makeText(getApplicationContext(),"Passwords Not Matched",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Form Validation Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish(){
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResetPassword.this, Settings.class);
        startActivity(intent);
    }
}

