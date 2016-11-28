package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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
            if (getIntent().hasExtra("image"))
                imgItem = BitmapFactory.decodeByteArray(
                        getIntent().getByteArrayExtra("image"), 0, getIntent().getByteArrayExtra("image").length);
            name = getIntent().getExtras().getString("name", "");
            price = getIntent().getExtras().getString("price", "");
        }

        //Set Textviews
        tvName.setText(name);
        tvPrice.setText(price);

        //Trigger animation
        fadeinAnimation(R.anim.fadein);
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
