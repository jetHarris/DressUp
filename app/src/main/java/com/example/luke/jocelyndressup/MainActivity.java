package com.example.luke.jocelyndressup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.R.attr.tag;

public class MainActivity extends AppCompatActivity {
    //variables to keep track of the total number of items for each category
    int numberOfFeet = 3;
    int numberOfLegs = 3;
    int numberOfTorsos = 3;
    int numberOfHeads = 3;

    //variables to keep track of what images are currently displayed
    int currentFeetImage = 0;
    int currentLegsImage = 0;
    int currentTorsoImage = 0;
    int currentHeadImage = 0;

    private String m_Text = "";
    private DBAdapter db;
    ArrayList<Integer> heads = new ArrayList<Integer>();
    ArrayList<Integer> feet = new ArrayList<Integer>();
    ArrayList<Integer> legs = new ArrayList<Integer>();
    ArrayList<Integer> torsos = new ArrayList<Integer>();
    ArrayList<Float> headPrices = new ArrayList<Float>();
    ArrayList<Float> feetPrices = new ArrayList<Float>();
    ArrayList<Float> legPrices = new ArrayList<Float>();
    ArrayList<Float> torsoPrices = new ArrayList<Float>();
    TextView priceView;
    TextView taxView;
    TextView totalView;
    ImageView headImage;
    ImageView torsoImage;
    ImageView legsImage;
    ImageView feetImage;
    private static final int SWIPE_MIN_DISTANCE = 20;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;

    private float runningPrice = 0;
    boolean outfitOnDisplay = false;
    String oName;
    private Context context;

    private final GestureDetector headDetector = new GestureDetector(this.context, new GestureListenerHead());
    private final GestureDetector torsoDetector = new GestureDetector(this.context, new GestureListenerTorso());
    private final GestureDetector legsDetector = new GestureDetector(this.context, new GestureListenerLegs());
    private final GestureDetector feetDetector = new GestureDetector(this.context, new GestureListenerFeet());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBAdapter(this);
        //get the existing database file or from assets folder if doesn't exist
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();
                //copy from the db from the assets fodler
                CopyDB(getBaseContext().getAssets().open("mydb"), new FileOutputStream(destPath + "/MyDB"));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        priceView = (TextView) findViewById(R.id.priceText);
        taxView = (TextView) findViewById(R.id.taxText);
        totalView = (TextView) findViewById(R.id.totalText);
        headImage = (ImageView) findViewById(R.id.imageHead);
        torsoImage = (ImageView) findViewById(R.id.imageTorso);
        legsImage = (ImageView) findViewById(R.id.imageLegs);
        feetImage = (ImageView) findViewById(R.id.imageFeet);

        headImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                headDetector.onTouchEvent(event);
                return true;
            }
        });

        torsoImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                torsoDetector.onTouchEvent(event);
                return true;
            }
        });

        legsImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                legsDetector.onTouchEvent(event);
                return true;
            }
        });

        feetImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                feetDetector.onTouchEvent(event);
                return true;
            }
        });


        setArrays();

        //get intent which would be from the outfits page
        //then use the outfit name to set the images appropriately
        if (getIntent().getExtras() != null) {
            oName = getIntent().getExtras().getString("OutfitName");
            if (oName != null) {
                setOutfitByName(oName);
                outfitOnDisplay = true;
            } else {
                if (getIntent().getExtras().getString("type") != "") {
                    currentHeadImage = heads.indexOf(getIntent().getExtras().getInt("head"));
                    currentTorsoImage = torsos.indexOf(getIntent().getExtras().getInt("torso"));
                    currentLegsImage = legs.indexOf(getIntent().getExtras().getInt("legs"));
                    currentFeetImage = feet.indexOf(getIntent().getExtras().getInt("feet"));
                }
            }
        }

        if (outfitOnDisplay) {
            TextView label = (TextView) findViewById(R.id.outfitLabel);
            TextView nameLabel = (TextView) findViewById(R.id.outfitNameText);
            Button removeOutfit = (Button) findViewById(R.id.removeOutfitBtn);
            removeOutfit.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
            nameLabel.setVisibility(View.VISIBLE);
            nameLabel.setText(oName);
        }


        recalculatePrice();
        setImages();


    }

    public void recalculatePrice() {
        runningPrice = 0;
        runningPrice += headPrices.get(currentHeadImage);
        runningPrice += torsoPrices.get(currentTorsoImage);
        runningPrice += legPrices.get(currentLegsImage);
        runningPrice += feetPrices.get(currentFeetImage);

        double roundedPrice = Math.round(runningPrice * 100.0) / 100.0;
        double tax = Math.round((runningPrice * 0.13) * 100.0) / 100.0;
        double total = Math.round((runningPrice * 1.13) * 100.0) / 100.0;
        priceView.setText("" + roundedPrice);
        taxView.setText("" + tax);
        totalView.setText("" + total);

    }

    public void setImages() {
        ImageView iv = (ImageView) findViewById(R.id.imageHead);
        String fileName = "a" + heads.get(currentHeadImage);
        int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
        iv.setImageResource(resID);

        ImageView iv1 = (ImageView) findViewById(R.id.imageTorso);
        String fileName1 = "a" + torsos.get(currentTorsoImage);
        int resID1 = getResources().getIdentifier(fileName1, "drawable", getPackageName());
        iv1.setImageResource(resID1);

        ImageView iv2 = (ImageView) findViewById(R.id.imageLegs);
        String fileName2 = "a" + legs.get(currentLegsImage);
        int resID2 = getResources().getIdentifier(fileName2, "drawable", getPackageName());
        iv2.setImageResource(resID2);

        ImageView iv3 = (ImageView) findViewById(R.id.imageFeet);
        String fileName3 = "a" + feet.get(currentFeetImage);
        int resID3 = getResources().getIdentifier(fileName3, "drawable", getPackageName());
        iv3.setImageResource(resID3);
    }

    public void setArrays() {
        db.open();
        Cursor c;
        //get all the head id's
        c = db.getItemsByType("head");
        if (c.moveToFirst()) {
            do {
                heads.add(c.getInt(0));
                headPrices.add(c.getFloat(2));
                //DisplayContact(c);
            } while (c.moveToNext());
        }
        //get all the torso ids
        c = db.getItemsByType("torso");
        if (c.moveToFirst()) {
            do {
                torsos.add(c.getInt(0));
                torsoPrices.add(c.getFloat(2));
                //DisplayContact(c);
            } while (c.moveToNext());
        }
        //get all the leg ids
        c = db.getItemsByType("legs");
        if (c.moveToFirst()) {
            do {
                legs.add(c.getInt(0));
                legPrices.add(c.getFloat(2));
                //DisplayContact(c);
            } while (c.moveToNext());
        }
        //get all the feet ids
        c = db.getItemsByType("feet");
        if (c.moveToFirst()) {
            do {
                feet.add(c.getInt(0));
                feetPrices.add(c.getFloat(2));
                //DisplayContact(c);
            } while (c.moveToNext());
        }


        numberOfFeet = feet.size() - 1;
        numberOfLegs = legs.size() - 1;
        numberOfTorsos = torsos.size() - 1;
        numberOfHeads = heads.size() - 1;


        db.close();
    }

    //used to create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //click events for the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            //if about was clicked then go to the about page
            case R.id.action_about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;

            //if the outfits menu item was clicked then go to the outfits page
            case R.id.action_outfits:
                Intent i2 = new Intent(this, OutfitsActivity.class);
                startActivity(i2);
                break;

        }
        return true;

    }

    //a method used to set all the images displaying an outfit, given the name of the outfit
    public void setOutfitByName(String name) {
        //call the database method to first actually get outfit by name
        Cursor c;
        db.open();
        c = db.getOutfitByName(name);

        if (c.moveToFirst()) {

            //get the id's from the database then set the appropriate images
            currentHeadImage = heads.indexOf(c.getInt(2));
            ImageView iv = (ImageView) findViewById(R.id.imageHead);
            String fileName = "a" + c.getInt(2);
            int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
            iv.setImageResource(resID);

            currentTorsoImage = torsos.indexOf(c.getInt(3));
            ImageView iv1 = (ImageView) findViewById(R.id.imageTorso);
            String fileName1 = "a" + c.getInt(3);
            int resID1 = getResources().getIdentifier(fileName1, "drawable", getPackageName());
            iv1.setImageResource(resID1);

            currentLegsImage = legs.indexOf(c.getInt(4));
            ImageView iv2 = (ImageView) findViewById(R.id.imageLegs);
            String fileName2 = "a" + c.getInt(4);
            int resID2 = getResources().getIdentifier(fileName2, "drawable", getPackageName());
            iv2.setImageResource(resID2);

            currentFeetImage = feet.indexOf(c.getInt(5));
            ImageView iv3 = (ImageView) findViewById(R.id.imageFeet);
            String fileName3 = "a" + c.getInt(5);
            int resID3 = getResources().getIdentifier(fileName3, "drawable", getPackageName());
            iv3.setImageResource(resID3);
        } else {
            Toast.makeText(this, "Get failed on " + name, Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //copyDB method given to us in class
    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //Copy one byte at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();  //close streams
        outputStream.close();
    }


    //onClick method to handle all the click events from the buttons on the page.
    public void onClick(View view) {
        switch (view.getId()) {
            //if the next head button is clicked then switch the image of the head to the next one
            case R.id.nextHeadBtn:
                if (currentHeadImage < numberOfHeads)
                    ++currentHeadImage;
                else
                    currentHeadImage = 0;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageHead);
                String fileName = "a" + heads.get(currentHeadImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the prev head button is clicked then switch the image of the head to the previous one
            case R.id.prevHeadBtn:
                if (currentHeadImage > 0)
                    --currentHeadImage;
                else
                    currentHeadImage = numberOfHeads;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageHead);
                String fileName = "a" + heads.get(currentHeadImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the next torso button is clicked then switch the image of the torso to the next one
            case R.id.nextTorsoBtn:
                if (currentTorsoImage < numberOfTorsos)
                    ++currentTorsoImage;
                else
                    currentTorsoImage = 0;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageTorso);
                String fileName = "a" + torsos.get(currentTorsoImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the prev torso button is clicked then switch the image of the torso to the previous one
            case R.id.prevTorsoBtn:
                if (currentTorsoImage > 0)
                    --currentTorsoImage;
                else
                    currentTorsoImage = numberOfTorsos;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageTorso);
                String fileName = "a" + torsos.get(currentTorsoImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the next legs button is clicked then switch the image of the legs to the next one
            case R.id.nextLegsBtn:
                if (currentLegsImage < numberOfLegs)
                    ++currentLegsImage;
                else
                    currentLegsImage = 0;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageLegs);
                String fileName = "a" + legs.get(currentLegsImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the prev legs button is clicked then switch the image of the legs to the previous one
            case R.id.prevLegsBtn:
                if (currentLegsImage > 0)
                    --currentLegsImage;
                else
                    currentLegsImage = numberOfLegs;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageLegs);
                String fileName = "a" + legs.get(currentLegsImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the next feet button is clicked then switch the image of the feet to the next one
            case R.id.nextFeetBtn:
                if (currentFeetImage < numberOfFeet)
                    ++currentFeetImage;
                else
                    currentFeetImage = 0;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageFeet);
                String fileName = "a" + feet.get(currentFeetImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;
            //if the prev feet button is clicked then switch the image of the feet to the previous one
            case R.id.prevFeetBtn:
                if (currentFeetImage > 0)
                    --currentFeetImage;
                else
                    currentFeetImage = numberOfFeet;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageFeet);
                String fileName = "a" + feet.get(currentFeetImage);
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
            recalculatePrice();
                break;

            //if the save button is clicked
            case R.id.buttonSave:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please input name of outfit");

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        //add a contact into the database
                        if (m_Text != "") {
                            db.open();
                            long id = db.insertOutfit(m_Text, heads.get(currentHeadImage), torsos.get(currentTorsoImage),
                                    legs.get(currentLegsImage), feet.get(currentFeetImage));
                            db.close();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


                break;
            //if the assemble button is pressed then the record the current state of the images and send that to the assembled page
            case R.id.buttonAssemble:
                Intent i3 = new Intent(this, AssembledActivity.class);
                i3.putExtra("head", heads.get(currentHeadImage));
                i3.putExtra("torso", torsos.get(currentTorsoImage));
                i3.putExtra("legs", legs.get(currentLegsImage));
                i3.putExtra("feet", feet.get(currentFeetImage));
                startActivity(i3);
                break;
            case R.id.removeOutfitBtn:
                db.open();
                db.deleteOutfitByName(oName);
                db.close();
                TextView label = (TextView) findViewById(R.id.outfitLabel);
                TextView nameLabel = (TextView) findViewById(R.id.outfitNameText);
                Button removeOutfit = (Button) findViewById(R.id.removeOutfitBtn);
                removeOutfit.setVisibility(View.INVISIBLE);
                label.setVisibility(View.INVISIBLE);
                nameLabel.setVisibility(View.INVISIBLE);
                break;

            case R.id.imageHead:
                Intent i4 = new Intent(this, ItemListActivity.class);
                i4.putExtra("type", "head");
                i4.putExtra("head", heads.get(currentHeadImage));
                i4.putExtra("torso", torsos.get(currentTorsoImage));
                i4.putExtra("legs", legs.get(currentLegsImage));
                i4.putExtra("feet", feet.get(currentFeetImage));
                startActivity(i4);
                break;
            case R.id.imageTorso:
                Intent i5 = new Intent(this, ItemListActivity.class);
                i5.putExtra("type", "torso");
                i5.putExtra("head", heads.get(currentHeadImage));
                i5.putExtra("torso", torsos.get(currentTorsoImage));
                i5.putExtra("legs", legs.get(currentLegsImage));
                i5.putExtra("feet", feet.get(currentFeetImage));
                startActivity(i5);
                break;
            case R.id.imageLegs:
                Intent i6 = new Intent(this, ItemListActivity.class);
                i6.putExtra("type", "legs");
                i6.putExtra("head", heads.get(currentHeadImage));
                i6.putExtra("torso", torsos.get(currentTorsoImage));
                i6.putExtra("legs", legs.get(currentLegsImage));
                i6.putExtra("feet", feet.get(currentFeetImage));
                startActivity(i6);
                break;
            case R.id.imageFeet:
                Intent i7 = new Intent(this, ItemListActivity.class);
                i7.putExtra("type", "feet");
                i7.putExtra("head", heads.get(currentHeadImage));
                i7.putExtra("torso", torsos.get(currentTorsoImage));
                i7.putExtra("legs", legs.get(currentLegsImage));
                i7.putExtra("feet", feet.get(currentFeetImage));
                startActivity(i7);
                break;

        }
    }

    public void clickClothing(String type) {
        Intent i4 = new Intent(this, ItemListActivity.class);
        i4.putExtra("type", type);
        i4.putExtra("head", heads.get(currentHeadImage));
        i4.putExtra("torso", torsos.get(currentTorsoImage));
        i4.putExtra("legs", legs.get(currentLegsImage));
        i4.putExtra("feet", feet.get(currentFeetImage));
        startActivity(i4);
    }

    public void flingClothing(boolean rightFling, String type) {
        if (rightFling) {
            switch (type) {
                case "head":
                    if (currentHeadImage < numberOfHeads)
                        ++currentHeadImage;
                    else
                        currentHeadImage = 0;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageHead);
                    String fileName = "a" + heads.get(currentHeadImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
                case "torso":
                    if (currentTorsoImage < numberOfTorsos)
                        ++currentTorsoImage;
                    else
                        currentTorsoImage = 0;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageTorso);
                    String fileName = "a" + torsos.get(currentTorsoImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
                case "legs":
                    if (currentLegsImage < numberOfLegs)
                        ++currentLegsImage;
                    else
                        currentLegsImage = 0;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageLegs);
                    String fileName = "a" + legs.get(currentLegsImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
                case "feet":
                    if (currentFeetImage < numberOfFeet)
                        ++currentFeetImage;
                    else
                        currentFeetImage = 0;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageFeet);
                    String fileName = "a" + feet.get(currentFeetImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
            }

        } else {
            switch (type) {
                case "head":
                    if (currentHeadImage > 0)
                        --currentHeadImage;
                    else
                        currentHeadImage = numberOfHeads;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageHead);
                    String fileName = "a" + heads.get(currentHeadImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
                case "torso":
                    if (currentTorsoImage > 0)
                        --currentTorsoImage;
                    else
                        currentTorsoImage = numberOfTorsos;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageTorso);
                    String fileName = "a" + torsos.get(currentTorsoImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
                case "legs":
                    if (currentLegsImage > 0)
                        --currentLegsImage;
                    else
                        currentLegsImage = numberOfLegs;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageLegs);
                    String fileName = "a" + legs.get(currentLegsImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
                case "feet":
                    if (currentFeetImage > 0)
                        --currentFeetImage;
                    else
                        currentFeetImage = numberOfFeet;
                {
                    ImageView iv = (ImageView) findViewById(R.id.imageFeet);
                    String fileName = "a" + feet.get(currentFeetImage);
                    int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                    iv.setImageResource(resID);
                }
                recalculatePrice();
                break;
            }
        }

    }


    private final class GestureListenerHead extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            clickClothing("head");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true,"head");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false,"head");
                return true;
            }
            return true;
        }
    }

    private final class GestureListenerTorso extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            clickClothing("torso");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true,"torso");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false,"torso");
                return true;
            }
            return true;
        }
    }

    private final class GestureListenerLegs extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            clickClothing("legs");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true,"legs");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false,"legs");
                return true;
            }
            return true;
        }
    }

    private final class GestureListenerFeet extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            clickClothing("feet");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true,"feet");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false,"feet");
                return true;
            }
            return true;
        }
    }


}
