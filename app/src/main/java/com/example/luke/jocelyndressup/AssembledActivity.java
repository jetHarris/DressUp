package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AssembledActivity extends AppCompatActivity {

    //test comment
    //numbers to keep track of the currently displayed image
    int currentFeetImage = 3;
    int currentLegsImage = 3;
    int currentTorsoImage = 3;
    int currentHeadImage = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembled);

        //if there was an intent passed to this page
        if( getIntent().getExtras() != null)
        {
            //get what all the image choices are
            currentHeadImage = getIntent().getExtras().getInt("head");
            currentTorsoImage = getIntent().getExtras().getInt("torso");
            currentLegsImage = getIntent().getExtras().getInt("legs");
            currentFeetImage = getIntent().getExtras().getInt("feet");

            //using the image choices, set the appropriate image
            ImageView iv = (ImageView) findViewById(R.id.imageHead);
            String fileName = "a" + currentHeadImage;
            int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
            iv.setImageResource(resID);

            ImageView iv2 = (ImageView) findViewById(R.id.imageTorso);
            String fileName2 = "a" + currentTorsoImage;
            int resID2 = getResources().getIdentifier(fileName2, "drawable", getPackageName());
            iv2.setImageResource(resID2);

            ImageView iv3 = (ImageView) findViewById(R.id.imageLegs);
            String fileName3 = "a" + currentLegsImage;
            int resID3 = getResources().getIdentifier(fileName3, "drawable", getPackageName());
            iv3.setImageResource(resID3);

            ImageView iv4 = (ImageView) findViewById(R.id.imageFeet);
            String fileName4 = "a" + currentFeetImage;
            int resID4 = getResources().getIdentifier(fileName4, "drawable", getPackageName());
            iv4.setImageResource(resID4);
        }
    }


    //when the home button is clicked, go to the home page
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonHome:
                Intent i2 = new Intent(this, MainActivity.class);
                startActivity(i2);
                break;
        }
    }
}
