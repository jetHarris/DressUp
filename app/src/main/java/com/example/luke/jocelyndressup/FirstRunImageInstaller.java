package com.example.luke.jocelyndressup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Luke on 2016-11-23.
 */

public class FirstRunImageInstaller extends AsyncTask<InstallPacket, Integer, Button> {
    @Override
    protected Button doInBackground(InstallPacket... params) {
        //take all the images from the drawable folder and put them in the internal storage
        Bitmap feet1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.feet1);
        saveToInternalStorage(feet1,"feet1",params[0].c);
        //feet1.recycle();

        Bitmap feet2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.feet2);
        saveToInternalStorage(feet2,"feet2",params[0].c);
        //feet2.recycle();

        Bitmap feet3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.feet3);
        saveToInternalStorage(feet3,"feet3",params[0].c);
        //feet3.recycle();

        Bitmap head1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.head1);
        saveToInternalStorage(head1,"head1",params[0].c);
        //head1.recycle();

        Bitmap head2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.head2);
        saveToInternalStorage(head2,"head2",params[0].c);
        //head2.recycle();

        Bitmap head3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.head3);
        saveToInternalStorage(head3,"head3",params[0].c);
        //head3.recycle();

        Bitmap torso1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.torso1);
        saveToInternalStorage(torso1,"torso1",params[0].c);
        //torso1.recycle();

        Bitmap torso2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.torso2);
        saveToInternalStorage(torso2,"torso2",params[0].c);
        //torso2.recycle();

        Bitmap torso3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.torso3);
        saveToInternalStorage(torso3,"torso3",params[0].c);
        //torso3.recycle();

        Bitmap legs1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.legs1);
        saveToInternalStorage(legs1,"legs1",params[0].c);
        //legs1.recycle();

        Bitmap legs2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.legs2);
        saveToInternalStorage(legs2,"legs2",params[0].c);
        //legs2.recycle();

        Bitmap legs3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.legs3);
        saveToInternalStorage(legs3,"legs3",params[0].c);
        //legs3.recycle();

        return params[0].but;
    }

    private void saveToInternalStorage(Bitmap bitmapImage, String filename, Context c){

        FileOutputStream fos = null;
        try {
            fos = c.openFileOutput(filename+".bmp", Context.MODE_PRIVATE);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 10, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    protected void onPostExecute(Button but){
        but.setEnabled(true);
        but.setVisibility(View.VISIBLE);
    }
}
