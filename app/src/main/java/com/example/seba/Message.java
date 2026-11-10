package com.example.seba;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private final String id;
    private final String type;
    private final String content;
    private final String date;
    private final long timestamp;
    private boolean canceled;

    public Message(String type, String content) {
        this.timestamp = System.currentTimeMillis();
        this.id = String.valueOf(timestamp);
        this.type = type;
        this.content = content;
        this.date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        this.canceled = false;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        String status = canceled ? " [ODWOŁANY]" : "";
        if (content == null || content.trim().isEmpty()) {
            return type + status + " • " + date;
        }
        return type + status + " • " + date + "\n" + content;
    }
}
