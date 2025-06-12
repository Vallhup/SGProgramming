package com.example.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class EnemyGenerator implements IGameObject {
//    private static final float GEN_INTERVAL = 1.0f;
//    private static final float MIN_INTERVAL = 0.1f;
//    private static final float WAVE_INTERVAL = 30.0f;
//    private final Random random = new Random();
//
//    private float time;
//    private float interval = GEN_INTERVAL;
//    private float waveTime;
//    private float waveInteval = WAVE_INTERVAL;
//    private float speed = 1.0f;
//    private int wave = 0;
//    private boolean normalPhase = true;
//
//    //public EnemyGenerator(PracticeScene practiceScene) { this.scene = practiceScene; }
//
//    @Override
//    public void update() {
//        waveTime += GameView.frameTime;
//        if (normalPhase) {
//            time += GameView.frameTime;
//            if (time > interval) {
//                generate(false);
//                time -= interval;
//                interval *= 0.995f;
//                if (interval < MIN_INTERVAL) {
//                    interval = MIN_INTERVAL; // 하지만 이 이상 빨라지진 않게 한다
//                }
//            }
//            if (waveTime > waveInteval) {
//                generate(true);
//                waveTime = 0;
//                normalPhase = false;
//            }
//        }
//
//        else { // boss phase
//            if (waveTime > waveInteval || PracticeScene.top().objectsAt(PracticeScene.Layer.enemy).isEmpty()) {
//                waveTime = 0;
//                normalPhase = true;
//            }
//        }
//    }
//
//    private void generate(boolean boss) {
//        wave++;
//
//        for(int i = 0; i < 5; ++i) {
//            int level = (wave + 8) / 10 - random.nextInt(3);
//            if(level < 0) level = 0;
//            if(level > Enemy.MAX_LEVEL) level = Enemy.MAX_LEVEL;
//            PracticeScene.top().add(Enemy.get(Enemy.Type.normal, level, 1.0f));
//        }
//    }
//
//    @Override
//    public void draw(Canvas canvas){
//    }

    private static final float INTERVAL_INIT = 2.0f;
    private static final float INTERVAL_MIN = 0.1f;
    private static final float INTERVAL_WAVE = 30.0f;
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
                // 다음 wave 에서 waveInterval 이나 flySpeedRatio 등을 조정해도 좋을듯.
            }
        } else {
            time += GameView.frameTime;
            if (time > interval) {
                spawn();
                time -= interval;
                interval *= 0.995f; // 0.5% 씩 간격을 줄인다
                if (interval < INTERVAL_MIN) {
                    interval = INTERVAL_MIN;
                    // 하지만 이 이상 빨라지진 않게 한다
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
        scene.add(MainScene.Layer.enemy, enemy);
    }
    @Override
    public void draw(Canvas canvas) {
//        Enemy.drawPath(canvas);
    }
}
