package com.example.game;

import android.graphics.Canvas;
import android.graphics.Rect;

import androidx.recyclerview.widget.RecyclerView;

import com.example.spgpproject.R;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;

public class Bullet extends Sprite implements IRecyclable {
    public enum Type {
        attack, slow;
    }

    private Rect srcRect = new Rect();
    private float dx, dy, radius;
    private Enemy target;
    private float power;

    private Bullet() {
        super(R.mipmap.attacktower);
    }

    public static Bullet get(Tower tower, Enemy enemy) {
        Bullet bullet = Scene.top().getRecyclable(Bullet.class);
        if(bullet == null){
            bullet = new Bullet();
        }
        bullet.init(tower, enemy);

        return bullet;
    }

    private void init(Tower tower, Enemy enemy) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int maxLevel = w / h;
        int level = tower.level;
        if (level < 1) level = 1;
        if (level > maxLevel) level = maxLevel;
        srcRect.set(h * (level - 1), 0, h * level, h);
        this.target = target;
        double radian = tower.angle * Math.PI / 180;
        double speed = 10.0 + level;
        dx = (float) (speed * Math.cos(radian));
        dy = (float) (speed * Math.sin(radian));
        this.power = tower.power;
        radius = 0.2f + level * 0.02f;
        //setPosition(tower.getX(), tower.getY(), radius);
    }

    @Override
    public void update() {
        // TODO : update 코드 추가

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    @Override
    public void onRecycle(){
    }
}
