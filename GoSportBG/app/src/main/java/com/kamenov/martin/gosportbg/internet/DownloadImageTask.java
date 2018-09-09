package com.kamenov.martin.gosportbg.internet;

import android.app.Activity;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kamenov.martin.gosportbg.R;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Martin on 7.7.2018 г..
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private ProgressBar mProgressBar;
    private ViewGroup mParent;
    private Activity mActivity;
    private boolean usedLoaderConstruntor;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.usedLoaderConstruntor = false;
    }

    public DownloadImageTask(ProgressBar progressBar, ViewGroup parent, Activity activity) {
        this.mProgressBar = progressBar;
        this.mParent = parent;
        this.mActivity = activity;
        this.usedLoaderConstruntor = true;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if(!usedLoaderConstruntor) {
            bmImage.setImageBitmap(result);
        } else {
            ViewGroup.LayoutParams params = mProgressBar.getLayoutParams();
            int index = mParent.indexOfChild(mProgressBar);
            mParent.removeView(mProgressBar);
            CircleImageView img = new CircleImageView(mActivity);
            mParent.addView(img, index);
            img.setLayoutParams(params);
            img.setImageBitmap(result);
        }
    }
}