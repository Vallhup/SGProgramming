package com.example.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.spgpproject.R;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Enemy extends AnimSprite implements IRecyclable, IBoxCollidable, ILayerProvider<PracticeScene.Layer> {
    public enum Type {
        boss, normal;
    }

    // SPEED 값 설정 필요
    private static final float SPEED = 300f;
    private static final float RADIUS = 90f;

    // 시간이 지나면 레벨에 따라 체력이 늘어나도록 구현할 예정
    // 최대 레벨 설정 필요
    public static final int MAX_LEVEL = 10;
    private int level;
    private int life, maxLife;
    protected RectF collisionRect = new RectF();

    // TODO : Gauge 색 설정 필요
    protected  static Gauge gauge;
    //protected static Gauge gauge = new Gauge(0.1f, R.color.enemy_gauge_fg, R.color.enemy_gauge_bg);

    public static Enemy get(int level, int index){
        return Scene.top().getRecyclable(Enemy.class).init(level, index);
    }

    public Enemy() { super(0, 0, 0); }


    private Enemy init(int level, int index){
        // TODO : 이미지 추가 필요
        //this.setImageResourceId(R.mipmap.enemy);

        // TODO : 최초 position 설정
        //setPosition();

        updateCollisionRect();
        this.level = level;
        // TODO : 레벨에 따라 체력이 얼마나 올라갈지 설정
        this.life = this.maxLife = (level + 1) * 10;
        dy = SPEED;

        return this;
    }

    public int getScore() {return (level + 1) * 100; }
    public boolean decreaseLife(int power){
        life -= power;
        return life <= 0;
    }

    @Override
    public void update() {
        super.update();
        if(dstRect.top > Metrics.height){
            Scene.top().remove(this);
        }

        else{
            updateCollisionRect();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float gauge_width = width * 0.7f;
        float gauge_x = x - gauge_width / 2;
        float gauge_y = dstRect.bottom;
        gauge.draw(canvas, gauge_x, gauge_y, gauge_width, (float)life / maxLife);
    }

    private void updateCollisionRect() {
        collisionRect.set(dstRect);
        collisionRect.inset(11f, 11f);
    }

    public RectF getCollisionRect() { return collisionRect; }

    @Override
    public void onRecycle(){
    }

    @Override
    public PracticeScene.Layer getLayer() { return PracticeScene.Layer.enemy; }
}
