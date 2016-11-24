package com.lyft.android.scissors.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by daniel on 11/21/16.
 */

public interface ShapeDrawer {

    void drawShapeAndBorder(Canvas canvas, int viewportWidth, int viewportHeight, int width, int height, Paint viewportPaint);
}
