package com.example.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.spgpproject.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Tower extends Sprite {
//    protected int level;
//    protected float x, y;
//
//    private boolean isAttacked;
//    private float attackedTime = -1.0f;
//
//    public enum Type {
//        attack, slow;
//    }
//    private static final String TAG = Tower.class.getSimpleName();
//
//    protected float power, range;
//    protected float angle = -90;
//    private static final float FIRE_INTERVAL = 0.25f;
//    private float fireCoolTime = FIRE_INTERVAL;
//    private static final float BULLET_OFFSET = 80f;
//    private final Bitmap barrelBitmap;
//    private final RectF barrelRect = new RectF();
//
//    public Tower(int level, float x, float y) {
//        super(0);
//        // TODO : barrel 이미지 필요
//        barrelBitmap = BitmapPool.get(R.mipmap.attacktower);
//        setPosition(x, y, 200, 200);
//        setLevel(level);
//        isAttacked = false;
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        if(isAttacked) {
//            attackedTime += GameView.frameTime;
//            if(attackedTime >= 5.0f){
//                isAttacked = false;
//                attackedTime = -1.0f;
//            }
//        }
//
//        else{
//            attack();
//        }
//    }
//
//    private static Paint rangePaint;
//    public void drawRange(Canvas canvas){
//        if(rangePaint == null){
//            rangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            rangePaint.setStyle(Paint.Style.STROKE);
//            rangePaint.setStrokeWidth(0.1f);
//            rangePaint.setPathEffect(new DashPathEffect(new float[]{0.1f, 0.2f}, 0));
//            rangePaint.setColor(0x7F7F0000);
//        }
//        canvas.drawCircle(x, y, range, rangePaint);
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//        canvas.save();
//        canvas.rotate(angle, x, y);
//        canvas.drawBitmap(barrelBitmap, null, barrelRect, null);
//        canvas.restore();
//    }
//
//    private void attack() {
//        Enemy target = setTarget();
//        if(target == null) {
//            return;
//        }
//
//        fireCoolTime -= GameView.frameTime;
//        if(fireCoolTime > 0){
//            return;
//        }
//        fireCoolTime = FIRE_INTERVAL;
//
//        PracticeScene scene = (PracticeScene) Scene.top();
//        if(scene == null) {
//            return;
//        }
//
//        Bullet bullet = Bullet.get(this, target);
//        scene.add(PracticeScene.Layer.enemy, bullet);
//    }
//
//    private Enemy setTarget() {
//        float closestDistSq = range * range;
//        Enemy nearest = null;
//
//        ArrayList<IGameObject> enemies = PracticeScene.top().objectsAt(PracticeScene.Layer.enemy);
//        for (IGameObject gameObject : enemies) {
//            if (!(gameObject instanceof Enemy)) continue;
//
//            Enemy enemy = (Enemy) gameObject;
//            float dx = x - enemy.getX();
//            float dy = y - enemy.getY();
//            float distSq = dx * dx + dy * dy;
//
//            if (distSq <= closestDistSq) {
//                closestDistSq = distSq;
//                nearest = enemy;
//            }
//        }
//
//        return nearest;
//    }
//
//    public boolean onTouch(MotionEvent event) {
//        // TODO : Touch Down 시 강화 UI 추가
//        switch(event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return true;
//        }
//        return false;
//    }
//
//    public void upgrade() {
//        setLevel(level + 1);
//    }
//
//    public void setAttacked(boolean value){
//        if(value && !isAttacked){
//            isAttacked = true;
//            attackedTime = 0.0f;
//        }
//    }
//
//    private void setLevel(int level) {
//        this.level = level;
//        this.power = (float) (10 * Math.pow(1.2, level - 1));
//        this.range = 2 + level * 2;
//        float barrelSize = 0.5f + level * 0.1f;
//        barrelRect.set(dstRect);
//        barrelRect.inset(-barrelSize, -barrelSize);
//        // TODO : 레벨에 따른 이미지 필요
//        // bitmap = BitmapPool.get(BITMAP_IDS[level - 1]);
//    }


    protected int level;
    protected float range;
    protected float interval;
    protected final RectF barrelRect = new RectF();
    protected float angle = -90;
    protected float time;
    private static final int[] BITMAP_IDS = {
            R.mipmap.attacktower, R.mipmap.slowtower
    };
    public Tower(int level, float x, float y) {
        super(0);
        setPosition(x, y, 200, 200);
        setLevel(level);
    }
    private static final int[] COSTS = {
        50, 20
    };
    public static int getInstallationCost(int type) {
        return COSTS[type - 1];
    }
    public static int getUpgradeCost(int type) {
        return Math.round((COSTS[type] - COSTS[type - 1]) * 1.1f);
    }
    public int getUpgradeCost() {
        return getUpgradeCost(level);
    }
    public static int getSellPrice(int type) {
        return COSTS[type - 1] / 2;
    }
    public int getSellPrice() {
        return getSellPrice(level);
    }
    private void setLevel(int level) {
        bitmap = BitmapPool.get(BITMAP_IDS[level - 1]);
        this.level = level;
        this.range = 200 + (level * 200);
        this.interval = 5.5f - level / 2.0f;
        barrelRect.set(dstRect);
        float barrelSize = 50f + level * 10f;
        barrelRect.inset(-barrelSize, -barrelSize);
    }

    @Override
    public void update() {
        super.update();
        Enemy fly = findNearestEnemy();
        if (fly != null) {
            angle = (float) Math.toDegrees(Math.atan2(fly.getY() - y, fly.getX() - x));
        }
        time += GameView.frameTime;
        if (time > interval && fly != null) {
            Bullet bullet = Bullet.get(this, fly);
            Scene.top().add(MainScene.Layer.bullet, bullet);
            time = 0;
        }
    }

    public Enemy findNearestEnemy() {
        float nearest_dist_sq = range * range;
        Enemy nearest = null;
        MainScene scene = (MainScene) Scene.top();
        ArrayList<IGameObject> flies = scene.objectsAt(MainScene.Layer.enemy);
        for (IGameObject gameObject: flies) {
            if (!(gameObject instanceof Enemy)) continue;
            Enemy fly = (Enemy) gameObject;
            float fx = fly.getX();
            float fy = fly.getY();

            // 현재 탐색 중인 fly까지의 거리 제곱 계산
            float dx = x - fx;
            float dx_sq = dx * dx;
            if (dx_sq > nearest_dist_sq) continue; // x축 거리만으로 범위 초과
            float dy = y - fy;
            float dy_sq = dy * dy;
            if (dy_sq > nearest_dist_sq) continue; // y축 거리만으로 범위 초과
            float dist_sq = dx_sq + dy_sq;
            if (nearest_dist_sq > dist_sq) {
                nearest_dist_sq = dist_sq;
                nearest = fly;
            }
        }
        return nearest;
    }

    private static Paint rangePaint;
    public void drawRange(Canvas canvas) {
        if (rangePaint == null) {
            rangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            rangePaint.setStyle(Paint.Style.STROKE);
            rangePaint.setStrokeWidth(10f);
            rangePaint.setPathEffect(new DashPathEffect(new float[]{10f, 20f}, 0));
            rangePaint.setColor(0x7F7F0000);
        }
        canvas.drawCircle(x, y, range, rangePaint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.rotate(angle, x, y);
        canvas.restore();
    }

    public boolean containsPoint(float x, float y) {
        return dstRect.contains(x, y);
    }

    public boolean intersectsIfInstalledAt(float x, float y) {
        float dx = Math.abs(x - this.x), dy = Math.abs(y - this.y);
        return dx <= radius && dy <= radius;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Cannon<%d>(%d,%d)@%d",
                level, (int)x/100, (int)y/100, System.identityHashCode(this));
    }

    public boolean upgrade() {
        if (level == BITMAP_IDS.length) {
            uninstall();
            return false;
        }
        setLevel(level + 1);
        return true;
    }

    protected void uninstall() {
        Scene.top().remove(MainScene.Layer.tower, this);
    }
}
