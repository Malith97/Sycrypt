package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

public class Registration extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    EditText Password , Repassword;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //checking if application is opened for the first time
        SharedPreferences preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall","");
        //If application opened for the first time
        if(FirstTime.equals("Yes")){
            Intent intent = new Intent(Registration.this,Login.class);
            startActivity(intent);
        //else
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }

        databaseHelper = new DatabaseHelper(this);

        Password = (EditText)findViewById(R.id.password);
        Repassword = (EditText)findViewById(R.id.repassword);
        Submit = (Button)findViewById(R.id.submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Password.getText().toString();
                String repassword = Repassword.getText().toString();

                if (password.equals("") || repassword.equals("")) {
                    Toast.makeText(getApplicationContext(),"Files are Empty",Toast.LENGTH_SHORT).show();
                }else if(password.equals(repassword)){
                    if (databaseHelper.insert(password)){
                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                        Registration.this.finish();
                        Toast.makeText(getApplicationContext(),"Registration is Successfull",Toast.LENGTH_SHORT).show();
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
    }
}
