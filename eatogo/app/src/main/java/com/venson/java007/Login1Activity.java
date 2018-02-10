package com.venson.java007;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login1Activity extends AppCompatActivity {
    Button btSt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);
        btSt = findViewById(R.id.btSt);
        btSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.login2);

//                Intent intent = new Intent(Login1Activity.this, login2.class);
//                startActivity(intent);    //startActivity觸發換頁
//                finish();   //換頁後結束此頁

            }
        });

    }
    public void onClick1(View view) {
        setContentView(R.layout.login3);

    }

    public void onClick2(View view) {
        setContentView(R.layout.login4);
    }

    public void onClickB(View view) {
        setContentView(R.layout.login3);
    }

    public void onClickC(View view) {
        setContentView(R.layout.login5);
    }
}