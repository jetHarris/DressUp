package com.example.luke.jocelyndressup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadNFCItemActivity extends AppCompatActivity {
    NFCManager nfcManager;
    Context thisContext;
    private DBAdapter db;
    String itemData;
    int iId;
    String iName;
    String iPrice;
    String iVendor;
    int iSender;
    String iType;

    Bitmap capture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_nfcitem);
        db = new DBAdapter(this);
        nfcManager = new NFCManager(this);
        nfcManager.onActivityCreate();

        nfcManager.setOnTagReadListener(new NFCManager.TagReadListener() {
            @Override
            public void onTagRead(String itemRead) {
                Toast.makeText(ReadNFCItemActivity.this, "Item Received!", Toast.LENGTH_LONG).show();
                //Toast.makeText(ReadNFCItemActivity.this, "Item Received:" + itemRead, Toast.LENGTH_LONG).show();
                itemData = itemRead;
                try {
                    JSONObject jsonObj = new JSONObject(itemRead);
                    JSONObject itemObject = jsonObj.getJSONObject("item");
                    iId = itemObject.getInt("id");
                    iName = itemObject.getString("name");
                    iPrice = itemObject.getString("price");
                    iVendor = itemObject.getString("vendor");
                    iSender = itemObject.getInt("sender");
                    iType = itemObject.getString("type");
                } catch (JSONException je) {
                    System.out.println("JSON Error - " + je.getMessage());
                } catch (Exception e) {
                    System.out.println("Error - " + e.getMessage());
                }
                // Display Read Item?
                updateItem();
                toMain();
            }
        });

        sharingAnimation(R.anim.rotatesharing);
    }

    private void sharingAnimation(int animationResourceID)
    {
        ImageView reuseImageView = (ImageView)findViewById(R.id.ivReadIcon);
        reuseImageView.setImageResource(R.drawable.icon);
        reuseImageView.setVisibility(View.VISIBLE);
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        reuseImageView.startAnimation(an);
    }

    // fires on NFC card detected
    @Override
    public void onNewIntent(Intent intent) {
        nfcManager.readTag(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcManager.onActivityResume();
    }

    @Override
    protected void onPause() {
        nfcManager.onActivityPause();
        super.onPause();
    }

    private void readItemDetails() {
        Intent i = new Intent(this, ItemDetailActivity.class);
        i.putExtra("id", iId);
        i.putExtra("name", iName);
        i.putExtra("price", iPrice);
        i.putExtra("type", iType);
        i.putExtra("vendor", iVendor);
        startActivity(i);
    }

    private void updateItem() {
        if (!iName.equals("") && !iPrice.equals("")) {
            db.open();
            Float price = Float.parseFloat(iPrice);
            Float hundred = 100.0F;
            db.insertItem(iName, Math.round(price * hundred) / hundred, iVendor, iSender, iType);
            db.close();
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(namify(iName) + ".bmp", Context.MODE_PRIVATE);
                // Use the compress method on the BitMap object to write image to the OutputStream
                capture.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, DisplayReadItemActivity.class);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            capture.compress(Bitmap.CompressFormat.JPEG, 50, bs);
            i.putExtra("image", bs.toByteArray());
            i.putExtra("name", iName);
            i.putExtra("price", iPrice);
            startActivity(i);
        } else {
            Toast.makeText(this, "Please fill out at least name and price", Toast.LENGTH_SHORT).show();
        }

        // update
//        if (!iName.equals("") && !iPrice.equals("")) {
//            db.open();
//            Float price = Float.parseFloat(iPrice);
//            Float hundred = 100.0F;
//            db.updateItem(iId, namify(iName), Math.round(price * hundred) / hundred, iVendor, iSender, iType);
//            db.close();
//            Toast.makeText(this, "Item Scanned to DB", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Invalid Item Tag", Toast.LENGTH_SHORT).show();
//        }
    }

    protected String namify(String name) {
        String temp = name.replace(' ', '_');
        String results = temp.toLowerCase();
        return results;
    }

    private void toMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onClick(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
} // ReadNFCItemActivity
