package com.example.luke.jocelyndressup;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ItemDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int headId;
    int torsoId;
    int legsId;
    int feetId;
    int itemId;
    String name;
    float price;
    String vendorName;
    int senderId;
    String type;
    private DBAdapter db;
    EditText nameText;
    EditText priceText;
    EditText vendorText;
    Spinner typeSpinner;
    ArrayList<String> types = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        db = new DBAdapter(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions( android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.tdlongicon2);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type");
            headId = getIntent().getExtras().getInt("head");
            torsoId = getIntent().getExtras().getInt("torso");
            legsId = getIntent().getExtras().getInt("legs");
            feetId = getIntent().getExtras().getInt("feet");
            itemId = getIntent().getExtras().getInt("id");
        }

        nameText = (EditText)findViewById(R.id.nameText);
        priceText = (EditText)findViewById(R.id.priceText);
        vendorText = (EditText)findViewById(R.id.vendText);
        typeSpinner = (Spinner)findViewById(R.id.typeSpinner);
        typeSpinner.setOnItemSelectedListener(this);
        types.add("head");
        types.add("torso");
        types.add("legs");
        types.add("feet");

        typeSpinner.setSelection(types.indexOf(type));

        getItem();
    }

    public void getItem(){
        db.open();
        Cursor c;
        //get all the head id's
        c = db.getItem(itemId);
        if(c.moveToFirst())
        {
            name = c.getString(1);
            price = c.getFloat(2);
            vendorName = c.getString(3);
            senderId = c.getInt(4);
            type = c.getString(5);
        }

        nameText.setText(name);
        priceText.setText(""+price);
        vendorText.setText(vendorName);

        ImageView iv = (ImageView) findViewById(R.id.detailImage);
        String fileName = "a" + itemId;
        int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
        iv.setImageResource(resID);

        db.close();
    }

    public void updateClicked(View view) {
        db.open();
        db.updateItem(itemId,nameText.getText().toString(),Float.parseFloat(priceText.getText().toString()),vendorText.getText().toString(),senderId,type);
        db.close();
        Intent i = new Intent(this, ItemListActivity.class);
        i.putExtra("type", type);
        i.putExtra("head", headId);
        i.putExtra("torso", torsoId);
        i.putExtra("legs", legsId);
        i.putExtra("feet", feetId);
        startActivity(i);
    }

    public void deleteClicked(View view) {
        db.open();
        db.deleteItem(itemId);
        db.close();
        Intent i = new Intent(this, ItemListActivity.class);
        i.putExtra("type", type);
        i.putExtra("head", headId);
        i.putExtra("torso", torsoId);
        i.putExtra("legs", legsId);
        i.putExtra("feet", feetId);
        startActivity(i);
    }

    public void cancelClicked(View view) {
        Intent i = new Intent(this, ItemListActivity.class);
        i.putExtra("type", type);
        i.putExtra("head", headId);
        i.putExtra("torso", torsoId);
        i.putExtra("legs", legsId);
        i.putExtra("feet", feetId);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = types.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
