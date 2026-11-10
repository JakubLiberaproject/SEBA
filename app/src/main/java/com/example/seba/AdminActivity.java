package com.example.seba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity {

    private TextView btnCancelAlarm;
    private TextView btnCancelWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView alarm = findViewById(R.id.btnAlarm);
        TextView warning = findViewById(R.id.btnOstrzezenie);
        btnCancelAlarm = findViewById(R.id.btnCancelAlarm);
        btnCancelWarning = findViewById(R.id.btnCancelWarning);

        alarm.setOnClickListener(v -> confirmAlarmSending("ALARM"));
        warning.setOnClickListener(v -> showMessageDialog("OSTRZEŻENIE"));

        if (btnCancelAlarm != null) {
            btnCancelAlarm.setOnClickListener(v -> confirmCancelAlarm());
        }

        if (btnCancelWarning != null) {
            btnCancelWarning.setOnClickListener(v -> confirmCancelWarning());
        }

        View buttonHistory = findViewById(R.id.buttonHistory);
        if (buttonHistory != null) {
            buttonHistory.setOnClickListener(v ->
                    startActivity(new Intent(this, HistoryActivity.class)));
        }

        findViewById(R.id.buttonLogout).setOnClickListener(v -> finish());

        updateCancelButtonsVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCancelButtonsVisibility();
    }

    private void updateCancelButtonsVisibility() {
        if (btnCancelAlarm != null) {
            btnCancelAlarm.setVisibility(MessageStore.getActiveAlarm() != null ? View.VISIBLE : View.GONE);
        }
        if (btnCancelWarning != null) {
            btnCancelWarning.setVisibility(MessageStore.getActiveWarning() != null ? View.VISIBLE : View.GONE);
        }
    }

    private void confirmAlarmSending(String type) {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie")
                .setMessage("Czy na pewno chcesz uruchomić " + type + "?")
                .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Tak", (dialog, which) -> {
                    MessageStore.add(type, "");
                    NotificationHelper.sendNotification(this, type, "Uruchomiono alarm ewakuacyjny!");
                    updateCancelButtonsVisibility();
                    Toast.makeText(this, type + " został uruchomiony.",
                            Toast.LENGTH_LONG).show();
                })
                .show();
    }

    private void confirmCancelAlarm() {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie odwołania")
                .setMessage("Czy na pewno chcesz odwołać ALARM?")
                .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Tak, odwołaj", (dialog, which) -> {
                    boolean canceled = MessageStore.cancelActiveAlarm();
                    updateCancelButtonsVisibility();
                    if (canceled) {
                        Toast.makeText(this, "Alarm został odwołany.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Brak aktywnego alarmu do odwołania.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void confirmCancelWarning() {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie odwołania")
                .setMessage("Czy na pewno chcesz odwołać OSTRZEŻENIE?")
                .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Tak, odwołaj", (dialog, which) -> {
                    boolean canceled = MessageStore.cancelActiveWarning();
                    updateCancelButtonsVisibility();
                    if (canceled) {
                        Toast.makeText(this, "Ostrzeżenie zostało odwołane.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Brak aktywnego ostrzeżenia do odwołania.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showMessageDialog(String type) {
        EditText input = new EditText(this);
        input.setHint("Wpisz treść komunikatu");
        input.setMinLines(3);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        int padding = (int) (12 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nowy komunikat: " + type)
                .setView(input)
                .setNegativeButton("Anuluj", (d, w) -> d.dismiss())
                .setPositiveButton("Dalej", null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String content = input.getText().toString().trim();
                    if (content.isEmpty()) {
                        input.setError("Wpisz treść komunikatu");
                    } else {
                        dialog.dismiss();
                        confirmSending(type, content);
                    }
                }));
        dialog.show();
    }

    private void confirmSending(String type, String content) {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie wysłania")
                .setMessage("Czy na pewno chcesz wysłać " + type + "?\n\n" + content)
                .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Wyślij", (dialog, which) -> {
                    MessageStore.add(type, content);
                    NotificationHelper.sendNotification(this, type, content);
                    updateCancelButtonsVisibility();
                    Toast.makeText(this, type + " został wysłany.",
                            Toast.LENGTH_LONG).show();
                })
                .show();
    }
}
