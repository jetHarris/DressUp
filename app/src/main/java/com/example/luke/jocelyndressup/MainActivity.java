package com.example.luke.jocelyndressup;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    //variables to keep track of the total number of items for each category
    int numberOfFeet = 3;
    int numberOfLegs = 3;
    int numberOfTorsos = 3;
    int numberOfHeads = 3;

    //variables to keep track of what images are currently displayed
    int currentFeetImage = 3;
    int currentLegsImage = 3;
    int currentTorsoImage = 3;
    int currentHeadImage = 3;

    private String m_Text = "";
    private DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBAdapter(this);
        //get the existing database file or from assets folder if doesn't exist
        try
        {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if(!f.exists()){
                f.mkdirs();
                f.createNewFile();
                //copy from the db from the assets fodler
                CopyDB(getBaseContext().getAssets().open("mydb"), new FileOutputStream(destPath + "/MyDB"));
            }
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        //get intent which would be from the outfits page
        //then use the outfit name to set the images appropriately
        if( getIntent().getExtras() != null)
        {
            String oName = getIntent().getExtras().getString("OutfitName");
            if (oName != "")
            {
                setOutfitByName(oName);
            }
        }
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

        switch(item.getItemId()){
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
    public void setOutfitByName(String name)
    {
        //call the database method to first actually get outfit by name
        Cursor c;
        db.open();
        c = db.getOutfitByName(name);

        if(c.moveToFirst()){

            //get the id's from the database then set the appropriate images
            currentHeadImage = Integer.parseInt(c.getString(2));
            ImageView iv = (ImageView) findViewById(R.id.imageHead);
            String fileName = "head" + currentHeadImage;
            int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
            iv.setImageResource(resID);

            currentTorsoImage = Integer.parseInt(c.getString(3));
            ImageView iv1 = (ImageView) findViewById(R.id.imageTorso);
            String fileName1 = "torso" + currentTorsoImage;
            int resID1 = getResources().getIdentifier(fileName1, "drawable", getPackageName());
            iv1.setImageResource(resID1);

            currentLegsImage = Integer.parseInt(c.getString(4));
            ImageView iv2 = (ImageView) findViewById(R.id.imageLegs);
            String fileName2 = "legs" + currentLegsImage;
            int resID2 = getResources().getIdentifier(fileName2, "drawable", getPackageName());
            iv2.setImageResource(resID2);

            currentFeetImage = Integer.parseInt(c.getString(5));
            ImageView iv3 = (ImageView) findViewById(R.id.imageFeet);
            String fileName3 = "feet" + currentFeetImage;
            int resID3 = getResources().getIdentifier(fileName3, "drawable", getPackageName());
            iv3.setImageResource(resID3);
        }
        else{
            Toast.makeText(this,"Get failed on " + name , Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    //copyDB method given to us in class
    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //Copy one byte at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0){
            outputStream.write(buffer,0,length);
        }
        inputStream.close();  //close streams
        outputStream.close();
    }

    //test method that should be implimented in the future
    //idea being to switch layout of this page when it goes through an orientation switch
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        System.out.println("TRIIIIIIGGGGERREED");
        super.onConfigurationChanged(newConfig);

        currentHeadImage = 1;
        ImageView iv = (ImageView) findViewById(R.id.imageHead);
        String fileName = "head" + currentHeadImage;
        int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
        iv.setImageResource(resID);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    //onClick method to handle all the click events from the buttons on the page.
    public void onClick(View view) {
        switch(view.getId()) {
            //if the next head button is clicked then switch the image of the head to the next one
            case R.id.nextHeadBtn:
                if(currentHeadImage < numberOfHeads)
                    ++currentHeadImage;
                else
                    currentHeadImage = 1;
                {
                ImageView iv = (ImageView) findViewById(R.id.imageHead);
                String fileName = "head" + currentHeadImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
                 }
                break;
            //if the prev head button is clicked then switch the image of the head to the previous one
            case R.id.prevHeadBtn:
                if(currentHeadImage > 1)
                    --currentHeadImage;
                else
                    currentHeadImage = numberOfHeads;
                {
                ImageView iv = (ImageView) findViewById(R.id.imageHead);
                String fileName = "head" + currentHeadImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
                 }
                break;
            //if the next torso button is clicked then switch the image of the torso to the next one
            case R.id.nextTorsoBtn:
                if(currentTorsoImage < numberOfTorsos)
                    ++currentTorsoImage;
                else
                    currentTorsoImage = 1;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageTorso);
                String fileName = "torso" + currentTorsoImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
                break;
            //if the prev torso button is clicked then switch the image of the torso to the previous one
            case R.id.prevTorsoBtn:
                if(currentTorsoImage > 1)
                    --currentTorsoImage;
                else
                    currentTorsoImage = numberOfTorsos;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageTorso);
                String fileName = "torso" + currentTorsoImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
                break;
            //if the next legs button is clicked then switch the image of the legs to the next one
            case R.id.nextLegsBtn:
                if(currentLegsImage < numberOfLegs)
                    ++currentLegsImage;
                else
                    currentLegsImage = 1;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageLegs);
                String fileName = "legs" + currentLegsImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
                break;
            //if the prev legs button is clicked then switch the image of the legs to the previous one
            case R.id.prevLegsBtn:
                if(currentLegsImage > 1)
                    --currentLegsImage;
                else
                    currentLegsImage = numberOfLegs;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageLegs);
                String fileName = "legs" + currentLegsImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
                break;
            //if the next feet button is clicked then switch the image of the feet to the next one
            case R.id.nextFeetBtn:
                if(currentFeetImage < numberOfFeet)
                    ++currentFeetImage;
                else
                    currentFeetImage = 1;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageFeet);
                String fileName = "feet" + currentFeetImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
                break;
            //if the prev feet button is clicked then switch the image of the feet to the previous one
            case R.id.prevFeetBtn:
                if(currentFeetImage > 1)
                    --currentFeetImage;
                else
                    currentFeetImage = numberOfFeet;
            {
                ImageView iv = (ImageView) findViewById(R.id.imageFeet);
                String fileName = "feet" + currentFeetImage;
                int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
                iv.setImageResource(resID);
            }
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
                        if(m_Text != "") {
                            db.open();
                            long id = db.insertOutfit(m_Text,currentHeadImage,currentTorsoImage,currentLegsImage,currentFeetImage);
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
                i3.putExtra("head", currentHeadImage);
                i3.putExtra("torso", currentTorsoImage);
                i3.putExtra("legs", currentLegsImage);
                i3.putExtra("feet", currentFeetImage);
                startActivity(i3);
                break;

        }
    }
}
