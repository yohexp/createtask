package com.hexp.quicknote;

import java.util.ArrayList;
import java.util.Locale;

public class NoteList {
    private final ArrayList<Note> notes = new ArrayList<>();

    public void addAll(ArrayList<Note> loadedNotes) {
        notes.addAll(loadedNotes);
    }

    public void add(Note note) {
        notes.add(0, note);
    }

    public void remove(Note note) {
        notes.remove(note);
    }

    public ArrayList<Note> all() {
        return new ArrayList<>(notes);
    }

    public int size() {
        return notes.size();
    }

    public boolean isEmpty() {
        return notes.isEmpty();
    }

    public ArrayList<Note> search(String query) {
        return filterNotes(notes, query);
    }

    public static ArrayList<Note> filterNotes(ArrayList<Note> notes, String query) {
        ArrayList<Note> results = new ArrayList<>();
        String needle = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            String title = note.getTitle().toLowerCase(Locale.ROOT);
            String body = note.getBody().toLowerCase(Locale.ROOT);
            String category = note.getCategory().toLowerCase(Locale.ROOT);
            if (needle.isEmpty()
                    || title.contains(needle)
                    || body.contains(needle)
                    || category.contains(needle)) {
                results.add(note);
            }
        }
        return results;
    }
}
