package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OpenValve extends Pipe{

    /**
     * Completed puzzle bitmap
     */
    private Bitmap puzzleComplete;

    /**
     * Collection of puzzle pieces
     */
    public ArrayList<Pipe> pieces = new ArrayList<Pipe>();
    /**
     * Constructor for the OpenValve
     *
     * @param context the context this valve
     */
    public OpenValve(Context context) {
        super(false, false, false, false, context, R.drawable.handle);
    }
}