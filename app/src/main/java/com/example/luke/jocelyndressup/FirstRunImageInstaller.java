package com.example.luke.jocelyndressup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
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
        Bitmap feet1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.tan_flats);
        saveToInternalStorage(feet1,"tan_flats",params[0].c);

        Bitmap feet2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.brown_flats);
        saveToInternalStorage(feet2,"brown_flats",params[0].c);

        Bitmap feet3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.sneakers);
        saveToInternalStorage(feet3,"sneakers",params[0].c);

        Bitmap head1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.nothing);
        saveToInternalStorage(head1,"nothing",params[0].c);

        Bitmap head2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.brown_hat);
        saveToInternalStorage(head2,"brown_hat",params[0].c);

        Bitmap head3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.black_hat);
        saveToInternalStorage(head3,"black_hat",params[0].c);

        Bitmap torso1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.brown_gold_sweater);
        saveToInternalStorage(torso1,"brown_gold_sweater",params[0].c);

        Bitmap torso2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.grey_crop_top);
        saveToInternalStorage(torso2,"grey_crop_top",params[0].c);

        Bitmap torso3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.white_dress_shirt);
        saveToInternalStorage(torso3,"white_dress_shirt",params[0].c);

        Bitmap legs1 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.black_leggings);
        saveToInternalStorage(legs1,"black_leggings",params[0].c);

        Bitmap legs2 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.peasant_skirt);
        saveToInternalStorage(legs2,"peasant_skirt",params[0].c);

        Bitmap legs3 = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.black_skirt);
        saveToInternalStorage(legs3,"black_skirt",params[0].c);

        Bitmap question_mark = BitmapFactory.decodeResource(params[0].c.getResources(),R.drawable.question_mark);
        saveToInternalStorage(question_mark,"question_mark",params[0].c);

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
