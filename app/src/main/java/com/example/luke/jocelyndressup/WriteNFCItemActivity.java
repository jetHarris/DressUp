package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WriteNFCItemActivity extends AppCompatActivity {
    NFCManager nfcManager;
    String itemData = "";
    String iId = "";
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

        if (getIntent().getExtras() != null) {
            iId = getIntent().getExtras().getString("itemId");
            iPrice = getIntent().getExtras().getString("price");
            iName = getIntent().getExtras().getString("name");
            iVendor = getIntent().getExtras().getString("vendor");
            iType = getIntent().getExtras().getString("type");

            headId = getIntent().getExtras().getInt("head");
            torsoId = getIntent().getExtras().getInt("torso");
            legsId = getIntent().getExtras().getInt("legs");
            feetId = getIntent().getExtras().getInt("feet");
        }

        itemData += "{";
        itemData += "\"item\": {";
        itemData += "\"id\":\"" + iId + "\",";
        itemData += "\"name\":\"" + iName + "\",";
        itemData += "\"price\":\"" + iPrice + "\",";
        itemData += "\"vendor\":\"" + iVendor + "\",";
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
        Intent i = new Intent(this, ItemDetailActivity.class);
        // Details to persist
        i.putExtra("id", iId);
        i.putExtra("type", iType);
        i.putExtra("head", headId);
        i.putExtra("torso", torsoId);
        i.putExtra("legs", legsId);
        i.putExtra("feet", feetId);
        startActivity(i);
    }
} // WriteNFCItemActivity
