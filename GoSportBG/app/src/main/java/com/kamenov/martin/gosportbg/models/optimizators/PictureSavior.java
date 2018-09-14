package com.kamenov.martin.gosportbg.models.optimizators;

import android.graphics.Bitmap;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Martin on 13.9.2018 г..
 */

public class PictureSavior {
    private static PictureSavior pictureSavior;
    private Dictionary<String, Bitmap> bitmaps;

    private PictureSavior() {
        bitmaps = new Hashtable();
    }

    public static PictureSavior getInstance() {
        if(pictureSavior == null) {
            pictureSavior = new PictureSavior();
        }

        return pictureSavior;
    }

    public boolean hasBitmap(String url) {
        if(bitmaps.get(url) == null) {
            return false;
        }

        return true;
    }

    public void setBitmap(String url, Bitmap bitmap) {
        if(url == null || bitmap == null || url.length() == 0) {
            return;
        }
        this.bitmaps.put(url, bitmap);
    }

    public Bitmap getBitmap(String url) {
        return bitmaps.get(url);
    }
}
