package com.example.luke.jocelyndressup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Luke on 2016-11-10.
 */

public class ChangeImageRunnable implements Runnable {
    ImageView iv;
    Context c;
    String filename;
    ChangeImageRunnable(ImageView imagev, Context context, String fileName){
        iv = imagev;
        c = context;
        filename = fileName;
    }
    @Override
    public void run() {
        //iv.setImageResource(resID);
        setImage(iv,filename);
        Animation animSlideBack = AnimationUtils.loadAnimation(c,
                R.anim.slideback);
        iv.startAnimation(animSlideBack);
    }

    public void setImage(ImageView view, String fileName){
        try {
            FileInputStream fin = c.openFileInput(fileName+".bmp");
            Bitmap b = BitmapFactory.decodeStream(fin);
            view.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}


