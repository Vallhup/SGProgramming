package com.example.game.GameOverScene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.game.MainScene.MainScene;

import org.w3c.dom.Text;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class GameOverScene extends Scene {
    private Paint paint;
    private final String message = "Game Over";
    public enum Layer{
        bg, ui
    }

    public GameOverScene() {
        initLayers(Layer.values().length);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(128);                          // 텍스트 크기 (픽셀 단위)
        paint.setTextAlign(Paint.Align.CENTER);         // 가운데 정렬
        paint.setAntiAlias(true);                       // 계단 현상 제거
    }

    @Override
    public void draw(Canvas canvas) {
        // 배경 처리 (필요시)
        super.draw(canvas);
        // 화면 중앙에 텍스트 그리기
        float x = Metrics.width  * 0.5f;
        float y = Metrics.height * 0.5f - ((paint.descent() + paint.ascent()) / 2);
        canvas.drawText(message, x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            new MainScene().change();
            return true;
        }

        return false;
    }

}
