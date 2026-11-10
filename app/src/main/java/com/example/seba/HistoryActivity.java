package com.example.seba;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HistoryActivity extends Activity {

    private static final int TAB_ALL = 0;
    private static final int TAB_ALARMS = 1;
    private static final int TAB_WARNINGS = 2;

    private int currentTab = TAB_ALL;

    private TextView tabAll;
    private TextView tabAlarms;
    private TextView tabWarnings;
    private LinearLayout containerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tabAll = findViewById(R.id.tabAll);
        tabAlarms = findViewById(R.id.tabAlarms);
        tabWarnings = findViewById(R.id.tabWarnings);
        containerHistory = findViewById(R.id.containerHistory);

        tabAll.setOnClickListener(v -> selectTab(TAB_ALL));
        tabAlarms.setOnClickListener(v -> selectTab(TAB_ALARMS));
        tabWarnings.setOnClickListener(v -> selectTab(TAB_WARNINGS));

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        renderHistory();
    }

    private void selectTab(int tab) {
        currentTab = tab;
        updateTabStyles();
        renderHistory();
    }

    private void updateTabStyles() {
        tabAll.setBackgroundResource(currentTab == TAB_ALL ? R.drawable.bg_primary_button : R.drawable.bg_tab_inactive);
        tabAll.setTextColor(currentTab == TAB_ALL ? Color.WHITE : Color.parseColor("#344054"));

        tabAlarms.setBackgroundResource(currentTab == TAB_ALARMS ? R.drawable.bg_primary_button : R.drawable.bg_tab_inactive);
        tabAlarms.setTextColor(currentTab == TAB_ALARMS ? Color.WHITE : Color.parseColor("#344054"));

        tabWarnings.setBackgroundResource(currentTab == TAB_WARNINGS ? R.drawable.bg_primary_button : R.drawable.bg_tab_inactive);
        tabWarnings.setTextColor(currentTab == TAB_WARNINGS ? Color.WHITE : Color.parseColor("#344054"));
    }

    private void renderHistory() {
        containerHistory.removeAllViews();
        List<Message> allMessages = MessageStore.getAll();

        int count = 0;
        float density = getResources().getDisplayMetrics().density;

        for (Message message : allMessages) {
            if (currentTab == TAB_ALARMS && !"ALARM".equals(message.getType())) {
                continue;
            }
            if (currentTab == TAB_WARNINGS && !"OSTRZEŻENIE".equals(message.getType())) {
                continue;
            }

            count++;
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundResource(R.drawable.bg_card);

            int padding = (int) (16 * density);
            card.setPadding(padding, padding, padding, padding);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, (int) (12 * density));
            card.setLayoutParams(params);

            // Header layout
            LinearLayout header = new LinearLayout(this);
            header.setOrientation(LinearLayout.HORIZONTAL);
            header.setGravity(Gravity.CENTER_VERTICAL);

            TextView typeText = new TextView(this);
            typeText.setTypeface(null, Typeface.BOLD);
            typeText.setTextSize(14);

            String typeStr = message.getType();
            if ("ALARM".equals(typeStr)) {
                typeText.setText("🚨 ALARM");
                typeText.setTextColor(Color.parseColor("#B42318"));
            } else if ("OSTRZEŻENIE".equals(typeStr)) {
                typeText.setText("⚠️ OSTRZEŻENIE");
                typeText.setTextColor(Color.parseColor("#C77A08"));
            } else if ("ODWOŁANIE".equals(typeStr)) {
                typeText.setText("✅ ODWOŁANIE");
                typeText.setTextColor(Color.parseColor("#15803D"));
            } else {
                typeText.setText(typeStr);
                typeText.setTextColor(Color.parseColor("#2563EB"));
            }

            if (message.isCanceled()) {
                typeText.setText(typeText.getText() + " [ODWOŁANY]");
            }

            LinearLayout.LayoutParams typeParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            typeText.setLayoutParams(typeParams);

            TextView dateText = new TextView(this);
            dateText.setText(message.getDate());
            dateText.setTextSize(12);
            dateText.setTextColor(Color.parseColor("#718096"));

            header.addView(typeText);
            header.addView(dateText);
            card.addView(header);

            if (message.getContent() != null && !message.getContent().trim().isEmpty()) {
                TextView contentText = new TextView(this);
                contentText.setText(message.getContent());
                contentText.setTextSize(14);
                contentText.setTextColor(Color.parseColor("#10233F"));

                LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                contentParams.setMargins(0, (int) (8 * density), 0, 0);
                contentText.setLayoutParams(contentParams);

                card.addView(contentText);
            }

            containerHistory.addView(card);
        }

        if (count == 0) {
            TextView empty = new TextView(this);
            empty.setText("Brak komunikatów w tej kategoria.");
            empty.setTextSize(15);
            empty.setTextColor(Color.parseColor("#718096"));
            empty.setPadding(0, (int) (20 * density), 0, 0);
            containerHistory.addView(empty);
        }
    }
}
