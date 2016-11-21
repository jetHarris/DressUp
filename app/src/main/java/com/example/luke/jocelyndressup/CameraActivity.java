package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class CameraActivity extends AppCompatActivity {
    private ImageView itemImg;
    static final int REQUEST_IMAGE_CAPTURE = 1;
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
                dispatchTakePictureIntent();
                break;
            case R.id.saveImageBtn:
                Intent i = new Intent(this, ItemDetailActivity.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                capture.compress(Bitmap.CompressFormat.PNG, 50, bs);
                i.putExtra("image", bs.toByteArray());
                startActivity(i);
                break;
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

