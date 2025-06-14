package com.example.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class EnemyGenerator implements IGameObject {
    private static final float INTERVAL_INIT = 2.0f;
    private static final float INTERVAL_MIN = 0.1f;
    private static final float INTERVAL_WAVE = 20.0f;
    private final MainScene scene;
    private float time, interval, flySpeedRatio;
    private float waveTime, waveInterval;
    private int wave;
    private boolean bossPhase;

    public EnemyGenerator(MainScene scene) {
        this.scene = scene;
        this.interval = INTERVAL_INIT;
        this.waveInterval = INTERVAL_WAVE;
        this.time = this.waveTime = 0;
        this.flySpeedRatio = 1.0f;
        this.wave = 0;
        this.bossPhase = false;
    }

    @Override
    public void update() {
        waveTime += GameView.frameTime;
        if (bossPhase) {
            if (waveTime > waveInterval || scene.objectsAt(MainScene.Layer.enemy).isEmpty()) {
                waveTime = 0;
                bossPhase = false;
                wave++;
            }
        } else {
            time += GameView.frameTime;
            if (time > interval) {
                spawn();
                time -= interval;
                interval *= 0.99f;
                if (interval < INTERVAL_MIN) {
                    interval = INTERVAL_MIN;
                }
            }
            if (waveTime > waveInterval) {
                bossPhase = true;
                spawn();
                waveTime = 0;
            }
        }
    }

    private void spawn() {
        Enemy enemy = Enemy.get(bossPhase, flySpeedRatio);
        enemy.setType(bossPhase);
        scene.add(MainScene.Layer.enemy, enemy);
    }
    @Override
    public void draw(Canvas canvas) {}
}
