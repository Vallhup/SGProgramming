package com.example.app;

import android.os.Bundle;

import com.example.spgpproject.BuildConfig;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class BaseDefenceActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        super.onCreate(savedInstanceState);
        new PracticeScene().push();
    }
}
