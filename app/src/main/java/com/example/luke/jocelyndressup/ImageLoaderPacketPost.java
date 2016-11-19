package com.example.luke.jocelyndressup;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Luke on 2016-11-19.
 */

public class ImageLoaderPacketPost {

    Bitmap bit;
    ImageView view;
    Context context;
    ImageView secondView= null;
    ImageLoaderPacketPost(Bitmap bit, ImageView v, Context c){
        this.bit = bit;
        view = v;
        context = c;
    }

    ImageLoaderPacketPost(Bitmap bit, ImageView v, Context c, ImageView second){
        this.bit = bit;
        view = v;
        context = c;
        secondView = second;
    }
}
