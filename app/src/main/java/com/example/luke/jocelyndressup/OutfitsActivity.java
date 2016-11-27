package com.example.luke.jocelyndressup;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class OutfitsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DBAdapter db;
    //linear layout that holds the two buttons and the header
    private LinearLayout ll;
    //list view that holds all the outfit names
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions( android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
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


        db = new DBAdapter(this);
        //this page requires the database and so it have to be found/created
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


        ll = (LinearLayout) findViewById(R.id.main_ll);
        lv = new ListView(this);
        ll.addView(lv);

        Cursor c;
        ArrayList<String> outfitNames = new ArrayList<String>();
        //get all the outfits and put then into the arrayList
        db.open();
        c = db.getAllOutfits();
        if(c.moveToFirst())
        {
            do {
                outfitNames.add(c.getString(1));
            }while(c.moveToNext());
        }
        db.close();

        //use the array list to fill out the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_textview, outfitNames);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

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

    //if an outfit name is clicked, then go to the main page passing the name of the outfit
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedFromList = (String)(lv.getItemAtPosition(position));
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("OutfitName", selectedFromList);
        startActivity(i);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            //if about is clicked then go to the about page
            case R.id.AboutBtn:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            //if main is clicked then go to the main page
            case R.id.homeBtn:
                Intent i2 = new Intent(this, MainActivity.class);
                startActivity(i2);
                break;
            case R.id.OutfitsBtn:
                break;
        }
    }
}
