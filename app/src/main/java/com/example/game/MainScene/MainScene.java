package com.example.game.MainScene;

import android.view.MotionEvent;

import com.example.game.GameOverScene.GameOverScene;
import com.example.spgpproject.R;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Score;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {
    private static final String TAG = MainScene.class.getSimpleName();
    protected final BackGround bg;
    protected final Selector selector;
    protected final Score money, life, score;

    public void decreaseLife(int damage) {
        life.add(-damage);

        if(life.getScore() <= 0){
            new GameOverScene().change();
        }
    }

    public enum Layer {
        bg, enemy, tower, bullet, score, selection, controller;
    }

    public MainScene() {
        initLayers(Layer.values().length);

        bg = new BackGround();
        add(Layer.bg, bg);
        add(Layer.selection, selector = new Selector(this));
        add(Layer.controller, new EnemyGenerator(this));

        money = new Score(R.mipmap.gold_number, Metrics.width - 50, 50, 100);
        money.setScore(70);
        add(Layer.score, money);

        life = new Score(R.mipmap.gold_number, Metrics.width - 350, 50, 100);
        life.setScore(7);
        add(Layer.score, life);

        score = new Score(R.mipmap.gold_number, Metrics.width - 550, 50, 100);
        score.setScore(0);
        add(Layer.score, score);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        return selector.onTouch(action, pts[0], pts[1]);
    }
}