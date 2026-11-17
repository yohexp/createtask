package com.hexp.quicknote;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class NoteStore {
    private static final String TAG = "NoteStore";
    private static final String PREFS_NAME = "quicknote_store";
    private static final String KEY_NOTES = "notes";

    private NoteStore() {
    }

    public static ArrayList<Note> load(Context context) {
        ArrayList<Note> notes = new ArrayList<>();
        SharedPreferences preferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String raw = preferences.getString(KEY_NOTES, "[]");
        try {
            JSONArray array = new JSONArray(raw);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                notes.add(new Note(
                        object.optString("title"),
                        object.optString("body"),
                        object.optString("category", Note.CATEGORY_PERSONAL),
                        object.optLong("updatedAt", System.currentTimeMillis())
                ));
            }
        } catch (JSONException exception) {
            Log.e(TAG, "Unable to read saved notes", exception);
        }
        return notes;
    }

    public static void save(Context context, ArrayList<Note> notes) {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                JSONObject object = new JSONObject();
                object.put("title", note.getTitle());
                object.put("body", note.getBody());
                object.put("category", note.getCategory());
                object.put("updatedAt", note.getUpdatedAt());
                array.put(object);
            }
        } catch (JSONException exception) {
            Log.e(TAG, "Unable to serialize notes", exception);
            return;
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_NOTES, array.toString())
                .apply();
    }
}
