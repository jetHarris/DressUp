package com.example.luke.jocelyndressup;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadNFCItemActivity extends AppCompatActivity {
    NFCManager nfcManager;
    Context thisContext;
    String itemData;
    String iId;
    String iName;
    String iPrice;
    String iVendor;
    String iType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_nfcitem);
        nfcManager = new NFCManager(this);
        nfcManager.onActivityCreate();

        nfcManager.setOnTagReadListener(new NFCManager.TagReadListener() {
            @Override
            public void onTagRead(String itemRead) {
                Toast.makeText(ReadNFCItemActivity.this, "Item Received:" + itemRead, Toast.LENGTH_LONG).show();
                itemData = itemRead;
                try {
                    JSONObject jsonObj = new JSONObject(itemRead);
                    JSONObject itemObject = jsonObj.getJSONObject("item");
                    iId = itemObject.getString("id");
                    iName = itemObject.getString("name");
                    iPrice = itemObject.getString("price");
                    iVendor = itemObject.getString("vendor");
                    iType = itemObject.getString("type");
                } catch (JSONException je) {
                    System.out.println("JSON Error - " + je.getMessage());
                } catch (Exception e) {
                    System.out.println("Error - " + e.getMessage());
                }
                // Display Read Item?
                readItemDetails();
            }
        });
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

    public void onClick(View view) {
        readItemDetails();
    }
} // ReadNFCItemActivity
