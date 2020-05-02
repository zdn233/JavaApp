package com.example.fotagd269zhan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;


//Custom Asynctask class --- used to load image from URL
public class downloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView ImageViewReference;
    Bitmap bitmap;

    public downloadImageTask(ImageView imview, Bitmap bitmap) {
        this.ImageViewReference = imview;
        this.bitmap = bitmap;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String URLdisplay = params[0];
        bitmap = null;
        try{
            InputStream srt = new java.net.URL(URLdisplay).openStream();
            bitmap = BitmapFactory.decodeStream(srt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        ImageViewReference.setImageBitmap(result);
    }
}