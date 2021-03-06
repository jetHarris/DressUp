package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WriteNFCItemActivity extends AppCompatActivity {
    NFCManager nfcManager;
    String itemData = "";
    int iSender = 44;
    int iId;
    String iPrice = "";
    String iName = "";
    String iVendor = "";
    String iType = "";
    int headId;
    int torsoId;
    int legsId;
    int feetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfcitem);

//        if (getIntent().getExtras() != null) {
//            iId = getIntent().getExtras().getInt("itemId");
//            iPrice = getIntent().getExtras().getString("price");
//            iName = getIntent().getExtras().getString("name");
//            iVendor = getIntent().getExtras().getString("vendor");
//            //iSender = getIntent().getExtras().getInt("sender");
//            iType = getIntent().getExtras().getString("type");
//
//            headId = getIntent().getExtras().getInt("head");
//            torsoId = getIntent().getExtras().getInt("torso");
//            legsId = getIntent().getExtras().getInt("legs");
//            feetId = getIntent().getExtras().getInt("feet");
//        }

        //Hard coded for testing
        iId = 0;
        iName = "grey_crop_top";
        iPrice = "20.50";
        iVendor = "Gap";
        iSender = 44;
        iType = "torso";

        itemData += "{";
        itemData += "\"item\": {";
        itemData += "\"id\":" + iId + ",";
        itemData += "\"name\":\"" + iName + "\",";
        itemData += "\"price\":\"" + iPrice + "\",";
        itemData += "\"vendor\":\"" + iVendor + "\",";
        itemData += "\"sender\":" + iSender + ",";
        itemData += "\"type\":\"" + iType + "\"";
        itemData += "}";
        itemData += "}";

        nfcManager = new NFCManager(this);
        nfcManager.onActivityCreate();

        nfcManager.setOnTagWriteListener(new NFCManager.TagWriteListener() {
            @Override
            public void onTagWritten() {
                Toast.makeText(WriteNFCItemActivity.this, "Item Sent: " + nfcManager.getWrittenData(), Toast.LENGTH_LONG).show();
            }
        });
        nfcManager.setOnTagWriteErrorListener(new NFCManager.TagWriteErrorListener() {
            @Override
            public void onTagWriteError(NFCWriteException exception) {
                Toast.makeText(WriteNFCItemActivity.this, exception.getType().toString(), Toast.LENGTH_LONG).show();
            }
        });

        sharingAnimation(R.anim.rotatesharing);
    }

    private void sharingAnimation(int animationResourceID)
    {
        ImageView reuseImageView = (ImageView)findViewById(R.id.ivIcon);
        reuseImageView.setImageResource(R.drawable.icon);
        reuseImageView.setVisibility(View.VISIBLE);
        Animation an = AnimationUtils.loadAnimation(this, animationResourceID);
        reuseImageView.startAnimation(an);
    }

    // fires on NFC card detected
    @Override
    public void onNewIntent(Intent intent){
        nfcManager.setWrittenData(itemData);
        nfcManager.writeDataToTag(intent,itemData);
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

    public void onClick(View view) {
        Intent i = new Intent(this, MainActivity.class);
        // Details to persist
//        i.putExtra("id", iId);
//        i.putExtra("type", iType);
//        i.putExtra("head", headId);
//        i.putExtra("torso", torsoId);
//        i.putExtra("legs", legsId);
//        i.putExtra("feet", feetId);
        startActivity(i);
    }
} // WriteNFCItemActivity
