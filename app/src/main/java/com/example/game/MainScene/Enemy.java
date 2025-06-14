package com.example.game.MainScene;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;

import androidx.core.graphics.PathParser;

import com.example.spgpproject.R;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.SheetSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class Enemy extends SheetSprite implements IRecyclable {
    private static final String TAG = Enemy.class.getSimpleName();
    private float range;
    private Type type;
    private float bossSkillTimer = 0;

    public enum Type {
        boss, normal;
        float getMaxHealth() {
            return HEALTHS[ordinal()];
        }
        static final float[] HEALTHS = { 130, 50 };
        static final int[] POSSIBILITIES = { 5, 10 };
        static int POSSIBILITY_SUM;
        static {
            POSSIBILITY_SUM = 0;
            for (int p : POSSIBILITIES) {
                POSSIBILITY_SUM += p;
            }
        }
        static Type random() {
            int value = rand.nextInt(Type.POSSIBILITY_SUM);
            for (int i = 0; i < Type.POSSIBILITIES.length; i++) {
                value -= Type.POSSIBILITIES[i];
                if (value < 0) {
                    Type type = Type.values()[i];
                    return type;
                }
            }
            return boss;
        }
    }
    public Enemy() {
        super(R.mipmap.enemy, 2.0f);
        if (rects_array == null) {
            int type_count = Type.values().length;
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            rects_array = new Rect[type_count][];
            int x = 0;
            for (int i = 0; i < type_count; i++) {
                rects_array[i] = new Rect[2];
                for (int j = 0; j < 2; j++) {
                    rects_array[i][j] = new Rect(x, 0, x + h, h);
                    x += h;
                }
            }
        }
        setPosition(0, 0, 200, 200);
    }
    public void setType(boolean boss)
    {
        if(boss) {
            type = Type.boss;
        } else {
            type = Type.normal;
        }
    }
    public static Enemy get(boolean boss, float speedRatio) {
        Enemy.Type type = boss ? Type.boss : Type.normal;
        float size = rand.nextFloat() * 100 + 150;
        if (boss) {
            size *= 1.5f;
        }
        float speed = speedRatio * (rand.nextFloat() * 50 + 75);
        return Scene.top().getRecyclable(Enemy.class).init(type, size, speed);
    }
    public Enemy init(Type type, float size, float speed) {
        this.type = type;
        srcRects = rects_array[type.ordinal()];
        setPosition(0, 0, size, size);
        distance = 0;
        dx = dy = 0;
        this.speed = speed;
        range = (type == Type.boss) ? 500.0f : 0;
        life = maxLife = displayLife = type.getMaxHealth() * (0.9f + rand.nextFloat() * 0.2f);
        this.bossSkillTimer = 0;
        update();
        return this;
    }

    private static final Random rand = new Random();
    private static final PathMeasure pm;
    private static final float pathLength;
    private static final Path path;

    static {
        path = PathParser.createPathFromPathData(
                "M -128,1817.6\n" +
                        "C 198.4,1785.6 384,1558.4 384,1280\n" +
                        "C 384,1001.6 86.4,956.8 89.6,656\n" +
                        "C 92.8,355.2 332.8,54.4 640,51.2\n" +
                        "C 947.2,48 1177.6,339.2 1184,649.6\n" +
                        "C 1190.4,960 988.8,924.8 992,1251.2\n" +
                        "C 995.2,1577.6 1440,1692.8 1609.6,1692.8\n" +
                        "C 1779.2,1692.8 2217.6,1516.8 2220.8,1264\n" +
                        "C 2224,1011.2 1993.6,940.8 1993.6,662.4\n" +
                        "C 1993.6,384 2236.8,83.2 2576,86.4\n" +
                        "C 2915.2,89.6 3120,419.2 3110.4,675.2\n" +
                        "C 3100.8,931.2 2816,1078.4 2819.2,1340.8\n" +
                        "C 2822.4,1603.2 3155.2,1782.4 3360,1766.4\n"
        );

        pm = new PathMeasure(path, false);
        pathLength = pm.getLength();
    }

    private static Rect[][] rects_array;
    private float distance, speed, angle;
    private float life, maxLife, displayLife;
    private static Gauge gauge;

    public boolean decreaseLife(float power) {
        life -= power;
        return life <= 0;
    }

    public void decreaseSpeed(float power) {
        speed -= power;
        if(speed < 20.0f){
            speed = 20.0f;
        }
    }

    public int money() {
        return (type == Type.boss) ? 4 : 2;
    }

    public int score() {
        return (type == Type.boss) ? 3 : 1;
    }

    private float dx, dy;
    private final float[] pos = new float[2];
    private final float[] tan = new float[2];

    @Override
    public void update() {
        if (life != displayLife) {
            float step = maxLife / 50;
            float diff = life - displayLife;
            if (diff < -step) {
                displayLife -= step;
            } else if (diff > step) {
                displayLife += step;
            } else {
                displayLife = life;
            }
        }
        distance += speed * GameView.frameTime;
        if (distance > pathLength) {
            MainScene scene = (MainScene) Scene.top();

            if (type == Type.boss) {
                scene.decreaseLife(3);
            } else {
                scene.decreaseLife(1);
            }

            scene.remove(MainScene.Layer.enemy, this);
            return;
        }

        bossSkillTimer += GameView.frameTime;
        if(bossSkillTimer >= 5.0f) {
            bossAttack();
            bossSkillTimer = 0;
        }

        float maxDiff = width / 5;
        dx += (2 * maxDiff * rand.nextFloat() - maxDiff) * GameView.frameTime;
        if (dx < -maxDiff) dx = -maxDiff;
        else if (dx > maxDiff) dx = maxDiff;
        dy += (2 * maxDiff * rand.nextFloat() - maxDiff) * GameView.frameTime;
        if (dy < -maxDiff) dy = -maxDiff;
        else if (dy > maxDiff) dy = maxDiff;

        pm.getPosTan(distance, pos, tan);
        setPosition(pos[0] + dx, pos[1] + dy);
        angle = (float) Math.toDegrees(Math.atan2(tan[1], tan[0]));
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
        ArrayList<IGameObject> towers = MainScene.top().objectsAt(MainScene.Layer.tower);
        ArrayList<IGameObject> targets = new ArrayList<IGameObject>();

        float mx = this.x;
        float my = this.y;
        float range = this.range;

        for(IGameObject gameObject : towers){
            if(!(gameObject instanceof Tower)) continue;

            Tower tower = (Tower) gameObject;
            float dx = tower.getX() - mx;
            float dy = tower.getY() - my;
            float distanceSquared = dx * dx + dy * dy;

            if(distanceSquared <= range * range){
                targets.add(gameObject);
            }
        }

        return targets;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        super.draw(canvas);
        canvas.rotate(angle, x, y);
        canvas.restore();
        float barSize = width * 2 / 3;
        if (gauge == null) {
            gauge = new Gauge(0.2f, R.color.enemy_health_fg, R.color.enemy_health_bg);
        }
        gauge.draw(canvas, x - barSize / 2, y + barSize / 2, barSize, displayLife / maxLife);
    }
    @Override
    public void onRecycle() {}


}
