package com.venson.create_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "MemberCreateActivity";
    private EditText etUserId;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etEmail;
    private TextView tvMessage;
    private boolean memberIdExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        findViews();
    }

    private void findViews() {
        etUserId = (EditText) findViewById(R.id.tvUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        // check if the id is been used
        etUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String url = Common.URL + "MemberServlet";
                    try {
                        memberIdExist = new MemberIdExistTask().execute(url,
                                etUserId.getText().toString()).get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    // show an error message if the id exists;
                    // otherwise, the error message should be clear
                    if (memberIdExist) {
                        tvMessage.setText(R.string.msg_UserIdExist);
                    } else {
                        tvMessage.setText(null);
                    }
                }
            }
        });
    }

    public void onSubmitClick(View view) {
        String userId = etUserId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String message = "";
        boolean isInputValid = true;
        if (memberIdExist) {
            message += getString(R.string.msg_UserIdExist) + "\n";
            isInputValid = false;
        }
        if (userId.isEmpty()) {
            message += getString(R.string.text_UserId) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (password.isEmpty()) {
            message += getString(R.string.hint_etPassword) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (!confirmPassword.equals(password)) {
            message += getString(R.string.msg_ConfirmPasswordNotSameAsPassword);
            isInputValid = false;
        }
        if (email.isEmpty()) {
            message += getString(R.string.text_etEmail) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        tvMessage.setText(message);
        if (isInputValid) {
            String url = Common.URL + "MemberServlet";
            Member member = new Member(userId, password,  email);
            int count = 0;
            try {
                count = new MemberUpdateTask().execute(url, "insert", member).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (count == 0) {
                tvMessage.setText(R.string.msg_FailCreateAccount);
            } else {
                // user ID and password will be saved in the preferences file
                // and starts MembershipActivity
                // while the user account is created successfully
                SharedPreferences preferences = getSharedPreferences(
                        Common.PREF_FILE, MODE_PRIVATE);
                preferences.edit().putBoolean("login", true)
                        .putString("userId", userId)
                        .putString("password", password).apply();
                Common.showToast(this, R.string.msg_SuccessfullyCreateAccount);
                Intent intent = new Intent(this, CreateActivity.class);
                startActivity(intent);
            }
        }
    }
    }

