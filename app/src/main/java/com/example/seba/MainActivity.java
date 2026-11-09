package com.example.seba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    private EditText editLogin, editPassword;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editLogin = findViewById(R.id.editLogin);
        editPassword = findViewById(R.id.editPassword);
        textError = findViewById(R.id.textError);

        findViewById(R.id.buttonLogin).setOnClickListener(v -> login());
    }

    private void login() {
        String login = editLogin.getText().toString().trim();
        String password = editPassword.getText().toString();

        if (login.equals("admin") && password.equals("admin")) {
            startActivity(new Intent(this, AdminActivity.class));
        } else if (login.equals("uczen") && password.equals("uczen")) {
            startActivity(new Intent(this, UserActivity.class));
        } else {
            textError.setText("Nieprawidłowy login lub hasło.");
            textError.setVisibility(View.VISIBLE);
        }
    }
}
