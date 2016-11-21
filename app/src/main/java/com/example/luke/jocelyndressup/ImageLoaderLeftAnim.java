package com.example.luke.jocelyndressup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Luke on 2016-11-19.
 */

public class ImageLoaderLeftAnim
        extends AsyncTask<ImageLoaderPacket, ImageLoaderPacketPost, ImageLoaderPacketPost> {
    protected void onPreExecute(){
        //Setup is done here
    }
    @Override
    protected ImageLoaderPacketPost doInBackground(ImageLoaderPacket... params) {
        //TODO Auto-generated method stub
        try{
            FileInputStream fin1 = params[0].context.openFileInput(params[0].fileName+".bmp");
            Bitmap b1 = BitmapFactory.decodeStream(fin1);
            ImageLoaderPacketPost packet1 = new ImageLoaderPacketPost(b1,params[0].view, params[0].context, params[0].secondView);
            publishProgress(packet1);

            FileInputStream fin = params[0].context.openFileInput(params[0].fileName+".bmp");
            //Bitmap b = BitmapFactory.decodeStream(fin);
            ImageLoaderPacketPost packet = new ImageLoaderPacketPost(b1,params[0].view, params[0].context);
            Thread.sleep(700);
            return packet;
        }catch(Exception e){
            Log.e("Image","Failed to load image",e);
        }
        return null;
    }
    protected void onProgressUpdate(ImageLoaderPacketPost ...params){
        params[0].secondView.setImageBitmap(params[0].bit);
        Animation animSlide = AnimationUtils.loadAnimation(params[0].context,
                R.anim.slide);
        params[0].view.startAnimation(animSlide);
    }
    protected void onPostExecute(ImageLoaderPacketPost ilpp){
        if(ilpp!=null){
            ilpp.view.setImageBitmap(ilpp.bit);
            Animation animSlideBack = AnimationUtils.loadAnimation(ilpp.context,
                    R.anim.slidebackleft);
            ilpp.view.startAnimation(animSlideBack);
        }
    }
    protected void onCancelled(){
    }
}
