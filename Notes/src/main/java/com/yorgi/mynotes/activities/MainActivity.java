package com.yorgi.mynotes.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ViewSwitcher;

import com.yorgi.mynotes.R;
import com.yorgi.mynotes.fragments.NavigationDrawerFragment;
import com.yorgi.mynotes.model.Note;
import com.yorgi.mynotes.util.DatabaseHelper;
import com.yorgi.mynotes.util.Helper;
import com.yorgi.mynotes.util.NotesAdapter;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    private static final String LIST_TYPE = "listType";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(position == 3) fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
        else fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
    }

    public void onSectionAttached(int number)
    {
        switch (number)
        {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
        }
    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_add)
        {
            startActivityForResult(new Intent(this, AddNewNote.class), AddNewNote.NEW_NOTE_REQ);
            return true;
        }        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AddNewNote.NEW_NOTE_REQ)
        {
            Note note = (Note) data.getSerializableExtra("Note");
            DatabaseHelper.getHelper().getNotesDao().createOrUpdate(note);
        }
    }

    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        private NotesAdapter adapter;
        private ViewSwitcher viewSwitcher;

        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.viewSwitcher);
            if(adapter == null) adapter = new NotesAdapter(getActivity(), Note.NoteState.NORMAL);
            ((AdapterView<ListAdapter>)viewSwitcher.getCurrentView()).setAdapter(adapter);
            setHasOptionsMenu(true);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            inflater.inflate(R.menu.fragment_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            if(item.getItemId() == R.id.action_test)
            {
                SharedPreferences prefs = getActivity().getSharedPreferences(Helper.MAIN_PREFS, MODE_PRIVATE);
                boolean var = prefs.getBoolean(LIST_TYPE, true);
                prefs.edit().putBoolean(LIST_TYPE, !var).commit();
                viewSwitcher.showNext();

                AdapterView<ListAdapter> adapterView = (AdapterView<ListAdapter>)viewSwitcher.getCurrentView();
                if(adapterView.getAdapter() == null) adapterView.setAdapter(adapter);

                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }


}
