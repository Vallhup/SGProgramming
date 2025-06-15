package com.example.game.MainScene;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.example.spgpproject.R;

import java.util.ArrayList;
import java.util.Locale;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class Tower extends Sprite {
    protected int level;
    protected float power, range;
    protected float interval;
    protected float angle = -90;
    protected float time;
    private boolean isAttacked;
    private float attackedTime = -1.0f;
    private static final int[] BITMAP_IDS = {
            R.mipmap.attacktower, R.mipmap.slowtower
    };

    public int getLevel() {
        return level;
    }

    public enum Type {
        attack, slow;
    }

    protected Type type;

    public Tower.Type getType()
    {
        return type;
    }
    public Tower(int type, float x, float y) {
        super(0);
        setPosition(x, y, 200, 200);
        setLevel(1);
        if(type == 1){
            this.type = Type.attack;
            interval = 0.5f;
        } else {
            this.type = Type.slow;
            interval = 1.0f;
        }
        bitmap = BitmapPool.get(BITMAP_IDS[type - 1]);
    }
    private static final int[] COSTS = {
        25, 15
    };
    public static int getInstallationCost(int type) {
        return COSTS[type - 1];
    }
    public static int getUpgradeCost(int type) {
        if(type == 1){
            return 20;
        } else {
            return 10;
        }
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
        if(level <= 2 && level > 0){
            this.level = level;
            this.range = 200 + (level * 200);
            this.power = level * 15;
        }
    }

    @Override
    public void update() {
        super.update();
        Enemy fly = findNearestEnemy();
        if (fly != null) {
            angle = (float) Math.toDegrees(Math.atan2(fly.getY() - y, fly.getX() - x));
        }

        if(isAttacked) {
            attackedTime += GameView.frameTime;
            if(attackedTime >= 2.0f){
                isAttacked = false;
                attackedTime = -1.0f;
            }
        } else {
            time += GameView.frameTime;
            if (time > interval && fly != null) {
                Bullet bullet = Bullet.get(this, fly);
                Scene.top().add(MainScene.Layer.bullet, bullet);
                time = 0;
            }
        }
    }

    public Enemy findNearestEnemy() {
        float nearest_dist_sq = range * range;
        Enemy nearest = null;
        MainScene scene = (MainScene) Scene.top();
        ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
        for (IGameObject gameObject: enemies) {
            if (!(gameObject instanceof Enemy)) continue;
            Enemy fly = (Enemy) gameObject;
            float fx = fly.getX();
            float fy = fly.getY();
            float dx = x - fx;
            float dx_sq = dx * dx;
            if (dx_sq > nearest_dist_sq) continue;
            float dy = y - fy;
            float dy_sq = dy * dy;
            if (dy_sq > nearest_dist_sq) continue;
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

    public void setAttacked(boolean value){
        this.isAttacked = value;
        attackedTime = 0.0f;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Cannon<%d>(%d,%d)@%d",
                level, (int)x/100, (int)y/100, System.identityHashCode(this));
    }

    public void upgrade() {
        setLevel(level + 1);
    }

    protected void uninstall() {
        Scene.top().remove(MainScene.Layer.tower, this);
    }
}
