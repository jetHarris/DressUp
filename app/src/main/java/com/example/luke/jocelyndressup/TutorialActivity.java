package com.example.luke.jocelyndressup;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TutorialActivity extends AppCompatActivity {
    ViewPager viewPager;
    ProgressBar prgBar;
    Button btnDone;
    int prevPosition;
    private boolean help;
    private static final int MAX_VIEWS = 5;//Total number of tutorial images
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getSupportActionBar().hide(); // Get rid of actionbar manually

        context = this.getApplicationContext();
        SharedPreferences spAppHistory = getSharedPreferences("AppHistory", MODE_PRIVATE);
        boolean previously_run = spAppHistory.getBoolean("previously_run", false);

        btnDone = (Button) findViewById(R.id.btn_Done);
        help = false;
        if(getIntent().getExtras() != null) {
            help = getIntent().getExtras().getBoolean("help", false);
        }

        if(previously_run && help == false) {
            Intent i = new Intent(this, BufferActivity.class);
            startActivity(i);
        }
        else {
            if(!help) {
                firstTimeInstall();
                btnDone.setEnabled(false);
                Toast.makeText(this, "Doing first time install of images in background", Toast.LENGTH_SHORT).show();
            }
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TutorialPagerAdapter());
        viewPager.setOnPageChangeListener(new TutorialPageChangeListener());

        prgBar = (ProgressBar) findViewById(R.id.prgBar);
        prgBar.setMax(MAX_VIEWS - 1);
        prevPosition = -1;


        //btnDone.setVisibility(View.INVISIBLE);
    } // onCreate

    private void firstTimeInstall() {
        InstallPacket packet = new InstallPacket(context,btnDone);
        FirstRunImageInstaller installer = new FirstRunImageInstaller();
        installer.execute(packet);
//        //take all the images from the drawable folder and put them in the internal storage
//        Bitmap feet1 = BitmapFactory.decodeResource(getResources(),R.drawable.feet1);
//        saveToInternalStorage(feet1,"feet1");
//        feet1.recycle();
//
//        Bitmap feet2 = BitmapFactory.decodeResource(getResources(),R.drawable.feet2);
//        saveToInternalStorage(feet2,"feet2");
//        feet2.recycle();
//
//        Bitmap feet3 = BitmapFactory.decodeResource(getResources(),R.drawable.feet3);
//        saveToInternalStorage(feet3,"feet3");
//        feet3.recycle();
//
//        Bitmap head1 = BitmapFactory.decodeResource(getResources(),R.drawable.head1);
//        saveToInternalStorage(head1,"head1");
//        head1.recycle();
//
//        Bitmap head2 = BitmapFactory.decodeResource(getResources(),R.drawable.head2);
//        saveToInternalStorage(head2,"head2");
//        head2.recycle();
//
//        Bitmap head3 = BitmapFactory.decodeResource(getResources(),R.drawable.head3);
//        saveToInternalStorage(head3,"head3");
//        head3.recycle();
//
//        Bitmap torso1 = BitmapFactory.decodeResource(getResources(),R.drawable.torso1);
//        saveToInternalStorage(torso1,"torso1");
//        torso1.recycle();
//
//        Bitmap torso2 = BitmapFactory.decodeResource(getResources(),R.drawable.torso2);
//        saveToInternalStorage(torso2,"torso2");
//        torso2.recycle();
//
//        Bitmap torso3 = BitmapFactory.decodeResource(getResources(),R.drawable.torso3);
//        saveToInternalStorage(torso3,"torso3");
//        torso3.recycle();
//
//        Bitmap legs1 = BitmapFactory.decodeResource(getResources(),R.drawable.legs1);
//        saveToInternalStorage(legs1,"legs1");
//        legs1.recycle();
//
//        Bitmap legs2 = BitmapFactory.decodeResource(getResources(),R.drawable.legs2);
//        saveToInternalStorage(legs2,"legs2");
//        legs2.recycle();
//
//        Bitmap legs3 = BitmapFactory.decodeResource(getResources(),R.drawable.legs3);
//        saveToInternalStorage(legs3,"legs3");
//        legs3.recycle();


    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private void saveToInternalStorage(Bitmap bitmapImage, String filename){

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename+".bmp", Context.MODE_PRIVATE);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 10, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    imageView.setImageResource(R.drawable.home_tutorial_1);
                    imageView.setBackgroundColor(Color.WHITE);
                    break;

                case 1:
                    imageView.setImageResource(R.drawable.home_tutorial_2);
                    imageView.setBackgroundColor(Color.WHITE);
                    break;

                case 2:

                    imageView.setBackgroundColor(Color.WHITE);
                    break;

                case 3:
                    imageView.setImageResource(R.drawable.home_tutorial_3);
                    imageView.setBackgroundColor(Color.WHITE);
                    break;

                case 4:
                    imageView.setBackgroundColor(Color.WHITE);
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
