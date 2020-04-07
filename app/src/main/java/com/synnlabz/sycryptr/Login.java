package com.synnlabz.sycryptr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.synnlabz.sycryptr.database.DatabaseHelper;

public class Login extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    EditText Password;
    Button BtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        Password = (EditText)findViewById(R.id.password);
        BtnLogin = (Button)findViewById(R.id.login);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Password.getText().toString();
                String checkLogin = databaseHelper.checkpassword(password);

                if (checkLogin!=null){
                    Toast.makeText(getApplicationContext(),"Login is Successfull",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("password",checkLogin);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Login is Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
