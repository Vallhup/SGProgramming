package com.example.game;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.spgpproject.BuildConfig;
import com.example.spgpproject.R;

import java.util.Arrays;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Selector extends Sprite {
    private static final String TAG = Selector.class.getSimpleName();
    private static final float TILE_SIZE = 100;
    private static final float SELECTOR_SIZE = 2 * TILE_SIZE;
    private final MainScene scene;
    private Tower tower;
    private static final int[] MENU_ITEMS_BLANK = {};
    private static final int[] MENU_ITEMS_INSTALL = {
            R.mipmap.attacktower, R.mipmap.slowtower
    };
    private static final int[] MENU_ITEMS_CANNON = {
            R.mipmap.upgrade, R.mipmap.uninstall,
    };
    private int[] menuItems = MENU_ITEMS_BLANK;
    private final Bitmap menuBgBitmap, notAvailableBitmap;
    private final Paint alphaPaint = new Paint();
    private ValueAnimator alphaAnimator;
    private static final int ALPHA_ANIM_DURATION_MSEC = 300;

    public Selector(MainScene scene) {
        super(R.mipmap.selection);
        this.scene = scene;
        menuBgBitmap = BitmapPool.get(R.mipmap.menu_bg);
        notAvailableBitmap = BitmapPool.get(R.mipmap.not_available);
        setPosition(-SELECTOR_SIZE, -SELECTOR_SIZE, SELECTOR_SIZE, SELECTOR_SIZE);
    }
    private void hideSelector() {
        setPosition(-SELECTOR_SIZE, -SELECTOR_SIZE);
    }

    private final RectF menuRect = new RectF();
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (tower != null) {
            tower.drawRange(canvas);
        }
        prepareMenuRect();
        for (int item: menuItems) {
            menuRect.offset(SELECTOR_SIZE, 0);
            canvas.drawBitmap(menuBgBitmap, null, menuRect, alphaPaint);
            Bitmap itemBitmap = BitmapPool.get(item);
            canvas.drawBitmap(itemBitmap, null, menuRect, alphaPaint);
            if (prohibits(item)) {
                canvas.drawBitmap(notAvailableBitmap, null, menuRect, alphaPaint);
            }
        }
    }

    private boolean prohibits(int item) {
        if (item == R.mipmap.attacktower) {
            return !canInstall(1);
        }
        if (item == R.mipmap.slowtower) {
            return !canInstall(2);
        }
        if (item == R.mipmap.upgrade) {
            if (tower == null) return true;
            int score = scene.score.getScore();
            int cost = tower.getUpgradeCost();
            return cost > score;
        }
        return false;
    }

    private boolean canInstall(int type) {
        int cost = Tower.getInstallationCost(type);
        int score = scene.score.getScore();
        return cost <= score;
    }

    private void prepareMenuRect() {
        menuRect.set(dstRect);
        float right = dstRect.right + SELECTOR_SIZE * menuItems.length;
        if (right > Metrics.width) {
            menuRect.offset(-SELECTOR_SIZE * (menuItems.length + 1), 0);
        }
    }

    public boolean onTouch(int action, float x, float y) {
        boolean processed = handleMenuItem(action, x, y);
        if (processed) {
            return false;
        }
        tower = findCannonAt(x, y);
        if (tower != null) {
            if (action == MotionEvent.ACTION_UP) {
                setMenuItems(MENU_ITEMS_CANNON);
            } else {
                bitmap = BitmapPool.get(R.mipmap.selection);
                setPosition(tower.getX(), tower.getY());
                setMenuItems(MENU_ITEMS_BLANK);
            }
            return true;
        }
        int mapX = (int)(x / TILE_SIZE);
        int mapY = (int)(y / TILE_SIZE);
        float cx = (mapX + 1) * TILE_SIZE;
        float cy = (mapY + 1) * TILE_SIZE;

        setPosition(cx, cy);

        boolean possible = !intersectsIfInstalledAt(cx, cy) && scene.bg.canInstallAt(mapX, mapY);
        int resId = possible ? R.mipmap.selection : R.mipmap.sel_non_installable;
        bitmap = BitmapPool.get(resId);

        if (action != MotionEvent.ACTION_UP) {
            setMenuItems(MENU_ITEMS_BLANK);
            return true;
        }
        if (!possible) {
            hideSelector();
            return true;
        }
        setMenuItems(MENU_ITEMS_INSTALL);
        return true;
    }

    private boolean handleMenuItem(int action, float x, float y) {
        if (action != MotionEvent.ACTION_DOWN || menuItems.length == 0) return false;
        prepareMenuRect();
        for (int item : menuItems) {
            menuRect.offset(SELECTOR_SIZE, 0);
            if (menuRect.contains(x, y)) {
                if (BuildConfig.DEBUG) {
                    Resources res = GameView.view.getResources();
                    String name = res.getResourceEntryName(item);
                    Log.d(TAG, "Menu selected: " + name + "(" + item + ")");
                }
                boolean done = doItemAction(item);
                if (done) {
                    hideSelector();
                }
                return true;
            }
        }
        return false;
    }

    private boolean doItemAction(int menuItem) {
        if (menuItem == R.mipmap.attacktower) {
            return installCannon(1);
        } else if (menuItem == R.mipmap.slowtower) {
            return installCannon(2);
        } else if (menuItem == R.mipmap.upgrade) {
            return upgradeCannon();
        } else if (menuItem == R.mipmap.uninstall) {
            return uninstallCannon();
        }
        return false;
    }

    private boolean installCannon(int type) {
        int cost = Tower.getInstallationCost(type);
        int score = scene.score.getScore();
        if (cost > score) return false;
        scene.score.add(-cost);
        Tower tower = new Tower(type, (int)x, (int)y);
        scene.add(MainScene.Layer.tower, tower);
        return true;
    }
    private boolean upgradeCannon() {
        int cost = tower.getUpgradeCost();
        int score = scene.score.getScore();
        if (cost > score) return false;
        scene.score.add(-cost);
        tower.upgrade();
        return true;
    }

    private boolean uninstallCannon() {
        int price = tower.getSellPrice();
        scene.score.add(price);
        tower.uninstall();
        tower = null;
        return true;
    }

    private void setMenuItems(int... items) {
        menuItems = items;
        if (BuildConfig.DEBUG) {
            // 문자열 생성 비용이 있는 로그들은 BuildConfig.DEBUG 로 감싸는 것이 유리하다
            Resources res = GameView.view.getResources();
            String[] names = Arrays.stream(items).mapToObj(resId->{
                String name = res.getResourceEntryName(resId);
                return name + "(" + resId + ")";
            }).toArray(String[]::new);
            Log.d(TAG, "Items = " + Arrays.toString(names));
        }

        if (alphaAnimator == null) {
            alphaAnimator = ValueAnimator
                    .ofInt(0, 192)
                    .setDuration(ALPHA_ANIM_DURATION_MSEC);
            alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    alphaPaint.setAlpha((Integer)valueAnimator.getAnimatedValue());
                }
            });
        }
        alphaAnimator.start();
    }
    private Tower findCannonAt(float x, float y) {
        for (IGameObject obj: scene.objectsAt(MainScene.Layer.tower)) {
            Tower cannon = (Tower) obj;
            if (cannon.containsPoint(x, y)) {
                return cannon;
            }
        }
        return null;
    }
    private boolean intersectsIfInstalledAt(float x, float y) {
        for (IGameObject obj: scene.objectsAt(MainScene.Layer.tower)) {
            Tower cannon = (Tower) obj;
            if (cannon.intersectsIfInstalledAt(x, y)) {
                return true;
            }
        }
        return false;
    }
}
