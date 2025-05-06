package com.example.game;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.spgpproject.R;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class SlowTower extends Sprite {
    private static final String TAG = AttackTower.class.getSimpleName();
    private static final float TOWER_WIDTH = 175f;
    private static final int TOWER_SRT_WIDTH = 80;
    private float targetX;

    private static final float FIRE_INTERVAL = 0.25f;
    private float fireCoolTime = FIRE_INTERVAL;
    private static final float BULLET_OFFSET = 80f;

    public SlowTower() {
        // TODO : 이미지 추가 필요
        // super(R.mipmap.slowtower);
        setPosition(Metrics.width / 2, Metrics.height - 200, TOWER_WIDTH, TOWER_WIDTH);
        targetX = x;
    }

    @Override
    public void update() {
        super.update();

        // TODO : update 코드 추가
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // TODO : rendering 코드 추가
    }

    private void attack() {
        // TODO : Attack 코드 추가
    }

    private void setTargetX(float x) {
        //targetX = Math.max(radius, Math.min(x, Metrics.width - radius));
        // TODO : Target 설정 코드 추가
    }

    public boolean onTouch(MotionEvent event) {
        // TODO : Touch Down 시 강화 UI 추가
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetX(pts[0]);
                return true;
        }
        return false;
    }
}
