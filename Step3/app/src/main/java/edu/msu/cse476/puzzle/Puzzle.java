package edu.msu.cse476.puzzle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class Puzzle {

    public void setPuzzleView(PuzzleView puzzleView) {
        this.puzzleView = puzzleView;
    }

    PuzzleView puzzleView;



    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle.
     */
    final static float SCALE_IN_VIEW = 0.9f;
    /**
     * Width of green border in display points
     */
    final static float BORDER_WIDTH = 3.0f; // make the border easier to see.
    /**
     * Paint for filling the area the puzzle is in
     */
    private final Paint fillPaint;
    /**
     * Paint for outlining the area the puzzle is in
     */
    private final Paint outlinePaint;
    /**
     * Completed puzzle bitmap
     */
    private final Bitmap puzzleComplete;
    /**
     * Collection of puzzle pieces
     */
    public ArrayList<PuzzlePiece> pieces = new ArrayList<>();

    /**
     * The size of the puzzle in pixels
     */
    private int puzzleSize;

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;

    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    private PuzzlePiece dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    /**
     * Random number generator
     */
    private static Random random = new Random();

    /**
     * The name of the bundle keys to save the puzzle
     */
    private final static String LOCATIONS = "Puzzle.locations";
    private final static String IDS = "Puzzle.ids";



    public Puzzle(Context context) {
        // Create paint for filling the area the puzzle will
        // be solved in.
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(0xff008000);
        outlinePaint.setStyle(Paint.Style.STROKE);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float borderWidth = BORDER_WIDTH * (metrics.densityDpi/160f);

        outlinePaint.setStrokeWidth(borderWidth);

        // Load the solved puzzle image
        puzzleComplete =
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.sparty_done);
        // Load the puzzle pieces
        pieces.add( new PuzzlePiece(context, R.drawable.sparty1, 0.259f, 0.238f));
        //
        //  Delete the next 3 lines when working on Step 3
        //
        PuzzlePiece p = pieces.get(0);
        p.setX(0.259f);
        p.setY(0.238f);

        pieces.add(new PuzzlePiece(context, R.drawable.sparty2, 0.666f, 0.158f));
        pieces.add(new PuzzlePiece(context, R.drawable.sparty3, 0.741f, 0.501f));
        pieces.add(new PuzzlePiece(context, R.drawable.sparty4, 0.341f, 0.519f));
        pieces.add(new PuzzlePiece(context, R.drawable.sparty5, 0.718f, 0.834f));
        pieces.add(new PuzzlePiece(context, R.drawable.sparty6, 0.310f, 0.761f));

        shuffle();



    }

    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = Math.min(wid, hit);

        puzzleSize = (int)(minDim * SCALE_IN_VIEW);
        // Compute the margins so we center the puzzle
        marginX = (wid - puzzleSize) / 2;
        marginY = (hit - puzzleSize) / 2;
        //
        // Draw the outline of the puzzle, first the gray background then the
        // green border.
        //
        canvas.drawRect(marginX, marginY,
                marginX + puzzleSize, marginY + puzzleSize, fillPaint);
        canvas.drawRect(marginX, marginY, marginX + puzzleSize, marginY + puzzleSize, outlinePaint);

        scaleFactor = (float)puzzleSize /
                (float)puzzleComplete.getWidth();
        canvas.save();
        canvas.translate(marginX, marginY);
        canvas.scale(scaleFactor, scaleFactor);
        //canvas.drawBitmap(puzzleComplete, 0, 0, null);
        canvas.restore();
        for(PuzzlePiece piece : pieces) {
            piece.draw(canvas, marginX, marginY, puzzleSize, scaleFactor);
        }

        if (isDone())
        {
            //BitmapFactory.decodeResource(this.puzzleView.getResources(), R.drawable.sparty_done);
            Bitmap done = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.puzzleView.getResources(), R.drawable.sparty_done),puzzleSize,puzzleSize,false);
            canvas.drawBitmap(done,marginX,marginY,null);
        }
    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {

        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //

        float relX = (event.getX() - marginX) / puzzleSize;
        float relY = (event.getY() - marginY) / puzzleSize;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                //Log.i("onTouchEvent", "ACTION_DOWN");
                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //Log.i("onTouchEvent", "ACTION_UP");
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:
                // If we are dragging, move the piece and force a redraw
                if(dragging != null) {
                    dragging.move(relX - lastRelX, relY - lastRelY);
                    lastRelX = relX;
                    lastRelY = relY;

                    int piecePos = pieces.indexOf(dragging);
                    pieces.remove(piecePos);
                    if (dragging.isSnapped())
                    {
                        pieces.add(0,dragging);
                    }
                    else
                    {
                        pieces.add(5,dragging);
                    }

                    view.invalidate();
                    return true;
                }
                //Log.i("onTouchEvent",  "ACTION_MOVE: " + event.getX() + "," + event.getY());
                break;
        }

        return false;
    }

    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {

        // Check each piece to see if it has been hit
        // We do this in reverse order so we find the pieces in front
        for(int p=pieces.size()-1; p>=0;  p--) {
            if(pieces.get(p).hit(x, y, puzzleSize, scaleFactor)) {
                // We hit a piece!
                dragging = pieces.get(p);
                lastRelX = x;
                lastRelY = y;
                return true;
            }
        }

        return false;
    }

    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {

        if(dragging != null) {
            if(dragging.maybeSnap()) {
                // We have snapped into place
                view.invalidate();
                if(isDone()) {
                    // The puzzle is done
                    // Instantiate a dialog box builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());

                    ShuffleListener listener = new ShuffleListener();
                    // Parameterize the builder
                    builder.setTitle(R.string.hurrah);
                    builder.setMessage(R.string.completed_puzzle);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setNegativeButton(R.string.shuffle, listener);

                    // Create the dialog box and show it
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }

            int piecePos = pieces.indexOf(dragging);
            pieces.remove(piecePos);
            if (dragging.isSnapped())
            {
                pieces.add(0,dragging);
            }
            else
            {
                pieces.add(5,dragging);
            }

            dragging = null;
            return true;
        }

        return false;
    }
    private class ShuffleListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            shuffle();
            puzzleView.invalidate();
        }
    }

    /**
     * Determine if the puzzle is done!
     * @return true if puzzle is done
     */
    public boolean isDone() {
        for(PuzzlePiece piece : pieces) {
            if(!piece.isSnapped()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Shuffle the puzzle pieces
     */
    public void shuffle() {
        for(PuzzlePiece piece : pieces) {
            piece.shuffle(random);
        }

    }

    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        float [] locations = new float[pieces.size() * 2];
        int [] ids = new int[pieces.size()];

        for(int i=0;  i<pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getId();
        }

        bundle.putFloatArray(LOCATIONS, locations);
        bundle.putIntArray(IDS,  ids);
    }


    /**
     * Read the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {

        float [] locations = bundle.getFloatArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);

        for(int i=0; i<ids.length-1; i++) {

            // Find the corresponding piece
            // We don't have to test if the piece is at i already,
            // since the loop below will fall out without it moving anything
            for(int j=i+1;  j<ids.length;  j++) {
                if(ids[i] == pieces.get(j).getId()) {
                    // We found it
                    // Yah...
                    // Swap the pieces
                    PuzzlePiece t = pieces.get(i);
                    pieces.set(i, pieces.get(j));
                    pieces.set(j, t);
                }
            }
        }

        for(int i=0;  i<pieces.size(); i++) {
            PuzzlePiece piece = pieces.get(i);
            piece.setX(locations[i*2]);
            piece.setY(locations[i*2+1]);
        }
    }


}
