package com.example.game;

import android.graphics.Rect;

import com.example.spgpproject.R;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Bullet extends Sprite implements IRecyclable {
    private static final String TAG = Bullet.class.getSimpleName();

    public enum Type {
        attack, slow;
    }

    private Type type;

    private static final int[] BITMAP_IDS = {
            R.mipmap.attack_bullet, R.mipmap.slow_bullet
    };

    public Bullet() {
        super(R.mipmap.attack_bullet, 0, 0, 50f, 50f);
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
        radius = 20f + level * 2f;
        setPosition(tower.getX(), tower.getY(), radius);
        Tower.Type tType = tower.getType();
        if (tType == Tower.Type.attack) {
            type = Bullet.Type.attack;
            setImageResourceId(R.mipmap.attack_bullet);
        } else {
            type = Bullet.Type.slow;
            setImageResourceId(R.mipmap.slow_bullet);
        }

        return this;
    }

    @Override
    public void update() {
        super.update();
        MainScene scene = (MainScene) Scene.top();
        if (x < -radius || x > Metrics.width + radius ||
                y < -radius || y > Metrics.height + radius) {
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
        if(type == Type.attack) {
            boolean dead = enemy.decreaseLife(damage);
            if (dead) {
                scene.remove(MainScene.Layer.enemy, enemy);
                scene.score.add(enemy.score());
            }
        }

        else {
            enemy.decreaseSpeed(damage);
        }
    }

    @Override
    public void onRecycle() {}
}
