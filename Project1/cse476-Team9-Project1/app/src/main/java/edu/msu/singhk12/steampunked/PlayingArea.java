package edu.msu.singhk12.steampunked;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A representation of the playing area
 */
public class PlayingArea {
    /**
     * Percentage of the display width or height that
     * is occupied by the puzzle.
     */
    final static float SCALE_IN_VIEW = 0.95f;
    /**
     * The text size as a float
     */
    final float testTextSize = 48f;
    /**
     * The string for the player1 name
     */
    private String player1;
    /**
     * The string for the player2 name
     */
    private String player2;
    /**
     * Set the player1 name.
     * @param name name of player1
     */
    public void setPlayer1(String name) {player1 = name; }
    /**
     * Set the player2 name.
     * @param name name of player2
     */
    public void setPlayer2(String name) {player2 = name; }
    /**
     * The int indicating which player's turn
     */
    private int currentPlayer = 1;
    /**
     * Set the player turn.
     * @param turn turn for the player
     */
    public void setTurn(int turn) {
        currentPlayer = turn;
        dragging = null;
    }
    /**
     * Get's which which player's turn it is
     * @return currentPlayer integer
     */
    public int getTurn() {
        return currentPlayer;
    }

    /**
     * Width of the playing area (integer number of cells)
     */
    private final int width;

    /**
     * Height of the playing area (integer number of cells)
     */
    private final int height;

    /**
     * Storage for the pipes
     * First level: X, second level Y
     */
    private final Pipe [][] gridpipes;
    /**
     * The Startpipe for player1
     */
    private final StartPipe startPipe1;
    /**
     * The Startpipe for player2
     */
    private final StartPipe startPipe2;
    /**
     * The OpenValve for player1
     */
    private final OpenValve openValve1;
    /**
     * The OpenValve for player2
     */
    private final OpenValve openValve2;

    /**
     * Paint for filling the area the puzzle is in
     */
    private final Paint fillPaint;

    /**
     * Paint for outlining the area the puzzle is in
     */
    private final Paint outlinePaint;
    /**
     * The Oarea where the pipes will be held
     */
    private final PipeArea pipeArea;
    /**
     * Array for the leaks
     */
    private Leak [] leaks;
    /**
     * Array for the leaks X position
     */
    private float [] leaksX;
    /**
     * Array for the leaks Y position
     */
    private float [] leaksY;
    /**
     * Context for the game
     */
    private Context context;

    /**
     * The Endpipe for player1
     */
    private EndPipe endPipe1;
    /**
     * The Endpipe for player2
     */
    private EndPipe endPipe2;

    /**
     * Construct a playing area
     */
    public PlayingArea(int size, Context context) {
        this.context = context;

        this.width = this.height = size;

        // Allocate the playing area
        // Java automatically initializes all of the locations to null
        gridpipes = new Pipe[width][height];

        // Create paint for filling the area the puzzle will
        // be solved in.
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xffcccccc);

        //Create paint for the green outline
        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStrokeWidth(3);
        outlinePaint.setStyle(Paint.Style.STROKE);

        outlinePaint.setTextSize(testTextSize);

        //add open thing to grid
        startPipe1 = new StartPipe(context);
        startPipe2 = new StartPipe(context);

        startPipe1.setRotateAngle(270);
        startPipe2.setRotateAngle(270);

        add(startPipe1, 0, (int) height / 3);
        add(startPipe2, 0, (int) height * 2 / 3);

        openValve1 = new OpenValve(context);
        openValve2 = new OpenValve(context);

        openValve1.set(this, 0, height / 3);
        openValve2.set(this, 0, height * 2 / 3);

        //add end thing to grid
        /**
         * The Endpipe for player1
         */
        endPipe1 = new EndPipe(context);
        /**
         * The Endpipe for player2
         */
        endPipe2 = new EndPipe(context);

        endPipe1.setRotateAngle(90);
        endPipe2.setRotateAngle(90);

        add(endPipe1, width - 1, (int) height / 3 + 1);
        add(endPipe2, width - 1, (int) height * 2 / 3 + 1);

        pipeArea = new PipeArea(context);

        startPipe1.setPlayer(1);
        startPipe2.setPlayer(-1);

        openValve1.setPlayer(1);
        openValve2.setPlayer(-1);

        endPipe1.setPlayer(1);
        endPipe2.setPlayer(-1);
    }
    /**
     * Int for the size of the game
     */
    private int gameSize;
    /**
     * Int for the size of the gridsize
     */
    private float gridSize;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;
    /**
     * boolean to check for openvalve for player1
     */
    private boolean close1 = true;
    /**
     * boolean to check for openvalve for player2
     */
    private boolean close2 = true;
    /**
     * Set the player1 check.
     * @param check is valve open for the player1
     */
    public void setClose1(boolean check)
    {
        close1 = check;
    }
    /**
     * Set the player2 check.
     * @param check is valve open for the player2
     */
    public void setClose2(boolean check)
    {
        close2 = check;
    }
    /**
     * The scale for the drawing
     */
    private float scale = 1f;
    /**
     * boolean to check for if it's landscape
     */
    private boolean landscape;
    /**
     * boolean to check for if it has been updated
     */
    private boolean updated = false;
    /**
     * Drawing the Playing Area, Grid, Pipe Area, and all the pipes.
     * @param canvas the canvas everything will be drawn on
     */
    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        canvas.save();

        canvas.translate(scrollX, scrollY);

        gameSize = (int)(minDim * SCALE_IN_VIEW);
        gridSize = (float) gameSize / width;

        // Compute the margins so we center the puzzle
        if (minDim == wid){
            landscape = false;
            marginX = (wid - gameSize) / 2;
            marginY = 0;
        }
        else {
            landscape = true;
            marginX = 0;
            marginY = (hit - gameSize) / 2;
        }

        canvas.scale(scale, scale);
        //
        // Draw the outline of the puzzle
        //
        canvas.drawRect(marginX, marginY,
                marginX + gameSize, marginY + gameSize, fillPaint);

        //Draw the border of the puzzle area
        canvas.drawRect(marginX, marginY,
                marginX + gameSize, marginY + gameSize, outlinePaint);

        for (int i = 0; i < width; i++) {
            canvas.drawLine(marginX, gridSize * i + marginY, gameSize + marginX, gridSize * i + marginY, outlinePaint);
        }

        for (int i = 0; i < height; i++) {
            canvas.drawLine(gridSize * i + marginX, marginY, gridSize * i + marginX, gameSize + marginY, outlinePaint);
        }

        for (Pipe[] pieces : gridpipes){
            for (Pipe piece : pieces){
                if (piece != null)
                    piece.draw(canvas, marginX, marginY, gridSize);
            }
        }

        if (close1) {
            openValve1.setRotateAngle(0);
        }
        else {
            openValve1.setRotateAngle(90);
        }

        if (close2) {
            openValve2.setRotateAngle(0);
        }
        else {
            openValve2.setRotateAngle(90);
        }
        openValve1.draw(canvas, marginX, marginY, gridSize);
        openValve2.draw(canvas, marginX, marginY, gridSize);

        canvas.drawText(player1, marginX, marginY + (openValve1.getY()) * gridSize, outlinePaint);
        canvas.drawText(player2, marginX, marginY + (openValve2.getY()) * gridSize, outlinePaint);

        if (!updated) {
            pipeArea.updateLocation();
            updated = true;
        }
        pipeArea.draw(canvas);
        canvas.restore();
    }
    /**
     * Get the StartPipe for player1.
     */
    public StartPipe getStartPipe1(){
        return startPipe1;
    }
    /**
     * Get the StartPipe for player2.
     */
    public StartPipe getStartPipe2(){
        return startPipe2;
    }

    public EndPipe getEndPipe1() {return endPipe1;}
    public EndPipe getEndPipe2() {return endPipe2;}

    /**
     * Add a leak if one exists
     */
    public void addLeak(float x, float y, int d){
        Leak leak = new Leak(context);
        leak.addRotateAngle(d*90);
        add(leak,(int) x, (int) y);
    }

    /**
     * Get the pipe at a given location.
     * This will return null if outside the playing area.
     * @param x X location
     * @param y Y location
     * @return Reference to Pipe object or null if none exists
     */
    public Pipe getPipe(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return gridpipes[x][y];
    }

    /**
     * Add a pipe to the playing area
     * @param pipe Pipe to add
     * @param x X location
     * @param y Y location
     */
    public void add(Pipe pipe, int x, int y) {
        gridpipes[x][y] = pipe;
        pipe.set(this, x, y);
    }

    private class PipeArea {
        /**
         * Collection of puzzle pieces
         */
        private ArrayList<Pipe> randomPipe = new ArrayList<>();
        /**
         * Get the Pipe in the PlayingArea.
         */
        public ArrayList<Pipe> getPipe(){return randomPipe; }
        /**
         * Set the Pipe in the PlayingArea.
         * @param newPipe the pipe to be set in the playing area
         */
        public void setRandomPipe(ArrayList<Pipe> newPipe) {randomPipe = newPipe; }

        /**
         * Paint for filling the area the puzzle is in
         */
        private final Paint fillPaint;

        /**
         * Random number generator
         */
        private final Random random = new Random();
        /**
         * Context for the game
         */
        private final Context context;

        public PipeArea(Context context) {

            // Create paint for filling the area the pipes will be in
            // be solved in.
            fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            fillPaint.setColor(Color.GREEN);

            this.context = context;

            randomPipe.add(new NubPipe(context));
            randomPipe.add(new RightAnglePipe(context));
            //randomPipe.add(new StartPipe(context));
            randomPipe.add(new StraightPipe(context));
            randomPipe.add(new TPipe(context));

            shuffle();
        }
        /**
         * Shuffle for random pipe in the pipearea
         */
        public void shuffle() {
            while (randomPipe.size() < 5) {
                int rand = random.nextInt(3);

                Pipe random = null;
                if (rand == 0) {
                    random = new NubPipe(context);
                }
                else if (rand == 1) {
                    random = new RightAnglePipe(context);
                }
                else if (rand == 2) {
                    random = new StraightPipe(context);
                }
                else {
                    random = new TPipe(context);
                }
                randomPipe.add(random);
            }
            updateLocation();
        }
        /**
         * updates the locations and rotate angle
         */
        public void updateLocation() {
            int i = 0;
            for (Pipe pipe : randomPipe){
                if (!landscape) {
                    pipe.setRotateAngle(0);
                    pipe.setX(i);
                    pipe.setY(height);
                }
                else {
                    pipe.setRotateAngle(90);
                    pipe.setX(width);
                    pipe.setY(i);
                }
                i += 1;
            }
        }
        /**
         * Drawing Pipe Area and all the random pipes.
         * @param canvas the canvas everything will be drawn on
         */
        public void draw(Canvas canvas) {
            int wid = canvas.getWidth();
            int hit = canvas.getHeight();

            int playingAreahit;
            int playingAreawid;

            /**
             * Left margin in pixels
             */
            int randomMarginX;
            /**
             * Top margin in pixels
             */
            int randomMarginY;
            if (!landscape) {
                randomMarginX = marginX;
                randomMarginY = marginY + gameSize;
                playingAreahit = hit - marginY - gameSize;
                if (playingAreahit < gridSize)
                    playingAreahit = (int) gridSize;
                playingAreawid = gameSize;
            }
            else {
                randomMarginX = marginX + gameSize;
                randomMarginY = marginY;
                playingAreawid = wid - marginX - gameSize;
                if (playingAreawid < gridSize)
                    playingAreawid = (int) gridSize;
                playingAreahit = gameSize;
            }

            canvas.drawRect(randomMarginX, randomMarginY,
                    randomMarginX + playingAreawid, randomMarginY + playingAreahit, fillPaint);

            for (Pipe piece : randomPipe){
                piece.draw(canvas, marginX, marginY, gridSize);
            }
        }
        /**
         * If the discard button is pressed this sets the
         * pipe in the playing area to null
         */
        public void removeInstalled() {
            if (dragging != null)
                randomPipe.remove(dragging);
        }
        /**
         * If the discard button is pressed this sets the
         * pipe in the playing area to null
         */
        public void discard() {
            if (dragging != null)
                randomPipe.remove(dragging);
            else {
                int rand = random.nextInt(4);
                randomPipe.remove(rand);
            }

        }
    }
    /**
     * Shuffles a new pipe into the Area
     */
    public void shuffle() {
        pipeArea.removeInstalled();
        pipeArea.shuffle();
    }
    /**
     * Discards the pipe and shuffles the area
     */
    public void discard() {
        pipeArea.discard();
        pipeArea.shuffle();
    }
    /**
     * The pipe that has been clicked and is being dragged
     */
    private Pipe dragging;
//    private Pipe lastDraggedPipe;
    /**
     * Gets the pipe being dragged
     * @return pipe structure
     */
    public  Pipe GetLastDraggedPipe() {return dragging;}
    /**
     * The first touch
     */
    private Touch touch1 = new Touch();
    /**
     * The second touch
     */
    private Touch touch2 = new Touch();

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // playing area.
        //
        float relX = (event.getX() - marginX - scrollX) / gridSize / scale;
        float relY = (event.getY() - marginY - scrollY) / gridSize / scale;

        int id = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(view, event);
                touch1.copyToLast();
                if (dragging == null)
                    onTouched(relX, relY);
                lastRelX = relX;
                lastRelY = relY;
                view.invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(view, event);
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                if (dragging != null){
                    dragging.angleSnap();
                    dragging.positionSnap(width);
                }
//                onReleased();
                view.invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if(id == touch2.id) {
                    touch2.id = -1;
                } else if(id == touch1.id) {
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                if (dragging != null){
                    dragging.angleSnap();
                }
                view.invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(view, event);
                move(relX, relY);
                return true;
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
        ArrayList<Pipe> pieces = pipeArea.getPipe();

        // Check each piece to see if it has been hit
        // We do this in reverse order so we find the pieces in front
        for(int p=pieces.size()-1; p>=0;  p--) {
            if(pieces.get(p).hit(x, y, gridSize)) {
                // We hit a piece!
                dragging = pieces.get(p);
                pieces.remove(p);
                pieces.add(dragging);
                dragging.set(this, (int) width / 2, (int) height / 2);
                dragging.setPlayer(currentPlayer);
                lastRelX = x;
                lastRelY = y;
                return true;
            }
        }
        return false;
    }


    /**
     * Handle movement of the touches
     */
    private void move(float relX, float relY) {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        changeScale();
        changeScroll();

        if(touch1.id < 0 | dragging == null) {
            return;
        }

        if(touch1.id >= 0 && touch2.id < 0) {
            touch1.computeDeltas();
            // At least one touch
            // We are moving
            if (dragging.getX() + relX - lastRelX >= -0.5 && dragging.getX() + relX - lastRelX <= width - 0.5 && dragging.getY() + relY - lastRelY >= -0.5 && dragging.getY() + relY - lastRelY <= height - 0.5) {
                dragging.move(relX - lastRelX, relY - lastRelY);
                lastRelX = relX;
                lastRelY = relY;
            }
        }

        if(touch2.id >= 0) {
            /*
             * Rotation
             */
            float angle1 = angle(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);
            float angle2 = angle(touch1.x, touch1.y, touch2.x, touch2.y);
            float da = angle2 - angle1;
            rotate(da, touch1.x, touch1.y);
        }
    }

    /**
     * Rotate the image around the point x1, y1
     * @param dAngle Angle to rotate in degrees
     * @param x1 rotation point x
     * @param y1 rotation point y
     */
    public void rotate(float dAngle, float x1, float y1) {
        dragging.addRotateAngle(dAngle);
    }

    /**
     * Determine the angle for two touches
     * @param x1 Touch 1 x
     * @param y1 Touch 1 y
     * @param x2 Touch 2 x
     * @param y2 Touch 2 y
     * @return computed angle in degrees
     */
    private float angle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }
    /**
     * The int for the scroll X position
     */
    private int scrollX;
    /**
     * The int for the scroll Y position
     */
    private int scrollY;
    /**
     * Function to control the scrolling handling
     */
    private void changeScroll() {
        if (touch1.id >= 0 && touch2.id < 0 && dragging == null) {
            touch1.computeDeltas();
            scrollX += touch1.dX;
            scrollY += touch1.dY;
        }
    }
    /**
     * Function to control the scaling handling
     */
    private void changeScale() {
        if (dragging == null && touch1.id >= 0 && touch2.id >= 0) {
            float currDistance = (float)Math.sqrt(Math.pow((touch2.x - touch1.x), 2) + Math.pow((touch2.y - touch1.y), 2));
            float lastDistance = (float)Math.sqrt(Math.pow((touch2.lastX - touch1.lastX), 2) + Math.pow((touch2.lastY - touch1.lastY), 2));

            float scale = currDistance / lastDistance;
            this.scale *= scale;
        }
    }

    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(View view, MotionEvent event) {
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Convert to image coordinates
            float x = (event.getX(i) - marginX) / SCALE_IN_VIEW;
            float y = (event.getY(i) - marginY) / SCALE_IN_VIEW;

            if(id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch1.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }
        view.invalidate();
    }

    /**
     * Local class to handle the touch status for one touch.
     * We will have one object of this type for each of the
     * two possible touches.
     */
    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }

        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }
    }

    /**
     * Search to determine if this pipe has no leaks
     * @param start Starting pipe to search from
     * @return true if no leaks
     */
    public boolean search(Pipe start) {
        /*
         * Set the visited flags to false
         */
        for(Pipe[] row: gridpipes) {
            for(Pipe pipe : row) {
                if (pipe != null) {
                    pipe.setVisited(false);
                }
            }
        }

        /*
         * The pipe itself does the actual search
         */
        return start.search();
    }

    /**
     * The name of the bundle keys to save the puzzle
     */

    private final static String PIPEINGAME = "Pipe.ids";
    private final static String PIPEINGAMEANGLE = "Pipe.angle";
    private final static String PIPEINGAMECONNECT = "Pipe.connect";
    private final static String PIPEINGAMEPLAYER = "Pipe.player";

    private final static String DRAGGING = "Dragging.ids";
    private final static String DRAGGINGCONNECT = "Dragging.connect";

    private final static String TURN = "turn.info";
    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {

        int [][] pipes = new int[height][width];
        int [][] angle = new int[height][width];
        boolean [][] connection = new boolean[height * 4][width * 4];
        int [][] player = new int[height][width];

        for (int j = 0; j < height; j ++){
            for (int i = 0; i < width; i ++){
                if (gridpipes[i][j] != null) {
                    pipes[i][j] = gridpipes[i][j].getId();
                    angle[i][j] = (int)gridpipes[i][j].getRotateAngle();
                    gridpipes[i][j].setRotateAngle(0);
                    connection[i * 4][j * 4] = gridpipes[i][j].getConnect(0);
                    connection[i * 4 + 1][j * 4 + 1] = gridpipes[i][j].getConnect(1);
                    connection[i * 4 + 2][j * 4 + 2] = gridpipes[i][j].getConnect(2);
                    connection[i * 4 + 3][j * 4 + 3] = gridpipes[i][j].getConnect(3);
                    player[i][j] = gridpipes[i][j].getPlayer();
                }
                else
                    pipes[i][j] = -1;
            }
        }

        int [] dragging = new int[5];
        boolean [] draggingConnect = new boolean [20];

        for (int i = 0; i < 5; i ++){
            dragging[i] = pipeArea.getPipe().get(i).getId();
            draggingConnect[i * 4] = pipeArea.getPipe().get(i).getConnect(0);
            draggingConnect[i * 4 + 1] = pipeArea.getPipe().get(i).getConnect(1);
            draggingConnect[i * 4 + 2] = pipeArea.getPipe().get(i).getConnect(2);
            draggingConnect[i * 4 + 3] = pipeArea.getPipe().get(i).getConnect(3);
        }


        int turnRecord = currentPlayer;
        bundle.putInt(TURN, turnRecord);
        bundle.putSerializable(PIPEINGAME,  pipes);
        bundle.putSerializable(PIPEINGAMEANGLE,  angle);
        bundle.putSerializable(PIPEINGAMECONNECT,  connection);
        bundle.putSerializable(PIPEINGAMEPLAYER,  player);

        bundle.putIntArray(DRAGGING,  dragging);
        bundle.putSerializable(DRAGGINGCONNECT,  draggingConnect);
    }

    /**
     * Read the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle, Context context) {

        int turn = bundle.getInt(TURN);
        setTurn(turn);

        int[] dragging = bundle.getIntArray(DRAGGING);
        boolean[] draggingConnect = (boolean[]) bundle.getSerializable(DRAGGINGCONNECT);

        ArrayList<Pipe> newDragging = new ArrayList<Pipe>();

        for (int i = 0; i < 5; i++) {
            boolean north = draggingConnect[i * 4];
            boolean east = draggingConnect[i * 4 + 1];
            boolean south = draggingConnect[i * 4 + 2];
            boolean west = draggingConnect[i * 4 + 3];
            int id = dragging[i];

            Pipe piece = new Pipe(north, east, south, west, context, id);
            newDragging.add(piece);
        }

        pipeArea.setRandomPipe(newDragging);

        int[][] pipes = (int[][]) bundle.getSerializable(PIPEINGAME);
        int[][] angle = (int[][]) bundle.getSerializable(PIPEINGAMEANGLE);
        boolean[][] connection = (boolean[][]) bundle.getSerializable(PIPEINGAMECONNECT);
        int[][] players = (int[][]) bundle.getSerializable(PIPEINGAMEPLAYER);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int id = pipes[i][j];
                if (id != -1) {
                    boolean north = connection[i * 4][j * 4];
                    boolean east = connection[i * 4 + 1][j * 4 + 1];
                    boolean south = connection[i * 4 + 2][j * 4 + 2];
                    boolean west = connection[i * 4 + 3][j * 4 + 3];

                    int rotateAngle = angle[i][j];
                    int player = players[i][j];

                    Pipe piece = new Pipe(north, east, south, west, context, id);
                    piece.setRotateAngle(rotateAngle);
                    piece.setPlayer(player);
                    add(piece, i, j);
                }
            }
        }
    }


}