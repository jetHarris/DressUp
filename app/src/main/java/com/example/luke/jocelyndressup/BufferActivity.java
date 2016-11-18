package com.example.luke.jocelyndressup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        getSupportActionBar().hide(); // Get rid of actionbar manually
    }

    public void mainPageClicked(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
