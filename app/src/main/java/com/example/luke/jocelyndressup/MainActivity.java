package com.example.luke.jocelyndressup;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
    ArrayList<String> headNames = new ArrayList<String>();
    ArrayList<String> feetNames = new ArrayList<String>();
    ArrayList<String> legNames = new ArrayList<String>();
    ArrayList<String> torsoNames = new ArrayList<String>();
    ArrayList<AsyncTask> tasks = new ArrayList<AsyncTask>();
    ArrayList<ImageView> frontFacingViews = new ArrayList<ImageView>();
    ArrayList<String> fileNames = new ArrayList<String>();
    TextView priceView;
    TextView taxView;
    TextView totalView;


    ImageView headImage;
    ImageView torsoImage;
    ImageView legsImage;
    ImageView feetImage;

    ImageView headImageHidden;
    ImageView torsoImageHidden;
    ImageView legsImageHidden;
    ImageView feetImageHidden;

    TextView headPriceLabel;
    TextView torsoPriceLabel;
    TextView legsPriceLabel;
    TextView feetPriceLabel;

    TextView headNameLabel;
    TextView torsoNameLabel;
    TextView legsNameLabel;
    TextView feetNameLabel;


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

        System.gc();
        db = new DBAdapter(this);
        context = this.getApplicationContext();
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

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        imageView.setImageResource(R.drawable.tdicon);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);


        priceView = (TextView) findViewById(R.id.totalText);
        taxView = (TextView) findViewById(R.id.taxText);
        totalView = (TextView) findViewById(R.id.overallTotalText);

        headPriceLabel = (TextView) findViewById(R.id.headPrice);
        torsoPriceLabel = (TextView) findViewById(R.id.torsoPrice);
        legsPriceLabel = (TextView) findViewById(R.id.legsPrice);
        feetPriceLabel = (TextView) findViewById(R.id.feetPrice);

        headNameLabel = (TextView) findViewById(R.id.headName);
        torsoNameLabel = (TextView) findViewById(R.id.torsoName);
        legsNameLabel = (TextView) findViewById(R.id.legsName);
        feetNameLabel = (TextView) findViewById(R.id.feetName);


        headImage = (ImageView) findViewById(R.id.imageHead);
        torsoImage = (ImageView) findViewById(R.id.imageTorso);
        legsImage = (ImageView) findViewById(R.id.imageLegs);
        feetImage = (ImageView) findViewById(R.id.imageFeet);

        headImageHidden = (ImageView) findViewById(R.id.imageHeadTemp);
        torsoImageHidden = (ImageView) findViewById(R.id.imageTorsoTemp);
        legsImageHidden = (ImageView) findViewById(R.id.imageLegsTemp);
        feetImageHidden = (ImageView) findViewById(R.id.imageFeetTemp);

        frontFacingViews.add(headImage);
        frontFacingViews.add(headImageHidden);
        frontFacingViews.add(torsoImage);
        frontFacingViews.add(torsoImageHidden);
        frontFacingViews.add(legsImage);
        frontFacingViews.add(legsImageHidden);
        frontFacingViews.add(feetImage);
        frontFacingViews.add(feetImageHidden);

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
        } else {

        }

        if (outfitOnDisplay) {
            TextView label = (TextView) findViewById(R.id.outfitLabel);
            TextView nameLabel = (TextView) findViewById(R.id.outfitNameText);
            Button removeOutfit = (Button) findViewById(R.id.saveBtn);
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

        headPriceLabel.setText("$" + String.format("%.2f", headPrices.get(currentHeadImage)));
        torsoPriceLabel.setText("$" + String.format("%.2f", torsoPrices.get(currentTorsoImage)));
        legsPriceLabel.setText("$" + String.format("%.2f", legPrices.get(currentLegsImage)));
        feetPriceLabel.setText("$" + String.format("%.2f", feetPrices.get(currentFeetImage)));


        headNameLabel.setText("" + headNames.get(currentHeadImage));
        torsoNameLabel.setText("" + torsoNames.get(currentTorsoImage));
        legsNameLabel.setText("" + legNames.get(currentLegsImage));
        feetNameLabel.setText("" + feetNames.get(currentFeetImage));

        double roundedPrice = Math.round(runningPrice * 100.0) / 100.0;
        double tax = Math.round((runningPrice * 0.13) * 100.0) / 100.0;
        double total = Math.round((runningPrice * 1.13) * 100.0) / 100.0;

        priceView.setText("$" + String.format("%.2f", roundedPrice));
        taxView.setText("$" + String.format("%.2f", tax));
        totalView.setText("$" + String.format("%.2f", total));

    }

    public void setImages() {

        fileNames.clear();
        fileNames.add(headNames.get(currentHeadImage));
        fileNames.add(torsoNames.get(currentTorsoImage));
        fileNames.add(legNames.get(currentLegsImage));
        fileNames.add(feetNames.get(currentFeetImage));


        ImageLoaderSuperPacket packet = new ImageLoaderSuperPacket(fileNames, frontFacingViews, context);
        SuperImageLoader loader = new SuperImageLoader();
        loader.execute(packet);

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
                headNames.add(c.getString(1));
            } while (c.moveToNext());
        }
        //get all the torso ids
        c = db.getItemsByType("torso");
        if (c.moveToFirst()) {
            do {
                torsos.add(c.getInt(0));
                torsoPrices.add(c.getFloat(2));
                torsoNames.add(c.getString(1));
            } while (c.moveToNext());
        }
        //get all the leg ids
        c = db.getItemsByType("legs");
        if (c.moveToFirst()) {
            do {
                legs.add(c.getInt(0));
                legPrices.add(c.getFloat(2));
                legNames.add(c.getString(1));
            } while (c.moveToNext());
        }
        //get all the feet ids
        c = db.getItemsByType("feet");
        if (c.moveToFirst()) {
            do {
                feet.add(c.getInt(0));
                feetPrices.add(c.getFloat(2));
                feetNames.add(c.getString(1));
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
//            //if about was clicked then go to the about page
//            case R.id.action_about:
//                Intent i = new Intent(this, AboutActivity.class);
//                startActivity(i);
//                break;
//
//            //if the outfits menu item was clicked then go to the outfits page
//            case R.id.action_outfits:
//                Intent i2 = new Intent(this, OutfitsActivity.class);
//                startActivity(i2);
//                break;
//
//            case R.id.action_camera:
//                Intent i6 = new Intent(this, CameraActivity.class);
//                startActivity(i6);
//                break;
//
//            case R.id.action_read_nfc:
//                Intent i4 = new Intent(this, ReadNFCItemActivity.class);
//                startActivity(i4);
//                break;
//
//            case R.id.action_write_nfc:
//                Intent i5 = new Intent(this, WriteNFCItemActivity.class);
//                startActivity(i5);
//                break;

            case R.id.action_tutorial:
                SharedPreferences spAppHistory = getSharedPreferences("AppHistory", MODE_PRIVATE);
                SharedPreferences.Editor editor = spAppHistory.edit();
                editor.putBoolean("help", true);
                editor.commit();
                Intent i6 = new Intent(this, TutorialActivity.class);
                startActivity(i6);
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

            currentTorsoImage = torsos.indexOf(c.getInt(3));

            currentLegsImage = legs.indexOf(c.getInt(4));

            currentFeetImage = feet.indexOf(c.getInt(5));
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
    public void saveTextDisplay(boolean saveTracker){
        if (saveTracker) {
            Toast.makeText(this, "Outfit Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Save Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {

            //if the save button is clicked
            case R.id.saveBtn:
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
                            oName = m_Text;
                            outfitOnDisplay = true;
                            db.open();
                            long id = db.insertOutfit(m_Text, heads.get(currentHeadImage), torsos.get(currentTorsoImage),
                                    legs.get(currentLegsImage), feet.get(currentFeetImage));
                            db.close();
                            saveTextDisplay(true);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        saveTextDisplay(false);
                    }
                });

                builder.show();




                break;
            case R.id.removeOutfitBtn:
                if (outfitOnDisplay) {
                    db.open();
                    db.deleteOutfitByName(oName);
                    db.close();
                    TextView label = (TextView) findViewById(R.id.outfitLabel);
                    TextView nameLabel = (TextView) findViewById(R.id.outfitNameText);
                    Button removeOutfit = (Button) findViewById(R.id.saveBtn);
                    removeOutfit.setVisibility(View.INVISIBLE);
                    label.setVisibility(View.INVISIBLE);
                    nameLabel.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(this, "An outfit must be saved before it can be deleted", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.AboutBtn:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;

            //if the outfits menu item was clicked then go to the outfits page
            case R.id.OutfitsBtn:
                Intent i2 = new Intent(this, OutfitsActivity.class);
                startActivity(i2);
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

    public void changeImageLeft(ImageView iv, String filename, ImageView iv2) {
        ImageLoaderPacket packet = new ImageLoaderPacket(filename, iv, context, iv2);
        ImageLoaderLeftAnim il = new ImageLoaderLeftAnim();
        tasks.add(il);
        il.execute(packet);

        System.gc();
        recalculatePrice();
    }

    public void changeImageRight(ImageView iv, String filename, ImageView iv2) {

        ImageLoaderPacket packet = new ImageLoaderPacket(filename, iv, context, iv2);
        ImageLoaderRightAnim il = new ImageLoaderRightAnim();
        tasks.add(il);
        il.execute(packet);

        System.gc();

        recalculatePrice();

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
                    changeImageRight(headImage, headNames.get(currentHeadImage), headImageHidden);
                }
                break;
                case "torso":
                    if (currentTorsoImage < numberOfTorsos)
                        ++currentTorsoImage;
                    else
                        currentTorsoImage = 0;
                {
                    changeImageRight(torsoImage, torsoNames.get(currentTorsoImage), torsoImageHidden);
                }
                break;
                case "legs":
                    if (currentLegsImage < numberOfLegs)
                        ++currentLegsImage;
                    else
                        currentLegsImage = 0;
                {
                    changeImageRight(legsImage, legNames.get(currentLegsImage), legsImageHidden);
                }
                break;
                case "feet":
                    if (currentFeetImage < numberOfFeet)
                        ++currentFeetImage;
                    else
                        currentFeetImage = 0;
                {
                    changeImageRight(feetImage, feetNames.get(currentFeetImage), feetImageHidden);
                }
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
                    changeImageLeft(headImage, headNames.get(currentHeadImage), headImageHidden);
                }
                break;
                case "torso":
                    if (currentTorsoImage > 0)
                        --currentTorsoImage;
                    else
                        currentTorsoImage = numberOfTorsos;
                {
                    changeImageLeft(torsoImage, torsoNames.get(currentTorsoImage), torsoImageHidden);
                }
                break;
                case "legs":
                    if (currentLegsImage > 0)
                        --currentLegsImage;
                    else
                        currentLegsImage = numberOfLegs;
                {
                    changeImageLeft(legsImage, legNames.get(currentLegsImage), legsImageHidden);
                }
                break;
                case "feet":
                    if (currentFeetImage > 0)
                        --currentFeetImage;
                    else
                        currentFeetImage = numberOfFeet;
                {
                    changeImageLeft(feetImage, feetNames.get(currentFeetImage), feetImageHidden);
                }
                break;
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < tasks.size(); ++i) {
            if (tasks.get(i) != null) {
                tasks.get(i).cancel(true);
            }
        }
        System.gc();

    }

    private final class GestureListenerHead extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            clickClothing("head");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            clickClothing("head");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true, "head");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false, "head");
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
        public boolean onDoubleTap(MotionEvent e) {
            clickClothing("torso");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true, "torso");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false, "torso");
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
        public boolean onDoubleTap(MotionEvent e) {
            clickClothing("legs");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true, "legs");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false, "legs");
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
        public boolean onDoubleTap(MotionEvent e) {
            clickClothing("feet");
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(true, "feet");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                flingClothing(false, "feet");
                return true;
            }
            return true;
        }
    }


}
