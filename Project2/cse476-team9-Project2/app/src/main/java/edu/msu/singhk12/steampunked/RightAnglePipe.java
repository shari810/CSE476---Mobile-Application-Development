package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RightAnglePipe extends Pipe{
    /**
     * Constructor for the RightAnglePipe
     *
     * @param context for the pipoe
     */
    public RightAnglePipe(Context context) {
        super(false, true, true, false, context, R.drawable.ninety);
    }
}
