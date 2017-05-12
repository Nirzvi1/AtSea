package com.mdtermproject.atsea.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

public class ResourceLoader {
    private static Context c;

    public ResourceLoader(){}

    public Bitmap getBitmap(int resId){
        return BitmapFactory.decodeResource(c.getResources(), resId);
    }

    public int getResColour(int resId){
        return c.getResources().getColor(resId);
    }

    public String getResString(int resId){
        return c.getResources().getString(resId);
    }

    public static void init(Context con){
        c = con;
    }
}

