package com.hexp.quicknote;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class NoteListTest {
    private ArrayList<Note> sampleNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note("Algebra review", "Practice quadratic equations", Note.CATEGORY_SCHOOL, 1));
        notes.add(new Note("Weekend plan", "Hike and take photos", Note.CATEGORY_PERSONAL, 2));
        notes.add(new Note("App concept", "Build a small notes app", Note.CATEGORY_IDEAS, 3));
        return notes;
    }

    @Test
    public void blankQueryReturnsEveryNote() {
        assertEquals(3, NoteList.filterNotes(sampleNotes(), "   ").size());
    }

    @Test
    public void matchesTitleBodyAndCategory() {
        assertEquals(1, NoteList.filterNotes(sampleNotes(), "algebra").size());
        assertEquals(1, NoteList.filterNotes(sampleNotes(), "photos").size());
        assertEquals(1, NoteList.filterNotes(sampleNotes(), "ideas").size());
    }

    @Test
    public void returnsEmptyListWhenNothingMatches() {
        assertEquals(0, NoteList.filterNotes(sampleNotes(), "volcano").size());
    }

    @Test
    public void matchingIsCaseInsensitive() {
        assertEquals(1, NoteList.filterNotes(sampleNotes(), "APP").size());
    }

    @Test
    public void addUsesTheSameListAsSearch() {
        NoteList list = new NoteList();
        list.add(new Note("Chemistry", "Lab report", Note.CATEGORY_SCHOOL, 1));
        list.add(new Note("Weekend", "Hike", Note.CATEGORY_PERSONAL, 2));

        assertEquals(2, list.size());
        assertEquals(2, list.all().size());
        assertEquals(1, list.search("lab").size());
    }
}
