package com.example.app;

import android.os.Bundle;

import com.example.game.GameOverScene.GameOverScene;
import com.example.game.MainScene.MainScene;
import com.example.spgpproject.BuildConfig;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainGameActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView.drawsDebugStuffs = !BuildConfig.DEBUG;
        Metrics.setGameSize(3200, 1800);
        new MainScene().push();
    }
}
