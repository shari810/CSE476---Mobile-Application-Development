package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * The clock view class
 */
public class ClockView extends View {
    /**
     * The image bitmap. None initially.
     */
    private Bitmap clockBitmap = null;

    /**
     * Paint to use when drawing the custom color hat
     */
    private Paint linePaint;
    /**
     * The rotation angle for the clock
     */
    private int rotateAngle = 45;

    public ClockView(Context context) {
        super(context);
        init(null, 0);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        clockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.streamgauge);

        linePaint = new Paint();
        linePaint.setStrokeWidth(10);
    }

    /**
     * Handle a draw event
     * @param canvas canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
         * Determine the margins and scale to draw the image
         * centered and scaled to maximum size on any display
         */
        // Get the canvas size
        float wid = canvas.getWidth();
        float hit = canvas.getHeight();

        // What would be the scale to draw the where it fits both
        // horizontally and vertically?
        float scaleH = wid / clockBitmap.getWidth();
        float scaleV = hit / clockBitmap.getHeight();

        // Use the lesser of the two
        /**
         * Image drawing scale
         */
        float imageScale = scaleH < scaleV ? scaleH : scaleV;

        float radius;
        if (wid < hit) {
            radius = wid / 2 / imageScale;
        }
        else {
            radius = hit / 2 / imageScale;
        }

        // What is the scaled image size?
        float iWid = imageScale * clockBitmap.getWidth();
        float iHit = imageScale * clockBitmap.getHeight();

        // Determine the top and left margins to center
        /**
         * Image left margin in pixels
         */
        float marginLeft = (wid - iWid) / 2;
        /**
         * Image top margin in pixels
         */
        float marginTop = (hit - iHit) / 2;

        /*
         * Draw the image bitmap
         */
        canvas.save();
        canvas.translate(marginLeft, marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(clockBitmap, 0, 0, null);

        canvas.translate(clockBitmap.getWidth() / 2, clockBitmap.getHeight() / 2);
        canvas.rotate(rotateAngle);
        canvas.drawLine(0, 0, 0, radius, linePaint);

        canvas.restore();
    }

    /**
     * Reset the clock
     */
    public void reset() {
        rotateAngle = 45;
        invalidate();
    }

    /**
     * Increase the clock rotation angle
     */
    public void increaseAngle() {
        rotateAngle += 9;
        invalidate();
    }
}