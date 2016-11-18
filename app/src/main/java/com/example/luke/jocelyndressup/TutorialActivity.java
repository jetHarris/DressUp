package com.example.luke.jocelyndressup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class TutorialActivity extends AppCompatActivity {
    ViewPager viewPager;
    ProgressBar prgBar;
    Button btnDone;
    int prevPosition;
    private boolean help;
    private static final int MAX_VIEWS = 5;//Total number of tutorial images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getSupportActionBar().hide(); // Get rid of actionbar manually

        SharedPreferences spAppHistory = getSharedPreferences("AppHistory", MODE_PRIVATE);
        boolean previously_run = spAppHistory.getBoolean("previously_run", false);

        help = false;
        if(getIntent().getExtras() != null) {
            help = getIntent().getExtras().getBoolean("help", false);
        }

        if(previously_run && help == false) {
            Intent i = new Intent(this, BufferActivity.class);
            startActivity(i);
        }
        else {
            if(!help)
                firstTimeInstall();
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TutorialPagerAdapter());
        viewPager.setOnPageChangeListener(new TutorialPageChangeListener());

        prgBar = (ProgressBar) findViewById(R.id.prgBar);
        prgBar.setMax(MAX_VIEWS - 1);
        prevPosition = -1;

        btnDone = (Button) findViewById(R.id.btn_Done);
        //btnDone.setVisibility(View.INVISIBLE);
    } // onCreate

    private void firstTimeInstall() {

    }

    public void cDone(View view) {
        SharedPreferences spAppHistory = getSharedPreferences("AppHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = spAppHistory.edit();
        editor.putBoolean("previously_run", true);
        editor.commit();
        Intent i = new Intent(this, BufferActivity.class);
        startActivity(i);
    } // cDone

    // ViewPager Initialization
    private class TutorialPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return MAX_VIEWS;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            Log.e("walkthrough", "instantiateItem(" + position + ");");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageViewContainer = inflater.inflate(R.layout.single_view, null);
            ImageView imageView = (ImageView) imageViewContainer.findViewById(R.id.image_view);

            switch(position) {
                case 0:
                    //imageView.setImageResource(R.drawable.image1);
                    imageView.setBackgroundColor(Color.RED);
                    break;

                case 1:
                    imageView.setBackgroundColor(Color.BLUE);
                    break;

                case 2:
                    imageView.setBackgroundColor(Color.GREEN);
                    break;

                case 3:
                    imageView.setBackgroundColor(Color.YELLOW);
                    break;

                case 4:
                    imageView.setBackgroundColor(Color.CYAN);
                    break;
            }

            ((ViewPager) container).addView(imageViewContainer, 0);
            return imageViewContainer;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }
    } // TutorialPagerAdapter

    // Callbacks for the ViewPager
    private class TutorialPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // Here is where you should show change the view of page indicator
            if(prevPosition < position)
                prgBar.setProgress(prgBar.getProgress() + 1);
            else
                prgBar.setProgress(prgBar.getProgress() - 1);
            prevPosition = position;

            switch(position) {
                case MAX_VIEWS - 1:
                    //btnDone.setVisibility(View.VISIBLE);
                    break;
                default:
                    //btnDone.setVisibility(View.INVISIBLE);
            }

        }

    } // TutorialPageChangeListener

} // TutorialActivity
