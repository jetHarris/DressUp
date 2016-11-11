package com.example.luke.jocelyndressup;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Luke on 2016-11-11.
 */

public class ChangeImageLeftRunnable implements Runnable {
    ImageView iv;
    int resID;
    Context c;
    ChangeImageLeftRunnable(ImageView imagev, int res, Context context){
        iv = imagev;
        resID = res;
        c = context;
    }
    @Override
    public void run() {
        iv.setImageResource(resID);
        Animation animSlideBack = AnimationUtils.loadAnimation(c,
                R.anim.slidebackleft);
        iv.startAnimation(animSlideBack);
    }
}