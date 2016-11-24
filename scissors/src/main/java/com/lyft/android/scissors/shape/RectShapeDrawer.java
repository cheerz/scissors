package com.lyft.android.scissors.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by daniel on 11/21/16.
 */

public class RectShapeDrawer implements ShapeDrawer {
    private Paint borderPaint = new Paint();

    public RectShapeDrawer() {
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStrokeWidth(3);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void drawShapeAndBorder(Canvas canvas, int viewportWidth, int viewportHeight, int width, int height, Paint viewportPaint) {
        final int left = (width - viewportWidth) / 2;
        final int top = (height - viewportHeight) / 2;

        float[] border = new float[]{
                left, top, left + viewportWidth, top,
                left + viewportWidth, top, left + viewportWidth, top + viewportHeight,
                left, top + viewportHeight, left + viewportWidth, top + viewportHeight,
                left, top, left, top + viewportHeight
        };

        canvas.drawLines(border, borderPaint);
    }
}
