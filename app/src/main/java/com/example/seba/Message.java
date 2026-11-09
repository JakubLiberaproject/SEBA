package com.example.seba;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    public final String type;
    public final String content;
    public final String date;

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
        this.date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(new Date());
    }

    @Override
    public String toString() {
        return type + " • " + date + "\n" + content;
    }
}
