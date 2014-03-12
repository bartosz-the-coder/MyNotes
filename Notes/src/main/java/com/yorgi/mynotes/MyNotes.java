package com.yorgi.mynotes;

import android.app.Application;

import com.yorgi.mynotes.model.Note;
import com.yorgi.mynotes.model.dao.NotesDao;
import com.yorgi.mynotes.util.Helper;

import java.util.Random;

/**
 * Created by Bartosz on 08.03.14.
 */
public class MyNotes extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Helper.setContext(this);
        //NotesDao.clear();
        //CreateMockData();
    }

    Random r = new Random();
    private void CreateMockData()
    {
        for(int i = 0 ; i < 12 ; i++)
        {
            int rand1 = r.nextInt(4) + 1;
            int rand2 = r.nextInt(3) + 1;
            Note n = new Note("Title " + (i + 1), "Tekst " + (i + 1));
            n.setAudioPath(i % rand1 == 0 ? "asd" : null);
            n.setImagePath(i % rand2 == 0 ? "asd" : null);
            Note.NoteState s;
            switch (i % 3)
            {
                case 0: s = Note.NoteState.NORMAL; break;
                case 1: s = Note.NoteState.ARCHIVED; break;
                case 2: s = Note.NoteState.DELETED; break;
                default: s = Note.NoteState.NORMAL;
            }
            n.setState(s);
            NotesDao.addNote(n);
        }
    }
}
