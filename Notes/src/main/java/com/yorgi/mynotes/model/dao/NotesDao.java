package com.yorgi.mynotes.model.dao;

import com.yorgi.mynotes.model.Note;
import com.yorgi.mynotes.util.DatabaseHelper;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bartosz on 08.03.14.
 */
public class NotesDao
{
    public static List<Note> getLimitedRows(long offset, long limit)
    {
        try
        {
            return DatabaseHelper.getHelper().getNotesDao().queryBuilder().offset(offset).limit(limit).query();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Note> getFilteredByState(Note.NoteState filter)
    {
        try
        {
            return DatabaseHelper.getHelper().getNotesDao().queryBuilder()
                    .where().eq("State", filter.ordinal()).query();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Note> getAll()
    {
        return DatabaseHelper.getHelper().getNotesDao().queryForAll();
    }

    public static long getCount()
    {
        return DatabaseHelper.getHelper().getNotesDao().countOf();
    }

    public static void addNote(Note note)
    {
        DatabaseHelper.getHelper().getNotesDao().create(note);
    }

    public static void updateNote(Note note)
    {
        DatabaseHelper.getHelper().getNotesDao().update(note);
    }

    public static void removeNote(Note note)
    {
        removeNotes(note);
    }

    public static void removeNotes(Note... notes)
    {
        DatabaseHelper.getHelper().getNotesDao().delete(Arrays.asList(notes));
    }

    public static void clear()
    {
        try
        {
            DatabaseHelper.getHelper().getNotesDao().deleteBuilder().delete();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
