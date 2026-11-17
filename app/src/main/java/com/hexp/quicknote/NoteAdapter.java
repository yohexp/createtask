package com.hexp.quicknote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hexp.quicknote.databinding.ItemNoteBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    public interface Listener {
        void onNoteClick(Note note);

        void onDeleteClick(Note note);
    }

    private final ArrayList<Note> items = new ArrayList<>();
    private final boolean showDelete;
    private final Listener listener;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("MMM d, h:mm a", Locale.getDefault());

    public NoteAdapter(boolean showDelete, Listener listener) {
        this.showDelete = showDelete;
        this.listener = listener;
    }

    public void submitList(ArrayList<Note> notes) {
        items.clear();
        items.addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(items.get(position), position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    final class NoteViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoteBinding binding;

        NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Note note, int position, int itemCount) {
            binding.getRoot().updateAppearance(position, itemCount);
            binding.titleText.setText(note.getTitle());
            if (note.getBody().isEmpty()) {
                binding.bodyText.setText(R.string.note_body_empty);
            } else {
                binding.bodyText.setText(note.getBody());
            }
            binding.categoryChip.setText(note.getCategory());
            binding.dateText.setText(dateFormat.format(new Date(note.getUpdatedAt())));
            binding.deleteButton.setVisibility(showDelete ? View.VISIBLE : View.GONE);
            binding.noteCard.setOnClickListener(view -> listener.onNoteClick(note));
            binding.deleteButton.setOnClickListener(view -> listener.onDeleteClick(note));
        }
    }
}
