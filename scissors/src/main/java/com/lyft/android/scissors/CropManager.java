package com.lyft.android.scissors;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created by daniel on 8/22/16.
 */
public class CropManager {

    private float originalSrcViewScaleRatio = -1.0f;
    private float originalScale = 1f;
    private int bitmapWidth;
    private int bitmapHeight;
    private int originalPositionX = -1;
    private int originalPositionY = -1;
    private int rotation = 0;

    private float scale = 1;

    public void applyScaleRotation(Matrix matrix) {
        matrix.postTranslate(-bitmapWidth / 2.0f, -bitmapHeight / 2.0f);
        matrix.postScale(scale, scale);
        matrix.postRotate(rotation);
    }

    public void setRotation(int rotation) {
        this.rotation = (rotation + 360) % 360;
        originalPositionX = -1;
        originalPositionY = -1;
        originalScale = 1f;
    }

    public boolean reset(int bitmapWidth, int bitmapHeight, int viewportWidth, int viewportHeight) {
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        if (bitmapWidth > 0 && bitmapHeight > 0) {
            findOriginalScaleFactor(viewportWidth, viewportHeight);
            return true;
        }
        return false;
    }

    public void scale(float scaleFactor) {
        float newScale = scaleFactor * scale;
        newScale = newScale < originalSrcViewScaleRatio * 1 ? originalSrcViewScaleRatio : newScale;
        newScale = newScale > originalSrcViewScaleRatio * 3 ? originalSrcViewScaleRatio * 3 : newScale;
        scale = newScale;
    }

    private Point getRotatedBitmapDims() {
        boolean rotated180 = rotation % 180 == 0;
        int widthSize = rotated180 ? bitmapWidth : bitmapHeight;
        int heightSize = rotated180 ? bitmapHeight : bitmapWidth;
        return new Point(widthSize, heightSize);
    }

    Point getLimits(int availableWidth, int availableHeight) {
        Point point = getRotatedBitmapDims();
        int horizontalLimit = computeLimit((int) (point.x * scale), availableWidth);
        int verticalLimit = computeLimit((int) (point.y * scale), availableHeight);
        return new Point(horizontalLimit, verticalLimit);
    }

    PointF getOriginalPositionOnBitmap(int horizontalLimit, int verticalLimit) {
        float x = 0;
        float y = 0;

        if (originalPositionX >= 0 && originalPositionY >= 0) {
            int posX = originalPositionX;
            int posY = originalPositionY;

            if (rotation % 180 == 90) {
                posX = originalPositionY;
                posY = originalPositionX;
            }

            x = getPositionRelativeToCenter(posX, horizontalLimit, originalSrcViewScaleRatio);
            y = getPositionRelativeToCenter(posY, verticalLimit, originalSrcViewScaleRatio);

            if (rotation == 90) {
                x = -x;
            } else if (rotation == 180) {
                x = -x;
                y = -y;
            } else if (rotation == 270) {
                y = -y;
            }
        }
        return new PointF(x, y);
    }

    private static float getPositionRelativeToCenter(int originalPosition, int limit, float scale) {
        return -originalPosition * 1.0f * scale + limit;

    }

    void setOriginalPositionAndScale(int x, int y, float scale) {
        this.originalPositionX = x;
        this.originalPositionY = y;
        this.originalScale = scale;
    }

    private void findOriginalScaleFactor(int availableWidth, int availableHeight) {
        Point point = getRotatedBitmapDims();
        final float fw = (float) availableWidth / point.x;
        final float fh = (float) availableHeight / point.y;
        float minimumScale = Math.max(fw, fh);
        originalSrcViewScaleRatio = minimumScale * originalScale;
        scale = originalSrcViewScaleRatio;
    }

    private static int computeLimit(int bitmapSize, int viewportSize) {
        return (bitmapSize - viewportSize) / 2;
    }

}
