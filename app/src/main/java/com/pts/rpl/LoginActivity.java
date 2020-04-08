package com.pts.rpl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText user, pass;
    private boolean isFormFilled = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.pass);

    }
    public void login(View view) {
        final String username = user.getText().toString();
        final String password = pass.getText().toString();
        isFormFilled = true;

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Harap diisi kolom username/password", Toast.LENGTH_SHORT).show();
            isFormFilled = false;
        }
        if (isFormFilled){
            HashMap<String, String> body = new HashMap<>();
            body.put("username", username);
            body.put("password", password);
            AndroidNetworking.post("http://192.168.1.2/Ulangan/login.php")
                    .addBodyParameter(body)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if ((username.isEmpty()) || (password.isEmpty())) {
                                Toast.makeText(LoginActivity.this, "semuanya harus di isi", Toast.LENGTH_SHORT).show();
                                isFormFilled = false;
                            } else {

                                Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(USERNAME, String.valueOf(username));
                                editor.apply();
                                Intent in = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(in);
                                finish();

                            }


                        }

                        @Override
                        public void onError(ANError error) {
                            Toast.makeText(LoginActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
