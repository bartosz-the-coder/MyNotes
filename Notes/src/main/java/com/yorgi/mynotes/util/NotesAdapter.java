package com.yorgi.mynotes.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yorgi.mynotes.R;
import com.yorgi.mynotes.model.Note;
import com.yorgi.mynotes.model.dao.NotesDao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bartosz on 08.03.14.
 */
public class NotesAdapter extends BaseAdapter
{
    private static final int MAX_FEED_SIZE = 5;
    private static final long DATA_SIZE = NotesDao.getCount();

    private int count;
    private List<Note> data;
    private LayoutInflater inflater;
    private boolean isFooterAdded;

    public NotesAdapter(Context context, Note.NoteState notesFilter)
    {
        this(context, NotesDao.getFilteredByState(notesFilter));
    }

    public NotesAdapter(Context context, List<Note> data)
    {
        Collections.reverse(data);
        this.data = data;
        this.count = data.size();
        if (DATA_SIZE > MAX_FEED_SIZE)
        {
            isFooterAdded = true;
            count++;
        }
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        if(data == null || data.size() == 0) return -1;
        return data.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(position == count - 1 && isFooterAdded)
            return inflateFooterView(parent);

        if (convertView == null || convertView instanceof FrameLayout) convertView = inflateNoteView(parent);
        setupNoteView(convertView, parent, position);

        return convertView;
    }

    private View inflateNoteView(ViewGroup parent)
    {
        ViewHolder holder = new ViewHolder();
        View convertView = inflater.inflate(R.layout.list_item, parent, false);
        holder.Title = (TextView) convertView.findViewById(R.id.text1);
        holder.Text = (TextView) convertView.findViewById(R.id.text2);
        holder.SoundIcon = (ImageView) convertView.findViewById(R.id.audioIcon);
        holder.Thumbnail = (ImageView) convertView.findViewById(R.id.imageIcon);
        convertView.setTag(holder);
        return convertView;
    }

    private void setupNoteView(View convertView, ViewGroup parent, int position)
    {
        ViewHolder holder;
        if ((holder = (ViewHolder) convertView.getTag()) == null)
        {
            convertView = inflateNoteView(parent);
            holder = (ViewHolder) convertView.getTag();
        }
        Note note = (Note) getItem(position);
        holder.Title.setText(note.getTitle());
        holder.Text.setText(note.getText());
        holder.Thumbnail.setVisibility(note.hasPhoto() ? View.VISIBLE : View.INVISIBLE);
        holder.SoundIcon.setVisibility(note.hasAudio() ? View.VISIBLE : View.INVISIBLE);
    }

    private View inflateFooterView(ViewGroup parent)
    {
        View convertView = inflater.inflate(R.layout.footer, parent, false);
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long feedSize = DATA_SIZE - count;
                long offset = feedSize - MAX_FEED_SIZE;
                if (isFooterAdded)
                {
                    offset++;
                    feedSize++;
                }
                if (offset < 0) offset = 0;
                loadNotes(NotesDao.getLimitedRows(offset, feedSize < MAX_FEED_SIZE ? feedSize : MAX_FEED_SIZE));
            }
        });
        return convertView;
    }

    public void loadNotes(List<Note> data)
    {
        Collections.reverse(data);
        this.data.addAll(count - 1, data);
        count += data.size();
        if (count - 1 == DATA_SIZE)
        {
            isFooterAdded = false;
            count--;
        }
        else
        {
            if(!isFooterAdded)
            {
                isFooterAdded = true;
                count++;
            }
        }
        notifyDataSetChanged();
    }

    public void addNotes(Note... notes)
    {
        addNotes(Arrays.asList(notes));
    }

    public void addNotes(List<Note> notes)
    {
        this.data.addAll(0, notes);
        notifyDataSetChanged();
    }

    public void refreshData()
    {
        long offset = NotesDao.getCount() - count;
        if(isFooterAdded) offset++;
        if (offset < 0) offset = 0;
        data = NotesDao.getLimitedRows(offset, count);
        notifyDataSetInvalidated();
    }

    static class ViewHolder
    {
        TextView Title;
        TextView Text;
        ImageView Thumbnail;
        ImageView SoundIcon;
    }
}
