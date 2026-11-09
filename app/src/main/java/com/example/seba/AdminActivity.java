package com.example.seba;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView alarm = findViewById(R.id.btnAlarm);
        TextView warning = findViewById(R.id.btnOstrzezenie);

        alarm.setOnClickListener(v -> showMessageDialog("ALARM"));
        warning.setOnClickListener(v -> showMessageDialog("OSTRZEŻENIE"));

        findViewById(R.id.buttonLogout).setOnClickListener(v -> finish());
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

        // Przechwytujemy kliknięcie Dalej, aby nie zamykać okna przy pustej treści.
        // Osobna metoda poniżej jest wywoływana po zatwierdzeniu tekstu.
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
                    Toast.makeText(this, type + " zapisany w wersji demo.",
                            Toast.LENGTH_LONG).show();
                })
                .show();
    }
}
