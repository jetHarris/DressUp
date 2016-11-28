package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;

public class DisplayReadItemActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvPrice;
    Bitmap imgItem = null;
    String name;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_read_item);
        getSupportActionBar().hide(); // Get rid of actionbar manually

        tvName = (TextView) findViewById(R.id.dis_name);
        tvPrice = (TextView) findViewById(R.id.dis_price);

        // Get Bundled data
        if (getIntent().getExtras() != null) {
//            if (getIntent().hasExtra("image"))
//                imgItem = BitmapFactory.decodeByteArray(
//                        getIntent().getByteArrayExtra("image"), 0, getIntent().getByteArrayExtra("image").length);
            name = getIntent().getExtras().getString("name", "");
            price = getIntent().getExtras().getString("price", "");
        }

        //Set Textviews
        tvName.setText(namify(name));
        tvPrice.setText(price);

        try {
            FileInputStream fin = openFileInput(namify(name) + ".bmp");
            imgItem = BitmapFactory.decodeStream(fin);
            ImageView iv = (ImageView)findViewById(R.id.ivNewItem);
            iv.setImageResource(R.drawable.icon);
            iv.setImageBitmap(imgItem);
        }catch(Exception e){
            Log.e("Image","Failed to load image",e);
        }

        //Trigger animation
        fadeinAnimation(R.anim.fadein);
    }

    protected String namify(String name) {
        String temp = name.replace(' ', '_');
        String results = temp.toLowerCase();
        return results;
    }

    private void fadeinAnimation(int animationResourceID)
    {
        ImageView reuseImageView = (ImageView)findViewById(R.id.ivNewItem);
        if(imgItem != null)
            reuseImageView.setImageBitmap(imgItem);
        reuseImageView.setVisibility(View.VISIBLE);
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        reuseImageView.startAnimation(an);
    }

    public void onClick(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
