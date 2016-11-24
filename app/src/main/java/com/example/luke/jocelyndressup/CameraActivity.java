package com.example.luke.jocelyndressup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class CameraActivity extends AppCompatActivity {
    private ImageView itemImg;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int MY_PERMISSIONS_REQUEST_CAMERA = 4;
    private Bitmap capture;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Disable Camera features with this boolean if user doesnt have camera and we say it is not required in the manifest
        //boolean hasCamera = hasSystemFeature(PackageManager.FEATURE_CAMERA);
        itemImg = (ImageView)findViewById(R.id.imageView);
        saveBtn = (Button)findViewById(R.id.saveImageBtn);
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.imageButton:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {

                    } else {
                        // request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
                else
                    dispatchTakePictureIntent();
                break;
            case R.id.saveImageBtn:
                Intent i = new Intent(this, ItemDetailActivity.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                capture.compress(Bitmap.CompressFormat.PNG, 10, bs);
                i.putExtra("image", bs.toByteArray());
                startActivity(i);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, Do the task you need to do.
                    dispatchTakePictureIntent();
                } else {
                    // permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Permission Required for this feature!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            itemImg.setImageBitmap(imageBitmap);
            capture = imageBitmap;
            saveBtn.setVisibility(View.VISIBLE);
        }
    }
}

