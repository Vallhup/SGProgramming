package com.example.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class EnemyGenerator implements IGameObject {
    private static final float GEN_INTERVAL = 1.0f;
    private static final float MIN_INTERVAL = 0.1f;
    private static final float WAVE_INTERVAL = 30.0f;
    private final Random random = new Random();

    private float time;
    private float interval = GEN_INTERVAL;
    private float waveTime;
    private float waveInteval = WAVE_INTERVAL;
    private float speed = 1.0f;
    private int wave = 0;
    private boolean normalPhase = true;

    //public EnemyGenerator(PracticeScene practiceScene) { this.scene = practiceScene; }

    @Override
    public void update() {
        waveTime += GameView.frameTime;
        if (normalPhase) {
            time += GameView.frameTime;
            if (time > interval) {
                generate(false);
                time -= interval;
                interval *= 0.995f;
                if (interval < MIN_INTERVAL) {
                    interval = MIN_INTERVAL; // 하지만 이 이상 빨라지진 않게 한다
                }
            }
            if (waveTime > waveInteval) {
                generate(true);
                waveTime = 0;
                normalPhase = false;
            }
        }

        else { // boss phase
            if (waveTime > waveInteval || PracticeScene.top().objectsAt(PracticeScene.Layer.enemy).isEmpty()) {
                waveTime = 0;
                normalPhase = true;
            }
        }
    }

    private void generate(boolean boss) {
        wave++;

        for(int i = 0; i < 5; ++i) {
            int level = (wave + 8) / 10 - random.nextInt(3);
            if(level < 0) level = 0;
            if(level > Enemy.MAX_LEVEL) level = Enemy.MAX_LEVEL;
            PracticeScene.top().add(Enemy.get(level, i));
        }
    }

    @Override
    public void draw(Canvas canvas){
    }
}
