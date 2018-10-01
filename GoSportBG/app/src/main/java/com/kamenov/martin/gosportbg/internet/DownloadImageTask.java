package com.kamenov.martin.gosportbg.internet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.models.engine.ImageBorderService;
import com.kamenov.martin.gosportbg.models.optimizators.ImageCachingService;

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
    private ImageCachingService imageCachingService;
    private String url;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.usedLoaderConstruntor = false;
        this.imageCachingService = ImageCachingService.getInstance();
    }

    public DownloadImageTask(ProgressBar progressBar, Activity activity) {
        this.mProgressBar = progressBar;
        this.mActivity = activity;
        this.usedLoaderConstruntor = true;
        this.imageCachingService = ImageCachingService.getInstance();
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
        if(!imageCachingService.hasBitmap(url)) {
            imageCachingService.setBitmap(url, result);
        }

        if(!usedLoaderConstruntor) {
            bmImage.setImageBitmap(result);
        } else {
            if(result == null) {
                result = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.noimagefound);
            }
            mParent = (ViewGroup) mProgressBar.getParent();
            ViewGroup.LayoutParams params = mProgressBar.getLayoutParams();
            int index = mParent.indexOfChild(mProgressBar);
            mParent.removeView(mProgressBar);
            CircleImageView img = new CircleImageView(mActivity);
            ImageBorderService.addBorders(img);
            mParent.addView(img, index);
            img.setLayoutParams(params);
            img.setImageBitmap(result);
        }
    }
}