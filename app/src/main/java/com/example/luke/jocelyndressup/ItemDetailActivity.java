package com.example.luke.jocelyndressup;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
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
    ImageView display;
    private Context context;
    boolean addingItem = false;
    Bitmap capture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        db = new DBAdapter(this);
        context = this.getApplicationContext();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
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

        display = (ImageView) findViewById(R.id.detailImage);

        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra("image")) {
                capture = BitmapFactory.decodeByteArray(
                        getIntent().getByteArrayExtra("image"), 0, getIntent().getByteArrayExtra("image").length);
                Button updateBtn = (Button) findViewById(R.id.updateBtn);
                updateBtn.setText("Save Item");
                Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
                deleteBtn.setVisibility(View.INVISIBLE);
                display.setImageBitmap(capture);
                addingItem = true;
            } else {
                type = getIntent().getExtras().getString("type");
                headId = getIntent().getExtras().getInt("head");
                torsoId = getIntent().getExtras().getInt("torso");
                legsId = getIntent().getExtras().getInt("legs");
                feetId = getIntent().getExtras().getInt("feet");
                itemId = getIntent().getExtras().getInt("id");
            }
        }

        nameText = (EditText) findViewById(R.id.nameText);
        priceText = (EditText) findViewById(R.id.totalText);
        vendorText = (EditText) findViewById(R.id.vendText);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        typeSpinner.setOnItemSelectedListener(this);
        types.add("head");
        types.add("torso");
        types.add("legs");
        types.add("feet");

        typeSpinner.setSelection(types.indexOf(type));

        getItem();
    }

    public void setImage(ImageView view, String fileName) {
        try {
            ImageLoaderPacket packet = new ImageLoaderPacket(fileName, view, context);
            ImageLoader il = new ImageLoader();
            il.execute(packet);
//            FileInputStream fin = openFileInput(fileName+".bmp");
//            Bitmap b = BitmapFactory.decodeStream(fin);
//            view.setImageBitmap(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getItem() {
        db.open();
        Cursor c;
        //get all the head id's
        c = db.getItem(itemId);
        if (c.moveToFirst()) {
            name = c.getString(1);
            price = c.getFloat(2);
            vendorName = c.getString(3);
            senderId = c.getInt(4);
            type = c.getString(5);
        }

        nameText.setText(name);
        priceText.setText("" + price);
        vendorText.setText(vendorName);

        setImage(display, name);
//        ImageView iv = (ImageView) findViewById(R.id.detailImage);
//        String fileName = "a" + itemId;
//        int resID = getResources().getIdentifier(fileName, "drawable", getPackageName());
//        iv.setImageResource(resID);

        db.close();
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.AboutBtn:
                Intent i1 = new Intent(this, AboutActivity.class);
                startActivity(i1);
                break;

            case R.id.updateBtn:
                if (addingItem) {
                    db.open();
                    db.insertItem(nameText.getText().toString(), Float.parseFloat(priceText.getText().toString()), vendorText.getText().toString(), senderId, type);
                    db.close();
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(nameText.getText().toString()+".bmp", Context.MODE_PRIVATE);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        capture.compress(Bitmap.CompressFormat.PNG, 10, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, ItemListActivity.class);
                    i.putExtra("type", type);
                    i.putExtra("head", headId);
                    i.putExtra("torso", torsoId);
                    i.putExtra("legs", legsId);
                    i.putExtra("feet", feetId);
                    startActivity(i);

                } else {
                    db.open();
                    db.updateItem(itemId, nameText.getText().toString(), Float.parseFloat(priceText.getText().toString()), vendorText.getText().toString(), senderId, type);
                    db.close();
                    Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, ItemListActivity.class);
                    i.putExtra("type", type);
                    i.putExtra("head", headId);
                    i.putExtra("torso", torsoId);
                    i.putExtra("legs", legsId);
                    i.putExtra("feet", feetId);
                    startActivity(i);
                }
                break;

            case R.id.deleteBtn:
                db.open();
                db.deleteItem(itemId);
                db.close();
                Intent i2 = new Intent(this, ItemListActivity.class);
                i2.putExtra("type", type);
                i2.putExtra("head", headId);
                i2.putExtra("torso", torsoId);
                i2.putExtra("legs", legsId);
                i2.putExtra("feet", feetId);
                startActivity(i2);
                break;

            //if the outfits menu item was clicked then go to the outfits page
            case R.id.OutfitsBtn:
                Intent i3 = new Intent(this, OutfitsActivity.class);
                startActivity(i3);
                break;

            case R.id.homeBtn:
                Intent i4 = new Intent(this, MainActivity.class);
                startActivity(i4);
                break;

            case R.id.cancelBtn:
                if(addingItem){
                    Intent i6 = new Intent(this, CameraActivity.class);
                    startActivity(i6);
                }
                else {
                    Intent i5 = new Intent(this, ItemListActivity.class);
                    i5.putExtra("type", type);
                    i5.putExtra("head", headId);
                    i5.putExtra("torso", torsoId);
                    i5.putExtra("legs", legsId);
                    i5.putExtra("feet", feetId);
                    startActivity(i5);
                }
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        type = types.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        BitmapDrawable drawable = (BitmapDrawable) display.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        display.setImageBitmap(null);

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        System.gc();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setImage(display, name);
    }
}
