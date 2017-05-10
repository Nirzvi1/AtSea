package com.mdtermproject.atsea.graphics;

import android.graphics.Bitmap;
import android.util.SparseArray;

import com.mdtermproject.atsea.utils.ResourceLoader;

/**
 * Created by Windows on 2017-05-03.
 */

public class ImageLibrary {
    private static SparseArray<Bitmap> images;

    public static void init(){
        images = new SparseArray<>();
        ResourceLoader rl = new ResourceLoader();
    }
    public static Bitmap getImage(int resId){
        return images.get(resId);
    }
}
