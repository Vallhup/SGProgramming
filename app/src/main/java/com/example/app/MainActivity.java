package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.BaseDefenceActivity;
import com.example.game.PathView;
import com.example.spgpproject.R;
import com.example.spgpproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private @NonNull ActivityMainBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
        ui.pathView.setCallback(pointsCallback);
        updatePointsCount(0);
    }
    private PathView.Callback pointsCallback = new PathView.Callback() {
        @Override
        public void onPointsCountChange(int count) {
            updatePointsCount(count);
        }
    };

    public void onBtnClear(View view) {
        ui.pathView.clearPoints();
        updatePointsCount(0);
    }

    void updatePointsCount(int count) {
        String text = getString(R.string.points_count_fmt, count);
        ui.countTextView.setText(text);
    }

    public void onCheckClosed(View view) {
        ui.pathView.closePath(ui.closedCheckbox.isChecked());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent(this, BaseDefenceActivity.class));
        }
        return super.onTouchEvent(event);
    }
}