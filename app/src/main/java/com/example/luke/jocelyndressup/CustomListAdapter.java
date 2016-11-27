package com.example.luke.jocelyndressup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    private final Float[] price;
    ArrayList<ImageView> views = new ArrayList<ImageView>();
    Context c;
    public  ArrayList<AsyncTask> tasks = new ArrayList<AsyncTask>();

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid, Float[] price, Context con) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.price = price;
        c = con;
    }

    public void setImage(ImageView view, String fileName){
        try {
            ImageLoaderPacket packet = new ImageLoaderPacket(fileName,view, c);
            ImageLoader il = new ImageLoader();
            tasks.add(il);
            il.execute(packet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        setImage(imageView,itemname[position]);

        views.add(imageView);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname[position]);
        extratxt.setText("Price: $"+String.format("%.2f", price[position]));
        return rowView;

    };


}