package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;

public class StartPipe extends Pipe {
    /**
     * Constructor for the StartPipe
     *
     * @param context for the pipe
     */
    public StartPipe(Context context) {
        super(false, false, true, false, context, R.drawable.straight);
    }
}
