package com.example.game;

import android.view.MotionEvent;

import com.example.spgpproject.R;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Score;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {
    private static final String TAG = MainScene.class.getSimpleName();
    protected final BackGround bg;
    protected final Selector selector;
    protected final Score score;

    public enum Layer {
        bg, enemy, bullet, tower, score, selection, controller;
        public static final int COUNT = values().length;
    }

    public MainScene() {
        Metrics.setGameSize(1600, 900);
        initLayers(Layer.COUNT);

        bg = new BackGround();
        add(Layer.bg, bg);
        add(Layer.selection, selector = new Selector(this));
        add(Layer.controller, new EnemyGenerator(this));

        score = new Score(R.mipmap.gold_number, Metrics.width - 50, 50, 100);
        score.setScore(30);
        add(Layer.score, score);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        return selector.onTouch(action, pts[0], pts[1]);
    }
}