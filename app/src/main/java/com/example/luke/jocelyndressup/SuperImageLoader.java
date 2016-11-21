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

public class SuperImageLoader
        extends AsyncTask<ImageLoaderSuperPacket, ImageLoaderPacketPost, ImageLoaderPacketPost> {
    protected void onPreExecute(){
        //Setup is done here
    }
    @Override
    protected ImageLoaderPacketPost doInBackground(ImageLoaderSuperPacket... params) {
//TODO Auto-generated method stub
        try{

            FileInputStream fin = params[0].context.openFileInput(params[0].fileNames.get(0)+".bmp");
            Bitmap b = BitmapFactory.decodeStream(fin);


            ImageLoaderPacketPost packet = new ImageLoaderPacketPost(b,params[0].viewList.get(0), params[0].context, params[0].viewList.get(1));
            publishProgress(packet);


            //b.recycle();
            fin = params[0].context.openFileInput(params[0].fileNames.get(1)+".bmp");
            b = BitmapFactory.decodeStream(fin);


            packet = new ImageLoaderPacketPost(b,params[0].viewList.get(2), params[0].context, params[0].viewList.get(3));
            publishProgress(packet);

            //b.recycle();
            fin = params[0].context.openFileInput(params[0].fileNames.get(2)+".bmp");
            b = BitmapFactory.decodeStream(fin);


            packet = new ImageLoaderPacketPost(b,params[0].viewList.get(4), params[0].context, params[0].viewList.get(5));
            publishProgress(packet);

            //b.recycle();
            fin = params[0].context.openFileInput(params[0].fileNames.get(3)+".bmp");
            b = BitmapFactory.decodeStream(fin);


            packet = new ImageLoaderPacketPost(b,params[0].viewList.get(6), params[0].context, params[0].viewList.get(7));
            publishProgress(packet);

            return packet;
        }
        catch(OutOfMemoryError e){

        }catch(Exception e){
            Log.e("Image","Failed to load image",e);
        }
        return null;
    }
    protected void onProgressUpdate(ImageLoaderPacketPost... params){
        try {
            params[0].view.setImageBitmap(params[0].bit);
            params[0].secondView.setImageBitmap(params[0].bit);
        }
        catch(OutOfMemoryError e){

        }
        catch (Exception ex){

        }
    }
    protected void onPostExecute(ImageLoaderPacketPost ilpp){
        //ilpp.bit.recycle();
    }
    protected void onCancelled(){
    }
}
