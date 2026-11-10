package com.example.seba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends Activity {
    private View layoutRoot;
    private TextView textLatest;
    private TextView buttonStopAlarm;
    private MediaPlayer player;
    private Vibrator vibrator;

    private static String dismissedAlarmId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        layoutRoot = findViewById(R.id.layoutRoot);
        textLatest = findViewById(R.id.textLatest);
        buttonStopAlarm = findViewById(R.id.buttonStopAlarm);

        if (buttonStopAlarm != null) {
            buttonStopAlarm.setOnClickListener(v -> confirmStopAlarm());
        }

        findViewById(R.id.buttonHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        findViewById(R.id.buttonLogout).setOnClickListener(v -> {
            stopPanicSound();
            finish();
        });
    }

    private void confirmStopAlarm() {
        new AlertDialog.Builder(this)
                .setTitle("Wyłączenie alarmu")
                .setMessage("Czy na pewno chcesz wyłączyć dźwięk alarmu?")
                .setNegativeButton("Nie", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Tak, wyłącz", (dialog, which) -> {
                    Message activeAlarm = MessageStore.getActiveAlarm();
                    if (activeAlarm != null) {
                        dismissedAlarmId = activeAlarm.getId();
                    }
                    stopPanicSound();
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUiState();
    }

    private void updateUiState() {
        Message latest = MessageStore.getLatest();
        if (latest == null) {
            textLatest.setText("Brak nowych komunikatów.");
            stopPanicSound();
            return;
        }

        textLatest.setText(latest.toString());

        Message activeAlarm = MessageStore.getActiveAlarm();
        if (activeAlarm != null) {
            if (!activeAlarm.getId().equals(dismissedAlarmId)) {
                startPanicSound();
            } else {
                stopPanicSound();
            }
        } else {
            stopPanicSound();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Sound & vibration continue playing during active alarm until user dismisses or admin cancels it.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPanicSound();
    }

    private void startVibration() {
        try {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                long[] pattern = {0, 800, 300, 800, 300};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
                } else {
                    vibrator.vibrate(pattern, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopVibration() {
        if (vibrator != null) {
            try {
                vibrator.cancel();
            } catch (Exception ignored) {
            }
            vibrator = null;
        }
    }

    private void startPanicSound() {
        if (layoutRoot != null) {
            layoutRoot.setBackgroundColor(Color.parseColor("#FFEBEE"));
        }
        if (buttonStopAlarm != null) {
            buttonStopAlarm.setVisibility(View.VISIBLE);
        }

        // 1. Maximize volume for alarm and media streams to bypass silent mode
        try {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (audioManager != null) {
                int maxAlarmVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxAlarmVol, 0);

                int maxMusicVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusicVol, 0);
            }
        } catch (Exception ignored) {
        }

        // 2. Start continuous vibration
        startVibration();

        if (player != null && player.isPlaying()) {
            return;
        }

        try {
            int resId = R.raw.soundpanic;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build();

            player = MediaPlayer.create(this, resId, audioAttributes, 0);
            if (player == null) {
                player = MediaPlayer.create(this, resId);
            }

            if (player != null) {
                player.setLooping(true);
                player.setVolume(1.0f, 1.0f);
                player.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPanicSound() {
        stopVibration();

        if (player != null) {
            try {
                if (player.isPlaying()) player.stop();
            } catch (IllegalStateException ignored) {
            }
            player.release();
            player = null;
        }
        if (layoutRoot != null) {
            layoutRoot.setBackgroundColor(Color.parseColor("#F3F6FB"));
        }
        if (buttonStopAlarm != null) {
            buttonStopAlarm.setVisibility(View.GONE);
        }
    }
}
