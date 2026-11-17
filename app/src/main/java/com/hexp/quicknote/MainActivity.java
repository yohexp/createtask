package com.hexp.quicknote;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hexp.quicknote.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final NoteList noteList = new NoteList();
    private NoteAdapter mainAdapter;
    private NoteAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applySystemBarInsets();

        noteList.addAll(NoteStore.load(this));
        setUpLists();
        setUpSearch();
        binding.fabAdd.setOnClickListener(view -> openEditor(null));
        refreshMainList();
    }

    private void setUpLists() {
        mainAdapter = new NoteAdapter(true, new NoteAdapter.Listener() {
            @Override
            public void onNoteClick(Note note) {
                openEditor(note);
            }

            @Override
            public void onDeleteClick(Note note) {
                confirmDelete(note);
            }
        });
        binding.notesRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.notesRecycler.setAdapter(mainAdapter);

        searchAdapter = new NoteAdapter(false, new NoteAdapter.Listener() {
            @Override
            public void onNoteClick(Note note) {
                openEditor(note);
            }

            @Override
            public void onDeleteClick(Note note) {
            }
        });
        binding.searchResultsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.searchResultsRecycler.setAdapter(searchAdapter);
    }

    private void setUpSearch() {
        binding.searchView.setupWithSearchBar(binding.searchBar);
        binding.searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                updateSearchResults(text.toString());
            }
        });
        updateSearchResults("");
    }

    private void updateSearchResults(String query) {
        ArrayList<Note> matches = noteList.search(query);
        searchAdapter.submitList(matches);
        binding.searchEmpty.setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void refreshMainList() {
        mainAdapter.submitList(noteList.all());
        binding.emptyState.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
        if (noteList.isEmpty()) {
            binding.toolbar.setSubtitle(R.string.no_notes_subtitle);
        } else {
            binding.toolbar.setSubtitle(getResources().getQuantityString(
                    R.plurals.note_count, noteList.size(), noteList.size()));
        }
        updateSearchResults(binding.searchView.getText().toString());
    }

    private void openEditor(Note note) {
        if (binding.searchView.isShowing()) {
            binding.searchView.hide();
            binding.searchView.setText("");
        }
        NoteEditorSheet.show(this, note, (title, body, category) -> {
            boolean isNew = note == null;
            Note target = isNew
                    ? new Note(title, body, category, System.currentTimeMillis())
                    : note;
            if (isNew) {
                noteList.add(target);
            } else {
                target.setTitle(title);
                target.setBody(body);
                target.setCategory(category);
                target.setUpdatedAt(System.currentTimeMillis());
            }
            NoteStore.save(this, noteList.all());
            refreshMainList();
            Snackbar.make(binding.getRoot(),
                    isNew ? R.string.note_added : R.string.note_updated,
                    Snackbar.LENGTH_SHORT).show();
        });
    }

    private void confirmDelete(Note note) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_note_title)
                .setMessage(R.string.delete_note_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    noteList.remove(note);
                    NoteStore.save(this, noteList.all());
                    refreshMainList();
                    Snackbar.make(binding.getRoot(), R.string.note_deleted,
                            Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }

    private void applySystemBarInsets() {
        int listBottomPadding = dp(104);
        int searchBottomPadding = dp(16);
        int fabBottomMargin = dp(24);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, 0, bars.right, 0);
            binding.appBar.setPadding(0, bars.top, 0, 0);
            binding.notesRecycler.setPadding(
                    binding.notesRecycler.getPaddingLeft(),
                    binding.notesRecycler.getPaddingTop(),
                    binding.notesRecycler.getPaddingRight(),
                    listBottomPadding + bars.bottom);
            binding.searchResultsRecycler.setPadding(
                    binding.searchResultsRecycler.getPaddingLeft(),
                    binding.searchResultsRecycler.getPaddingTop(),
                    binding.searchResultsRecycler.getPaddingRight(),
                    searchBottomPadding + bars.bottom);
            ViewGroup.MarginLayoutParams fabParams =
                    (ViewGroup.MarginLayoutParams) binding.fabAdd.getLayoutParams();
            fabParams.bottomMargin = fabBottomMargin + bars.bottom;
            binding.fabAdd.setLayoutParams(fabParams);
            return insets;
        });

        boolean nightMode = (getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(!nightMode);
        controller.setAppearanceLightNavigationBars(!nightMode);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
