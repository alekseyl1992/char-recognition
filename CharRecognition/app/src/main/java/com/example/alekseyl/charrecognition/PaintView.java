package com.example.alekseyl.charrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
        path.moveTo(0, 0);

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

    public int[] getPixels() {
        buildDrawingCache();
        Bitmap bitmap = this.getDrawingCache();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmap, imageSideSize, imageSideSize, true);

        int size = imageSideSize * imageSideSize;
        int pixels[] = new int[size];

        scaledBitmap.getPixels(pixels, 0, imageSideSize, 0, 0, imageSideSize, imageSideSize);

        return pixels;
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
