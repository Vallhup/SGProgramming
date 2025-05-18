package com.example.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.spgpproject.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Enemy extends AnimSprite implements IRecyclable, IBoxCollidable, ILayerProvider<PracticeScene.Layer> {
    public float getX() {
        return 0;
    }

    public float getY() {
        return 0;
    }

    public enum Type {
        boss, normal, COUNT, RANDOM;
        static final int[] POSSIBILITIES = { 0, 10, 20, 30, 40 };
        static int POSSIBILITY_SUM;
        static {
            POSSIBILITY_SUM = 0;
            for (int p : POSSIBILITIES) {
                POSSIBILITY_SUM += p;
            }
        }
    }

    // SPEED 값 설정 필요
    private static final float SPEED = 300f;
    private static final float RADIUS = 90f;

    // 시간이 지나면 레벨에 따라 체력이 늘어나도록 구현할 예정
    // 최대 레벨 설정 필요
    public static final int MAX_LEVEL = 10;
    private int level;
    private float speed, distance;
    private float life, maxLife;
    protected RectF collisionRect = new RectF();

    private float range;
    private Type type;
    private float angle;

    // TODO : Gauge 색 설정 필요
    protected  static Gauge gauge;
    //protected static Gauge gauge = new Gauge(0.1f, R.color.enemy_gauge_fg, R.color.enemy_gauge_bg);

    private static Rect[][] rects_array;

    public static Enemy get(Type type, int level, float size){
        return Scene.top().getRecyclable(Enemy.class).init(type, level, size);
    }

    public Enemy() {
        super(0, 0, 0);

        if(rects_array == null){
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            rects_array = new Rect[Type.COUNT.ordinal()][];
            int x = 0;
            for(int i = 0; i < Type.COUNT.ordinal(); ++i){
                rects_array[i] = new Rect[2];
                for(int j = 0; j < 2; ++j){
                    rects_array[i][j] = new Rect(x, 0, x+h, h);
                    x += h;
                }
            }
        }
    }

    private static final Random rand = new Random();
    private Enemy init(Type type, int level, float size){
        if(type == Type.RANDOM){
            int value = rand.nextInt(Type.POSSIBILITY_SUM);
            for(int i = 0; i < Type.POSSIBILITIES.length; ++i){
                value -= Type.POSSIBILITIES[i];
                if(value < 0){
                    type = Type.values()[i];
                    break;
                }
            }
        }
        this.type = type;
        this.width = this.height = size;
        dx = dy = 0;
        this.distance = 0;
        this.level = level;
        range = (type == Type.boss) ? 7f : 0;

        // TODO : 이미지 추가 필요
        //this.setImageResourceId(R.mipmap.enemy);

        // TODO : 레벨에 따라 체력, 속도가 얼마나 올라갈지 설정
        this.life = this.maxLife = (level + 1) * 10;
        this.speed = (level + 1) * 10;

        return this;
    }

    public int getScore() {return (level + 1) * 100; }
    public boolean decreaseLife(int power){
        life -= power;
        return life <= 0;
    }

    @Override
    public void update() {
        distance += speed * GameView.frameTime;
        float maxDiff = width / 5;
        dx += (2 * maxDiff * rand.nextFloat() - maxDiff) * GameView.frameTime;
        if (dx < -maxDiff) dx = -maxDiff;
        else if (dx > maxDiff) dx = maxDiff;
        dy += (2 * maxDiff * rand.nextFloat() - maxDiff) * GameView.frameTime;
        if (dy < -maxDiff) dy = -maxDiff;
        else if (dy > maxDiff) dy = maxDiff;

        // TODO : Path 추가
//        pm.getPosTan(distance, pos, tan);
//        moveTo(pos[0] + dx, pos[1] + dy);
//        angle = (float)(Math.atan2(tan[1], tan[0]) * 180 / Math.PI) ;
//        if (distance > length) {
//            PracticeScene scene = (PracticeScene) Scene.top();
//            if (scene == null) return;
//            scene.remove(PracticeScene.Layer.enemy, this);
//        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(angle, x, y);
        super.draw(canvas);
        canvas.restore();
        float size = width * 2 / 3;
//        if (gauge == null) {
//            //gauge = new Gauge(0.2f, R.color.flyHealthFg, R.color.flyHealthBg);
//        }
        gauge.draw(canvas, x - size / 2, y + size / 2, size, life / maxLife);
    }
    private void bossAttack() {
        ArrayList<IGameObject> targets = setTarget();

        for(IGameObject gameObject : targets) {
            if(!(gameObject instanceof Tower)) continue;

            Tower tower = (Tower) gameObject;
            tower.setAttacked(true);
        }
    }

    private ArrayList<IGameObject> setTarget() {
        ArrayList<IGameObject> towers = PracticeScene.top().objectsAt(PracticeScene.Layer.tower);
        ArrayList<IGameObject> targets = new ArrayList<IGameObject>();

        float mx = this.x;
        float my = this.y;
        float range = this.range;

        for(IGameObject gameObject : towers){
            // range안에 있는 tower들을 targets에 push
            if(!(gameObject instanceof Tower)) continue;

            Tower tower = (Tower) gameObject;
            float dx = tower.x - mx;
            float dy = tower.y - my;
            float distanceSquared = dx * dx + dy * dy;

            if(distanceSquared <= range * range){
                targets.add(gameObject);
            }
        }

        return targets;
    }

    @Override
    public void onRecycle(){
    }

    @Override
    public PracticeScene.Layer getLayer() { return PracticeScene.Layer.enemy; }
}
