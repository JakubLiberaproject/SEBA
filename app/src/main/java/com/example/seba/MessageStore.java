package com.example.seba;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageStore {
    private static final List<Message> messages = new ArrayList<>();
    public static final long EXPIRE_TIME_MS = 60 * 60 * 1000L; // 1 hour

    public static Message add(String type, String content) {
        Message message = new Message(type, content);
        messages.add(0, message);
        return message;
    }

    private static void checkExpiredAlerts() {
        long now = System.currentTimeMillis();
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message msg = messages.get(i);
            if (("ALARM".equals(msg.getType()) || "OSTRZEŻENIE".equals(msg.getType())) && !msg.isCanceled()) {
                if (now - msg.getTimestamp() >= EXPIRE_TIME_MS) {
                    msg.setCanceled(true);
                    add("ODWOŁANIE", msg.getType() + " wygasł automatycznie po 1 godzina.");
                }
            }
        }
    }

    public static List<Message> getAll() {
        checkExpiredAlerts();
        return Collections.unmodifiableList(messages);
    }

    public static Message getLatest() {
        checkExpiredAlerts();
        return messages.isEmpty() ? null : messages.get(0);
    }

    public static Message getActiveAlarm() {
        checkExpiredAlerts();
        for (Message msg : messages) {
            if ("ALARM".equals(msg.getType()) && !msg.isCanceled()) {
                return msg;
            }
        }
        return null;
    }

    public static Message getActiveWarning() {
        checkExpiredAlerts();
        for (Message msg : messages) {
            if ("OSTRZEŻENIE".equals(msg.getType()) && !msg.isCanceled()) {
                return msg;
            }
        }
        return null;
    }

    public static boolean cancelActiveAlarm() {
        Message active = getActiveAlarm();
        if (active != null) {
            active.setCanceled(true);
            add("ODWOŁANIE", "Alarm został odwołany przez administratora.");
            return true;
        }
        return false;
    }

    public static boolean cancelActiveWarning() {
        Message active = getActiveWarning();
        if (active != null) {
            active.setCanceled(true);
            add("ODWOŁANIE", "Ostrzeżenie zostało odwołane przez administratora.");
            return true;
        }
        return false;
    }
}
