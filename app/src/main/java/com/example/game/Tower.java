package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.spgpproject.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Tower extends Sprite {
    protected int level;
    protected float x, y;

    private boolean isAttacked;
    private float attackedTime = -1.0f;

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

    public Tower(int level, float x, float y) {
        super(0);
        // TODO : barrel 이미지 필요
        //barrelBitmap = BitmapPool.get(R.mipmap.attacktower);
        this.x = x;
        this.y = y;
        isAttacked = false;
        setLevel(level);
    }

    @Override
    public void update() {
        super.update();
        if(isAttacked) {
            attackedTime += GameView.frameTime;
            if(attackedTime >= 5.0f){
                isAttacked = false;
                attackedTime = -1.0f;
            }
        }

        else{
            attack();
        }
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
        Enemy target = setTarget();
        if(target == null) {
            return;
        }

        fireCoolTime -= GameView.frameTime;
        if(fireCoolTime > 0){
            return;
        }
        fireCoolTime = FIRE_INTERVAL;

        PracticeScene scene = (PracticeScene) Scene.top();
        if(scene == null) {
            return;
        }

        Bullet bullet = Bullet.get(this, target);
        scene.add(PracticeScene.Layer.enemy, bullet);
    }

    private Enemy setTarget() {
        float closestDistSq = range * range;
        Enemy nearest = null;

        ArrayList<IGameObject> enemies = PracticeScene.top().objectsAt(PracticeScene.Layer.enemy);
        for (IGameObject gameObject : enemies) {
            if (!(gameObject instanceof Enemy)) continue;

            Enemy enemy = (Enemy) gameObject;
            float dx = x - enemy.getX();
            float dy = y - enemy.getY();
            float distSq = dx * dx + dy * dy;

            if (distSq <= closestDistSq) {
                closestDistSq = distSq;
                nearest = enemy;
            }
        }

        return nearest;
    }

    public boolean onTouch(MotionEvent event) {
        // TODO : Touch Down 시 강화 UI 추가
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
        }
        return false;
    }

    public void upgrade() {
        setLevel(level + 1);
    }

    public void setAttacked(boolean value){
        if(value && !isAttacked){
            isAttacked = true;
            attackedTime = 0.0f;
        }
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
