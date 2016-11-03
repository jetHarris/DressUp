package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        db = new DBAdapter(this);

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

        CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid, prices);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                sendToMain(position);

            }
        });
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
}
