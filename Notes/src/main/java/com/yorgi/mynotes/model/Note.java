package com.yorgi.mynotes.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Bartosz on 08.03.14.
 */
@DatabaseTable(tableName = "Notes")
public class Note
{
    public enum NoteState
    {
        NORMAL,
        ARCHIVED,
        DELETED
    }

    @DatabaseField(generatedId = true)
    private long Id;
    @DatabaseField
    private String Title;
    @DatabaseField
    private String Text;
    @DatabaseField(canBeNull = true)
    private String ImagePath;
    @DatabaseField(canBeNull = true)
    private String AudioPath;
    @DatabaseField(canBeNull = false)
    private int State;

    public Note(){}

    public Note(String title, String text)
    {
        this(title, text, null, null, NoteState.NORMAL);
    }

    public Note(String title, String text, String imagePath, String audioPath, NoteState state)
    {
        this.Title = title;
        this.Text = text;
        this.AudioPath = audioPath;
        this.ImagePath = imagePath;
        this.State = state.ordinal();
    }

    public String getImagePath()
    {
        return ImagePath;
    }

    public long getId()
    {
        return Id;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getText()
    {
        return Text;
    }

    public String getAudioPath()
    {
        return AudioPath;
    }

    public boolean hasAudio() { return AudioPath != null; }

    public void setId(long id)
    {
        Id = id;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public void setText(String text)
    {
        Text = text;
    }

    public void setImagePath(String imagePath)
    {
        ImagePath = imagePath;
    }

    public void setAudioPath(String audioPath)
    {
        AudioPath = audioPath;
    }

    public boolean hasPhoto()
    {
        return ImagePath != null;
    }

    public NoteState getState()
    {
        return NoteState.values()[State];
    }

    public void setState(NoteState state)
    {
        State = state.ordinal();
    }

}
