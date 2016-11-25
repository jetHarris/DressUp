package com.example.luke.jocelyndressup;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private DBAdapter db;
    ListView list;
    String[] itemname;

    Integer[] imgid;
    Float[] prices;
    int headID;
    int torsoID;
    int legsID;
    int feetID;
    String type;
    ArrayList<Integer> itemIDs;
    Context context;
    CustomListAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        db = new DBAdapter(this);
        context = this.getApplicationContext();


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
        //get intent which would be from the outfits page
        //then use the outfit name to set the images appropriately
        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type");
            headID = getIntent().getExtras().getInt("head");
            torsoID = getIntent().getExtras().getInt("torso");
            legsID = getIntent().getExtras().getInt("legs");
            feetID = getIntent().getExtras().getInt("feet");
        }

        setArrays();

        adapter = new CustomListAdapter(this, itemname, imgid, prices,context);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                sendToMain(position);

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                sendToDetails(pos);
                return true;
            }
        });
    }



    public void sendToDetails(int position){
        Intent i = new Intent(this, ItemDetailActivity.class);
        i.putExtra("id", itemIDs.get(position));
        i.putExtra("type", type);
        switch(type) {
            //if the next head button is clicked then switch the image of the head to the next one
            case "head":
                i.putExtra("head", itemIDs.get(position));
                i.putExtra("torso", torsoID);
                i.putExtra("legs", legsID);
                i.putExtra("feet", feetID);
                break;
            case "torso":
                i.putExtra("head", headID);
                i.putExtra("torso", itemIDs.get(position));
                i.putExtra("legs", legsID);
                i.putExtra("feet", feetID);
                break;
            case "legs":
                i.putExtra("head", headID);
                i.putExtra("torso", torsoID);
                i.putExtra("legs", itemIDs.get(position));
                i.putExtra("feet", feetID);
                break;
            case "feet":
                i.putExtra("head", headID);
                i.putExtra("torso", torsoID);
                i.putExtra("legs", legsID);
                i.putExtra("feet", itemIDs.get(position));
                break;
        }
        startActivity(i);
    }

    public void sendToMain(int position) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("type", type);
        switch(type) {
            //if the next head button is clicked then switch the image of the head to the next one
            case "head":
                i.putExtra("head", itemIDs.get(position));
                i.putExtra("torso", torsoID);
                i.putExtra("legs", legsID);
                i.putExtra("feet", feetID);
                break;
            case "torso":
                i.putExtra("head", headID);
                i.putExtra("torso", itemIDs.get(position));
                i.putExtra("legs", legsID);
                i.putExtra("feet", feetID);
                break;
            case "legs":
                i.putExtra("head", headID);
                i.putExtra("torso", torsoID);
                i.putExtra("legs", itemIDs.get(position));
                i.putExtra("feet", feetID);
                break;
            case "feet":
                i.putExtra("head", headID);
                i.putExtra("torso", torsoID);
                i.putExtra("legs", legsID);
                i.putExtra("feet", itemIDs.get(position));
                break;
        }
        startActivity(i);
    }

    public void setArrays() {
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Float> pricesTemp = new ArrayList<Float>();
        itemIDs = new ArrayList<Integer>();
        db.open();
        Cursor c;
        //get all the head id's
        c = db.getItemsByType(type);
        if (c.moveToFirst()) {
            do {
                itemIDs.add(c.getInt(0));
                names.add(c.getString(1));
                pricesTemp.add(c.getFloat(2));
                //DisplayContact(c);
            } while (c.moveToNext());
        }

        db.close();

        itemname = names.toArray(new String[names.size()]);
        //imgid = (Integer[])imgIDs.toArray();

        ArrayList<Integer> resIds = new ArrayList<Integer>();

        for (int i = 0; i < itemIDs.size(); ++i) {
            String fileName1 = "a" + itemIDs.get(i);
            int resID1 = getResources().getIdentifier(fileName1, "drawable", getPackageName());
            resIds.add(resID1);
        }

        imgid = resIds.toArray(new Integer[resIds.size()]);
        prices = pricesTemp.toArray(new Float[pricesTemp.size()]);


    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.AboutBtn:
                Intent i1 = new Intent(this, AboutActivity.class);
                startActivity(i1);
                break;

            //if the outfits menu item was clicked then go to the outfits page
            case R.id.OutfitsBtn:
                Intent i2 = new Intent(this, OutfitsActivity.class);
                startActivity(i2);
                break;

            case R.id.homeBtn:
                Intent i3 = new Intent(this, MainActivity.class);
                startActivity(i3);
                break;

            case R.id.addItemBtn:
                Intent i6 = new Intent(this, CameraActivity.class);
                startActivity(i6);
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for(int i = 0; i < adapter.tasks.size();++i){
            if(adapter.tasks.get(i) != null){
                adapter.tasks.get(i).cancel(true);
            }
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//        adapter.resume();
//    }
}
