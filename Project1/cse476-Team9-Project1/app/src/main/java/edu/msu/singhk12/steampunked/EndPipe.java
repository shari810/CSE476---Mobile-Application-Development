package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.graphics.Canvas;

public class EndPipe extends Pipe {
    /**
     * Constructor for the EndPipe
     *
     * @param context the endpipe
     */
    public EndPipe(Context context) {
        super(false, false, true, false, context, R.drawable.gauge);
    }

    /**
     * The overriden drawing for the end pipe
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param gridSize Size we draw one puzzle in pixels
     */
    @Override
    public void draw(Canvas canvas, int marginX, int marginY, float gridSize) {
        canvas.save();
        canvas.translate(0, 50);

        super.draw(canvas, marginX, marginY, gridSize);
        canvas.restore();
    }
}
