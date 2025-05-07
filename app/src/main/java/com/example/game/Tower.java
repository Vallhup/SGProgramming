package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.spgpproject.R;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Tower extends Sprite {
    protected int level;

    public enum Type {
        attack, slow;
    }
    private static final String TAG = Tower.class.getSimpleName();

    protected float damage, range;
    protected float angle = -90;
    private static final float FIRE_INTERVAL = 0.25f;
    private float fireCoolTime = FIRE_INTERVAL;
    private static final float BULLET_OFFSET = 80f;
    private final Bitmap barrelBitmap = null;
    private final RectF barrelRect = new RectF();

    public Tower(int level, int x, int y) {
        super(0);
        // TODO : barrel 이미지 필요
        //barrelBitmap = BitmapPool.get(R.mipmap.attacktower);
        setLevel(level);
    }

    @Override
    public void update() {
        super.update();

        // TODO : update 코드 추가
    }

    private static Paint rangePaint;
    public void drawRange(Canvas canvas){
        if(rangePaint == null){
            rangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            rangePaint.setStyle(Paint.Style.STROKE);
            rangePaint.setStrokeWidth(0.1f);
            rangePaint.setPathEffect(new DashPathEffect(new float[]{0.1f, 0.2f}, 0));
            rangePaint.setColor(0x7F7F0000);
        }
        canvas.drawCircle(x, y, range, rangePaint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.rotate(angle, x, y);
        canvas.drawBitmap(barrelBitmap, null, barrelRect, null);
        canvas.restore();
    }

    private void attack() {
        // TODO : Attack 코드 추가
    }

    private Enemy setTarget() {
        // TODO : Target 설정 코드 추가
        Enemy temp = new Enemy();
        return temp;
    }

    public boolean onTouch(MotionEvent event) {
        // TODO : Touch Down 시 강화 UI 추가
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                //setTargetX(pts[0]);



                return true;
        }
        return false;
    }

    public void upgrade() {
        setLevel(level + 1);
    }

    private void setLevel(int level) {
        this.level = level;
        this.damage = (float) (10 * Math.pow(1.2, level - 1));
        this.range = 2 + level * 2;
        float barrelSize = 0.5f + level * 0.1f;
        barrelRect.set(dstRect);
        barrelRect.inset(-barrelSize, -barrelSize);
        // TODO : 레벨에 따른 이미지 필요
        // bitmap = BitmapPool.get(BITMAP_IDS[level - 1]);
    }
}
