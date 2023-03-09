package edu.msu.singhk12.steampunked;

import android.content.Context;

public class NubPipe extends Pipe{
    /**
     * Constructor for the NubPipe
     *
     * @param context the context this pipe is in
     */
    public NubPipe(Context context) {

        super(false, false, true, false, context, R.drawable.cap);
    }
}

