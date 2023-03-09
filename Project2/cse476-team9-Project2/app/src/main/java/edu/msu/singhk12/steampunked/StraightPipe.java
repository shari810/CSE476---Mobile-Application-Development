package edu.msu.singhk12.steampunked;

import android.content.Context;

public class StraightPipe extends Pipe{
    /**
     * Constructor for the StraightPipe
     * @param context Context pipe is in
     */
    public StraightPipe(Context context) {
        super(true, false, true, false, context, R.drawable.straight);
    }
    
}
