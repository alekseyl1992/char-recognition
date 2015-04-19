package com.example.alekseyl.charrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
    private Paint paint;
    private Path path;
    private static final float width = 64f;
    private static final int color = Color.BLUE;

    public static int imageSideSize = 10;

    public PaintView(Context context) {
        super(context);
        init(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        this.setDrawingCacheEnabled(true);

        paint = new Paint();
        path = new Path();

        //paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                redraw();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        setDrawingCacheEnabled(true);
    }

    public boolean[] getPixels() {
        buildDrawingCache();
        Bitmap sourceBitmap = this.getDrawingCache();
        int sourceWidth = sourceBitmap.getWidth();
        int sourceHeight = sourceBitmap.getHeight();

        // get bounds
        RectF rect = new RectF();
        path.computeBounds(rect, true);
        int croppedWidth = (int) rect.width();
        int croppedHeight = (int) rect.height();

        int croppedPixels[] = new int[croppedWidth * croppedHeight];
        sourceBitmap.getPixels(croppedPixels, 0, croppedWidth,
                (int) rect.left, (int) rect.top,
                croppedWidth, croppedHeight);

        // calc offset
        int offsetX, offsetY;
        if (croppedHeight >= croppedWidth) {
            offsetX = (croppedHeight - croppedWidth) / 2;
            offsetY = 0;
        } else {
            offsetX = 0;
            offsetY = (croppedWidth - croppedHeight) / 2;
        }

        // center
        int centeredSideSize = Math.max(croppedWidth, croppedHeight);
        boolean centeredVector[] = new boolean[centeredSideSize * centeredSideSize];
        for (int y = 0; y < croppedHeight; ++y) {
            for (int x = 0; x < croppedWidth; ++x) {
                boolean color = croppedPixels[y * croppedWidth + x] != -1;
                int centeredX = offsetX + x;
                int centeredY = offsetY + y;

                centeredVector[centeredY * centeredSideSize + centeredX] = color;
            }
        }

        // scale down
        int scaleFactor = centeredSideSize / imageSideSize;

        boolean resultVector[] = new boolean[imageSideSize * imageSideSize];
        for (int y = 0; y < imageSideSize; ++y) {
            for (int x = 0; x < imageSideSize; ++x) {
                boolean avgColor = getAvgColor(
                        centeredVector,
                        centeredSideSize,
                        x * scaleFactor,
                        y * scaleFactor,
                        scaleFactor);
//                boolean avgColor = centeredVector[y * scaleFactor * centeredSideSize + x * scaleFactor];

                resultVector[y * imageSideSize + x] = avgColor;
            }
        }

        return resultVector;
    }

    private boolean getAvgColor(boolean matrix[], int sideSize, int fromX, int fromY, int probeSize) {
        int pixelsCount = probeSize * probeSize;
        int truePixelsCount = 0;

        for (int y = fromY; y < fromY + probeSize; ++y) {
            for (int x = fromX; x < fromX + probeSize; ++x) {
                if (matrix[y * sideSize + x])
                    ++truePixelsCount;
            }
        }

        float percentage = ((float) truePixelsCount) / pixelsCount;
        return percentage > 0.01;
    }

    public void clear() {
        path.reset();
        redraw();
    }

    protected void redraw() {
        setDrawingCacheEnabled(false);
        invalidate();
    }
}
