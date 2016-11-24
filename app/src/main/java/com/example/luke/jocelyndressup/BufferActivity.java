package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        getSupportActionBar().hide(); // Get rid of actionbar manually
        sharingAnimation(R.anim.rotatesharing);
    }

    private void sharingAnimation(int animationResourceID)
    {
        ImageView reuseImageView = (ImageView)findViewById(R.id.imageView2);
        reuseImageView.setImageResource(R.drawable.icon);
        reuseImageView.setVisibility(View.VISIBLE);
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        reuseImageView.startAnimation(an);
    }

    public void mainPageClicked(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
