package com.example.seba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserActivity extends Activity {
    private TextView textLatest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        textLatest = findViewById(R.id.textLatest);

        findViewById(R.id.buttonHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        findViewById(R.id.buttonLogout).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Message latest = MessageStore.getLatest();
        if (latest == null) {
            textLatest.setText("Brak nowych komunikatów.");
        } else {
            textLatest.setText(latest.toString());
        }
    }
}
