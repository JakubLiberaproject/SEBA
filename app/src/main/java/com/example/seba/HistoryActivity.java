package com.example.seba;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView history = findViewById(R.id.textHistory);
        StringBuilder result = new StringBuilder();

        for (Message message : MessageStore.getAll()) {
            if (result.length() > 0) result.append("\n\n────────────────────\n\n");
            result.append(message.toString());
        }

        if (result.length() > 0) history.setText(result.toString());

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }
}
