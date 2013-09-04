
package com.kingbright.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SnakeView extends View {
    private static final String STATUS = "status";

    // Game State
    public static final int RUNNING = 1;
    public static final int FAIL = 3;
    public static final int WELCOME = 4;

    private float mX;
    private float mY;
    private int mColumn;
    private int mRow;
    private Snake mSnake;
    private FruitsManager mFruitsManager;

    private int mState = WELCOME;

    public SnakeView(Context context) {
        super(context);
    }

    public SnakeView(Context context, AttributeSet as) {
        super(context, as);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();
    }

    public Parcelable onSave(Bundle state) {
        Bundle save = state;
        save.putInt(STATUS, mState);
        if (mState == RUNNING) {
            mSnake.save(save);
        }
        return save;
    }

    public void onRestore(Bundle state) {
        // Bundle save = state;
        // mState = save.getInt(STATUS);
        // if (mState != 0 && mState == RUNNING) {
        // mSnake = new Snake(save);
        // } else {
        //
        // }
    }

    private void init() {
        if (mSnake == null) {
            mRow = getHeight() / Snake.WIDTH;
            mColumn = getWidth() / Snake.WIDTH;
            mSnake = new Snake(mColumn, mRow, Color.RED);
            mSnake.gain(Color.BLUE);

            mSnake.gain(Color.GREEN);
            mSnake.gain(Color.YELLOW);
            mSnake.gain(Color.CYAN);
            mSnake.gain(Color.MAGENTA);
        }

        if (mFruitsManager == null) {
            mFruitsManager = new FruitsManager(mColumn, mRow);
        }
    }

    public void update() {
        if (mFruitsManager != null && mSnake != null) {
            mFruitsManager.generateFruits(mSnake);
            mSnake.move();
            mFruitsManager.onEatten(mSnake);
        }
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        drawFruits(canvas);
        drawSnake(canvas);
    }

    private void drawFruits(Canvas canvas) {
        if (mFruitsManager != null) {
            mFruitsManager.onDraw(canvas);
        }
    }

    private void drawSnake(Canvas canvas) {
        if (mSnake != null) {
            mSnake.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSnake == null) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int dir = caculateDirection(event.getX(), event.getY());
                if (dir != -1) {
                    mSnake.setDir(dir);
                }
                break;
        }
        return true;
    }

    private int caculateDirection(float x, float y) {
        if (mX == 0 && mY == 0) {
            mY = x;
            mY = y;
            return -1;
        }
        int dir = Math.abs(x - mX) > Math.abs(y - mY) ? (x - mX > 0 ? Snake.RIGHT : Snake.LEFT)
                : (y
                        - mY > 0 ? Snake.DOWN : Snake.UP);
        mX = 0;
        mY = 0;
        return dir;
    }
}
