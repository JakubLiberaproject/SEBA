package com.example.seba;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Tymczasowy magazyn danych. Po podłączeniu Firebase zastąpimy go bazą Firestore.
public class MessageStore {
    private static final List<Message> messages = new ArrayList<>();

    public static void add(String type, String content) {
        messages.add(0, new Message(type, content));
    }

    public static List<Message> getAll() {
        return Collections.unmodifiableList(messages);
    }

    public static Message getLatest() {
        return messages.isEmpty() ? null : messages.get(0);
    }
}
