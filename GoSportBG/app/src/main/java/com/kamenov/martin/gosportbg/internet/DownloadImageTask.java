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
import com.kamenov.martin.gosportbg.models.optimizators.PictureSavior;

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
    private PictureSavior pictureSavior;
    private String url;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.usedLoaderConstruntor = false;
        this.pictureSavior = PictureSavior.getInstance();
    }

    public DownloadImageTask(ProgressBar progressBar, Activity activity) {
        this.mProgressBar = progressBar;
        this.mActivity = activity;
        this.usedLoaderConstruntor = true;
        this.pictureSavior = PictureSavior.getInstance();
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        url = urls[0];
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
        if(!pictureSavior.hasBitmap(url)) {
            pictureSavior.setBitmap(url, result);
        }
        if(!usedLoaderConstruntor) {
            bmImage.setImageBitmap(result);
        } else {
            mParent = (ViewGroup) mProgressBar.getParent();
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