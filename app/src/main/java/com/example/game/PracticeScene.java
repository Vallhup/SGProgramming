package com.example.game;

import com.example.spgpproject.R;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.HorzScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class PracticeScene extends Scene {
    private static final String TAG = PracticeScene.class.getSimpleName();

    public enum Layer {
        bg1, enemy, bullet, tower, ui;
        public static final int COUNT = values().length;
    }

    public PracticeScene() {
        Metrics.setGameSize(1600, 900);
        initLayers(Layer.COUNT);

        // TODO : 배경 이미지 추가
        // add(Layer.bg1, new HorzScrollBackground());

        //Enemy enemy = Enemy.get(Enemy.)
    }
}
