package com.example.luke.jocelyndressup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Luke on 2016-11-19.
 */

public class ImageLoader
        extends AsyncTask<ImageLoaderPacket, Integer, ImageLoaderPacketPost> {
    protected void onPreExecute(){
        //Setup is done here
    }
    @Override
    protected ImageLoaderPacketPost doInBackground(ImageLoaderPacket... params) {
        //TODO Auto-generated method stub
        try{
            FileInputStream fin = params[0].context.openFileInput(params[0].fileName+".bmp");
            Bitmap b = BitmapFactory.decodeStream(fin);
            ImageLoaderPacketPost packet = new ImageLoaderPacketPost(b,params[0].view, params[0].context);
            return packet;
        }catch(Exception e){
            Log.e("Image","Failed to load image",e);
        }
        return null;
    }
    protected void onProgressUpdate(Integer... params){
        //Update a progress bar here, or ignore it, it's up to you
    }
    protected void onPostExecute(ImageLoaderPacketPost ilpp){
        if(ilpp!=null){
            ilpp.view.setImageBitmap(ilpp.bit);

        }
    }
    protected void onCancelled(){
    }
}
