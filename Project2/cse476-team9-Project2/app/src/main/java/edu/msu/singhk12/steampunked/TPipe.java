package edu.msu.singhk12.steampunked;

import android.content.Context;

public class TPipe extends Pipe{
    /**
     * Constructor for the TPipe
     *
     * @param context for the pipe
     */
    public TPipe (Context context) {
        super(true, true, true, false, context, R.drawable.tee);
    }
}
