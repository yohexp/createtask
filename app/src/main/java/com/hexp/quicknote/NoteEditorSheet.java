package com.hexp.quicknote;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.FrameLayout;

import com.hexp.quicknote.databinding.SheetNoteEditorBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.ChipGroup;

public final class NoteEditorSheet {
    public interface Listener {
        void onSave(String title, String body, String category);
    }

    private NoteEditorSheet() {
    }

    public static void show(Activity activity, Note existingNote, Listener listener) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        SheetNoteEditorBinding binding =
                SheetNoteEditorBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        boolean editing = existingNote != null;
        binding.sheetTitle.setText(editing ? R.string.edit_note : R.string.new_note);

        if (editing) {
            binding.titleEditText.setText(existingNote.getTitle());
            binding.bodyEditText.setText(existingNote.getBody());
            checkCategory(binding.categoryGroup, existingNote.getCategory());
            binding.titleEditText.setSelection(binding.titleEditText.length());
        } else {
            binding.chipPersonal.setChecked(true);
        }

        binding.titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                binding.titleLayout.setError(null);
            }
        });

        binding.closeButton.setOnClickListener(view -> dialog.dismiss());
        binding.cancelButton.setOnClickListener(view -> dialog.dismiss());
        binding.saveButton.setOnClickListener(view -> {
            String title = textOf(binding.titleEditText.getText());
            if (title.isEmpty()) {
                binding.titleLayout.setError(activity.getString(R.string.title_required));
                binding.titleEditText.requestFocus();
                return;
            }
            binding.titleLayout.setError(null);
            String body = textOf(binding.bodyEditText.getText());
            String category = selectedCategory(binding.categoryGroup);
            listener.onSave(title, body, category);
            dialog.dismiss();
        });

        dialog.setOnShowListener(ignored -> {
            FrameLayout sheet = dialog.findViewById(
                    com.google.android.material.R.id.design_bottom_sheet);
            if (sheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(sheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });

        dialog.show();
        binding.titleEditText.requestFocus();
    }

    private static void checkCategory(ChipGroup group, String category) {
        if (Note.CATEGORY_SCHOOL.equals(category)) {
            group.check(R.id.chipSchool);
        } else if (Note.CATEGORY_IDEAS.equals(category)) {
            group.check(R.id.chipIdeas);
        } else {
            group.check(R.id.chipPersonal);
        }
    }

    private static String selectedCategory(ChipGroup group) {
        int checkedId = group.getCheckedChipId();
        if (checkedId == R.id.chipSchool) {
            return Note.CATEGORY_SCHOOL;
        }
        if (checkedId == R.id.chipIdeas) {
            return Note.CATEGORY_IDEAS;
        }
        return Note.CATEGORY_PERSONAL;
    }

    private static String textOf(CharSequence text) {
        return text == null ? "" : text.toString().trim();
    }
}
