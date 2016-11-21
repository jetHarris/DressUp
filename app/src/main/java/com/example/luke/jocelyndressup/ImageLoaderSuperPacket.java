package com.example.luke.jocelyndressup;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Luke on 2016-11-19.
 */

public class ImageLoaderSuperPacket {
    Context context;
    ArrayList<ImageView> viewList;
    ArrayList<String> fileNames;

    ImageLoaderSuperPacket(ArrayList<String> fileNames, ArrayList<ImageView> viewList, Context c){
        this.fileNames = fileNames;
        this.viewList = viewList;
        context = c;
    }

}
