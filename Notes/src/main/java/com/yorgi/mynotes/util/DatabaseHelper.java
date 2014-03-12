package com.yorgi.mynotes.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yorgi.mynotes.model.Note;

import java.sql.SQLException;

/**
 * Created by Bartosz on 08.03.14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "notes_storage.db";
    private static final int DATABASE_VERSION = 1;

    //private Dao<Note, Long> notesDao = null;
    private RuntimeExceptionDao<Note, Long> notesRuntimeDao = null;

    // Singleton
    private static DatabaseHelper instance = null;
    public static DatabaseHelper getHelper()
    {
        if (instance == null) instance = new DatabaseHelper(Helper.getContext());
        return instance;
    }

    public static void releaseHelper()
    {
        if (instance != null)
        {
            OpenHelperManager.releaseHelper();
            instance = null;
        }
    }

    // Database Management
    /**
     * @param context         Associated content from the application. This is needed to locate the database.
     */
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * What to do when your database needs to be created. Usually this entails creating the tables and loading any
     * initial data.
     * <p/>
     * <p>
     * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method call or the one
     * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected results may result.
     * </p>
     *
     * @param database         Database being created.
     * @param connectionSource To use get connections to the database to be updated.
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            android.util.Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Note.class);
        }
        catch(SQLException e)
        {
            android.util.Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * What to do when your database needs to be updated. This could mean careful migration of old data to new data.
     * Maybe adding or deleting database columns, etc..
     * <p/>
     * <p>
     * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method call or the one
     * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected results may result.
     * </p>
     *
     * @param database         Database being upgraded.
     * @param connectionSource To use get connections to the database to be updated.
     * @param oldVersion       The version of the current database so we can know what to do to the database.
     * @param newVersion       The version of the new database so we can know what to do to the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            android.util.Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            if (oldVersion != newVersion)
            {
                TableUtils.dropTable(connectionSource, Note.class, true);
                onCreate(database, connectionSource);
            }
        }
        catch(SQLException e)
        {
            android.util.Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /*
    public Dao<Note, Long> getNotesDao() throws SQLException
    {
        if (notesDao == null) notesDao = getDao(Note.class);
        return notesDao;
    }
    */

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Note, Long> getNotesDao()
    {
        if (notesRuntimeDao == null) notesRuntimeDao = getRuntimeExceptionDao(Note.class);
        return notesRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close()
    {
        super.close();
        //notesDao = null;
        notesRuntimeDao = null;
    }
}
