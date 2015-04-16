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
    private static final float width = 48f;
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
        Bitmap bitmap = this.getDrawingCache();

        // crop
        RectF rect = new RectF();
        path.computeBounds(rect, true);

        System.out.println(rect);

        int minXY = Math.min((int) rect.left, (int) rect.top);
        if (minXY < 0)
            minXY = 0;

        int width = bitmap.getWidth();

        int maxXY = Math.max((int) rect.right, (int) rect.bottom);
        if (maxXY > width)
            maxXY = width;

        // center
        minXY = (minXY + (maxXY - width)) / 2;

        System.out.println("minXY: " + minXY);
        System.out.println("maxXY: " + maxXY);

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,
                minXY,
                minXY,
                maxXY - minXY,
                maxXY - minXY);

        // scale down
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                croppedBitmap, imageSideSize, imageSideSize, true);

        int size = imageSideSize * imageSideSize;
        int pixels[] = new int[size];

        // convert to vector
        scaledBitmap.getPixels(pixels, 0, imageSideSize, 0, 0, imageSideSize, imageSideSize);

        boolean biPixels[] = new boolean[pixels.length];

        for (int i = 0; i < pixels.length; ++i) {
            biPixels[i] = pixels[i] == -1;
        }

        return biPixels;
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
