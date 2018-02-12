package idv.ron.texttojson_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private ProgressDialog progressDialog;
    private AsyncTask loginTask;
    private EditText etUserId, etPassword;
    private TextView tvResult;

    class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String userJson = params[1];
            String jsonIn;
            jsonIn = getRemoteData(url, userJson);
            if (jsonIn.trim().isEmpty()) {
                return "";
            }
            Gson gson = new Gson();
            User user = gson.fromJson(jsonIn, User.class);
            return user.getName();
        }

        @Override
        protected void onPostExecute(String name) {
            showResult(name);
            progressDialog.cancel();
        }
    }

    private void showResult(String name) {
        String text;
        if (name.isEmpty()) {
            text = "User name or password is incorrect!";
        } else {
            text = "Hello, " + name;
        }
        tvResult.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        findViews();
    }

    private void findViews() {
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvResult = (TextView) findViewById(R.id.tvResult);
    }

    public void clickLogin(View view) {
        String id = etUserId.getText().toString();
        String password = etPassword.getText().toString();
        User user = new User(id, "", password);
        if (networkConnected()) {
            String url = Common.URL + "/LoginServlet";
            String userJson = new Gson().toJson(user);
            loginTask = new LoginTask().execute(url, userJson);
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
    }

    // check if the device connect to the network
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private String getRemoteData(String url, String outStr) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (loginTask != null) {
            loginTask.cancel(true);
            loginTask = null;
        }
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }
}
