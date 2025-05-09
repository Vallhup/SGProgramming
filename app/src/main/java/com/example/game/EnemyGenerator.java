package com.example.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class EnemyGenerator implements IGameObject {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private final Random random = new Random();
    public static final float GEN_INTERVAL = 5.0f;
    private final PracticeScene scene;
    private float enemyTime = 0;
    private int wave;

    public EnemyGenerator(PracticeScene practiceScene) { this.scene = practiceScene; }

    @Override
    public void update() {
        enemyTime -= GameView.frameTime;
        if(enemyTime < 0){
            generate();
            enemyTime = GEN_INTERVAL;
        }
    }

    private void generate() {
        wave++;

        for(int i = 0; i < 5; ++i) {
            int level = (wave + 8) / 10 - random.nextInt(3);
            if(level < 0) level = 0;
            if(level > Enemy.MAX_LEVEL) level = Enemy.MAX_LEVEL;
            scene.add(Enemy.get(level, i));
        }
    }

    @Override
    public void draw(Canvas canvas){
    }
}
