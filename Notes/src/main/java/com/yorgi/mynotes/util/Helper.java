package com.yorgi.mynotes.util;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by Bartosz on 08.03.14.
 */
public class Helper
{
    public static void setContext(Context context)
    {
        Helper.context = new WeakReference<>(context);
    }
    public static Context getContext()
    {
        return context.get();
    }
    private static WeakReference<Context> context;

    //Shared Preferences
    public static String MAIN_PREFS = "mainPrefs";


}
