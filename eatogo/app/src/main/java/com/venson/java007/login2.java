package com.venson.java007;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;


public class login2 extends AppCompatActivity {
    Button btCt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2);
        btCt = findViewById(R.id.btCt);


    }
}
