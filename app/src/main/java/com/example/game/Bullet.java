package com.example.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spgpproject.R;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Bullet extends Sprite implements IRecyclable {
//    public enum Type {
//        attack, slow;
//    }
//
//    private Rect srcRect = new Rect();
//    private float dx, dy, radius;
//    private Enemy target;
//    private float power;
//
//    private Bullet() {
//        super(R.mipmap.attacktower);
//    }
//
//    public static Bullet get(Tower tower, Enemy enemy) {
//        Bullet bullet = Scene.top().getRecyclable(Bullet.class);
//        if(bullet == null){
//            bullet = new Bullet();
//        }
//        bullet.init(tower, enemy);
//
//        return bullet;
//    }
//
//    private void init(Tower tower, Enemy enemy) {
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        int maxLevel = w / h;
//        int level = tower.level;
//        if (level < 1) level = 1;
//        if (level > maxLevel) level = maxLevel;
//        srcRect.set(h * (level - 1), 0, h * level, h);
//        this.target = target;
//        double radian = tower.angle * Math.PI / 180;
//        double speed = 10.0 + level;
//        dx = (float) (speed * Math.cos(radian));
//        dy = (float) (speed * Math.sin(radian));
//        this.power = tower.power;
//        radius = 0.2f + level * 0.02f;
//        //setPosition(tower.getX(), tower.getY(), radius);
//    }
//
//    @Override
//    public void update() {
//        // TODO : update 코드 추가
//
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
//    }
//
//    @Override
//    public void onRecycle(){
//    }

    private static final String TAG = Bullet.class.getSimpleName();

    public Bullet() {
        super(R.mipmap.bullet, 0, 0, 50f, 50f);
        srcRect = new Rect();
    }

    public static Bullet get(Tower tower, Enemy target) {
        return Scene.top().getRecyclable(Bullet.class).init(tower, target);
    }

    protected float power;
    protected boolean splashes;
    private Bullet init(Tower tower, Enemy target) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int maxLevel = w / h;
        int level = tower.level;
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        srcRect.set(h * (level - 1), 0, h * level, h);
        double radian = Math.toRadians(tower.angle);
        double speed = (level + 10) * 100; // 1100 ~ 2000
        dx = (float) (speed * Math.cos(radian));
        dy = (float) (speed * Math.sin(radian));
        this.power = (float) (10 * Math.pow(1.2, level - 1));
        this.splashes = level >= 4;
        // 10.0, 12.0, 14.4, 17.28, 20.736, 24.8832, 29.85984, 35.83181, 42.99817, 51.5978
        radius = 20f + level * 2f;
        setPosition(tower.getX(), tower.getY(), radius);

        return this;
    }

    @Override
    public void update() {
        super.update();
        MainScene scene = (MainScene) Scene.top();
        if (x < -radius || x > Metrics.width + radius ||
                y < -radius || y > Metrics.height + radius) {
            //Log.d("CannonFire", "Remove(" + x + "," + y + ") " + this);
            scene.remove(MainScene.Layer.bullet, this);
            return;
        }

        ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
        for (int index = enemies.size() - 1; index >= 0; index--) {
            Enemy enemy = (Enemy) enemies.get(index);
            boolean collides = CollisionHelper.collidesRadius(this, enemy);
            if (collides) {
                scene.remove(MainScene.Layer.bullet, this);
                hit(enemy, power, scene);
                break;
            }
        }
    }

    private void hit(Enemy enemy, float damage, MainScene scene) {
        boolean dead = enemy.decreaseLife(damage);
        if (dead) {
            scene.remove(MainScene.Layer.enemy, enemy);
            //scene.score.add(enemy.score());
        }
    }

    @Override
    public void onRecycle() {}
}
