package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spgpproject.BuildConfig;
import com.example.spgpproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (BuildConfig.DEBUG) {
            startGameActivity();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startGameActivity();
        }
        return super.onTouchEvent(event);
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, MainGameActivity.class);
        startActivity(intent);
    }
}