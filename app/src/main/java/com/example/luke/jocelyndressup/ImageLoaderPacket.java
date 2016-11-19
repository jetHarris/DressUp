package com.example.luke.jocelyndressup;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Luke on 2016-11-19.
 */

public class ImageLoaderPacket {
    String fileName;
    ImageView view;
    Context context;
    ImageView secondView = null;
    ImageLoaderPacket(String fname, ImageView v, Context c){
        fileName = fname;
        view= v;
        context = c;
    }

    ImageLoaderPacket(String fname, ImageView v, Context c, ImageView second){
        fileName = fname;
        view= v;
        context = c;
        secondView = second;
    }

}
