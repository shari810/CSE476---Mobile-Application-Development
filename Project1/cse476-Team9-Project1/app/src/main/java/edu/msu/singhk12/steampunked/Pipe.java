package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.method.Touch;
import android.util.Log;

/**
 * An example of how a pipe might be represented.
 */
public class Pipe {

    /**
     * Playing area this pipe is a member of
     */
    private PlayingArea playingArea = null;

    /**
     * Array that indicates which sides of this pipe
     * has flanges. The order is north, east, south, west.
     *
     * As an example, a T that has a horizontal pipe
     * with the T open to the bottom would be:
     *
     * false, true, true, true
     */
    private final boolean[] connect = {false, false, false, false};
    private final boolean[] originConnect = {false, false, false, false};

    public boolean getConnect(int i) {return connect[i]; }

    /**
     * X location in the playing area (index into array)
     */
    private float x = 0;

    /**
     * Y location in the playing area (index into array)
     */
    private float y = 0;
    /**
     * Set the X location in the playing area (index into array)
     * @param num the location X position
     */
    public void setX(float num) {x = num; }
    /**
     * Set the Y location in the playing area (index into array)
     * @param num the location Y position
     */
    public void setY(float num) {y = num; }
    /**
     * Get the X location in the playing area (index into array)
     * @return the location X position
     */
    public float getX() {return x; }
    /**
     * Get the Y location in the playing area (index into array)
     * @return the location Y position
     */
    public float getY() {return y; }

    /**
     * The image for the actual piece.
     */
    private final Bitmap piece;
    /**
     * Number id for the pipe
     */
    private final int id;
    /**
     * Get the number id for the pipe
     * @return id number
     */
    public int getId() {return id; }

    /**
     * Constructor
     * @param north True if connected north
     * @param east True if connected east
     * @param south True if connected south
     * @param west True if connected west
     */
    public Pipe(boolean north, boolean east, boolean south, boolean west, Context context, int id) {
        connect[0] = north;
        connect[1] = east;
        connect[2] = south;
        connect[3] = west;

        originConnect[0] = north;
        originConnect[1] = east;
        originConnect[2] = south;
        originConnect[3] = west;

        this.id = id;
        piece = BitmapFactory.decodeResource(context.getResources(), id);
    }

    /**
     * Find the neighbor of this pipe
     * @param d Index (north=0, east=1, south=2, west=3)
     * @return Pipe object or null if no neighbor
     */
    private Pipe neighbor(int d) {
        switch(d) {
            case 0:
                return playingArea.getPipe((int)x, (int)y-1);

            case 1:
                return playingArea.getPipe((int)x+1, (int)y);

            case 2:
                return playingArea.getPipe((int)x, (int)y+1);

            case 3:
                return playingArea.getPipe((int)x-1, (int)y);
        }
        return null;
    }

    /**
     * Get the playing area
     * @return Playing area object
     */
    public PlayingArea getPlayingArea() {
        return playingArea;
    }

    /**
     * Set the playing area and location for this pipe
     * @param playingArea Playing area we are a member of
     * @param x X index
     * @param y Y index
     */
    public void set(PlayingArea playingArea, float x, float y) {
        this.playingArea = playingArea;
        this.x = x;
        this.y = y;
    }
    /**
     * The rotation angle
     */
    private int rotateAngle = 0;
    /**
     * Get rotation angle for the pipe
     * @return rotation angle for the pipe
     */
    public int getRotateAngle() {
        return rotateAngle;
    }
    /**
     * Set rotation angle for the pipe
     * @param angle  rotation angle for the pipe
     */
    public void setRotateAngle(int angle) {
        rotateAngle = angle;
        connectUpdate();
    }
    /**
     * Add to the rotation angle for the pipe
     * @param dAngle  rotation angle for the pipe
     */
    public void addRotateAngle(float dAngle) {
        rotateAngle += dAngle;
        connectUpdate();
    }
    /**
     * Update the connections for the pipes
     */
    private void connectUpdate() {
        if (rotateAngle < 0){
            rotateAngle = rotateAngle % -360;
            rotateAngle += 360;
        }
        else
            rotateAngle = rotateAngle % 360;
        int i = (int) rotateAngle / 90;
        boolean first = true;
        if (i == 0) {
            connect[0] = originConnect[0];
            connect[1] = originConnect[1];
            connect[2] = originConnect[2];
            connect[3] = originConnect[3];
        }
        for (; i > 0; i --) {
            if (first){
                connect[0] = originConnect[3];
                connect[1] = originConnect[0];
                connect[2] = originConnect[1];
                connect[3] = originConnect[2];
                first = false;
            }
            else {
                boolean temp = connect[3];
                connect[3] = connect[2];
                connect[2] = connect[1];
                connect[1] = connect[0];
                connect[0] = temp;
            }
        }
    }

    /**
     * Draw the puzzle piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param gridSize Size we draw one puzzle in pixels
     */
    public void draw(Canvas canvas, int marginX, int marginY,
                     float gridSize) {
        int wid = piece.getWidth();
        int hit = piece.getHeight();

        int maxDim = wid < hit ? wid : hit;
        float scaleFactor = gridSize / maxDim;

        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * gridSize  + gridSize / 2, marginY + y * gridSize + gridSize / 2);

        // Scale it to the right size
        canvas.scale(scaleFactor, scaleFactor);

        canvas.rotate(rotateAngle);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f , -piece.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, null);

        canvas.restore();
    }

    /**
     * Test to see if we have touched a puzzle piece
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY,
                       float gridSize) {

        int wid = piece.getWidth();
        int hit = piece.getHeight();

        int maxDim = wid < hit ? wid : hit;
        float scaleFactor = gridSize / maxDim;

        // Make relative to the location and size to the piece size
        int pX = (int)(((testX - x) * gridSize - gridSize / 2) / scaleFactor) +
                piece.getWidth() / 2;
        int pY = (int)(((testY - y) * gridSize - gridSize / 2) / scaleFactor) +
                piece.getWidth() / 2;

        if(pX < 0 || pX >= piece.getWidth() ||
                pY < 0 || pY >= piece.getHeight()) {
            return false;
        }
        return true;
    }
    /**
     * The current player
     */
    private int player = 0;
    /**
     * Set the current player
     * @param playerNum the player number
     */
    public void setPlayer(int playerNum) {player = playerNum; }
    /**
     * Get the current player
     * @return the player number
     */
    public int getPlayer() {return player;}


    /**
     * We consider a piece to be in the right location if within
     * this angle distance
     */
    final static int SNAP_ANGLE = 44;
    /**
     * The array for the final angles in degrees
     */
    final static int[] FINAL_ANGLE = {0, 90, 180, 270, 360};
    /**
     * Check if at the angle can snap to another pipe
     */
    public void angleSnap() {
        if (rotateAngle < 0){
            rotateAngle = rotateAngle % -360;
            rotateAngle += 360;
        }
        else
            rotateAngle = rotateAngle % 360;
        for (int angle : FINAL_ANGLE){
            if (Math.abs(rotateAngle - angle) < SNAP_ANGLE) {
                rotateAngle = angle;
            }
        }
        connectUpdate();
    }
    /**
     * We consider a piece to be in the right location if within
     * this distance.
     */
    final static float SNAP_DISTANCE = 0.45f;
    /**
     * Check if the pipe can snap to another pipe
     */
    public boolean positionSnap(int size) {
        for (int i = 0; i < size + 1; i ++){
            for (int j = 0; j < size + 1; j ++){
                if ((i - SNAP_DISTANCE) < (x) && (x) < (i + SNAP_DISTANCE) && (j - SNAP_DISTANCE) < (y) && (y) < (j + SNAP_DISTANCE)){
                    for (int index = 0; index < 4; index ++){
                        if (connect[index]){
                            float originX = x;
                            float originY = y;
                            x = i;
                            y = j;
//                            if (playingArea.getPipe(i, j) == null) {
                                Pipe connection = neighbor(index);
                                if (connection != null && player == connection.getPlayer()){
                                    if (connection.getConnect((index + 2) % 4)){
                                        SetSnapped(true);
                                        return true;
                                    }
                                }
//                            }
                            x = originX;
                            y = originY;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * True if the pipe is snapped into position
     */
    private boolean isSnapped = false;

    /**
     * Setter for isSnapped
     */
    public void SetSnapped(boolean snap) {isSnapped = snap;}

    /**
     * Getter for isSnapped
     */
    public boolean GetSnapped(){
        if (isSnapped)
            return true;
        if (Math.abs(x - (int)x) < 0.3f && Math.abs(y - (int)y) < 0.3f) {
            for (int index = 0; index < 4; index ++) {
                if (connect[index]) {
                    float originX = x;
                    float originY = y;
                    x = (int) x;
                    y = (int) y;
                    Pipe connection = neighbor(index);
                    if (connection != null && player == connection.getPlayer()) {
                        if (!connection.getConnect((index + 2) % 4)) {
                            return false;
                        }
                    }
                    x = originX;
                    y = originY;
                }
            }
            SetSnapped(true);
            return true;
        }
        return false;
    }

    /**
     * Set true upon pipe being installed.
     * Determines if the pipe can be moved.
     */
    private boolean isInstalled = false;

    /**
     * Setter for isInstalled
     * Only set to true upon valid InstallButton Click
     */
    public void SetInstalled(){isInstalled = true;}

    /**
     * Move the puzzle piece by dx, dy
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        if (!isInstalled){ //cannot move installed pieces
            x += dx;
            y += dy;
            SetSnapped(false); //false while moving
        }
    }

    /**
     * Depth-first visited visited
     */
    private boolean visited = false;

    /**
     * Has this pipe been visited by a search?
     * @return True if yes
     */
    public boolean beenVisited() {
        return visited;
    }

    /**
     * Set the visited flag for this pipe
     * @param visited Value to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    /**
     * Get the neighbor for the pipe in the X position
     * @return the neighbor
     */
    public float getNeighborX(float currentX, int dir){
        if (dir == 1){
            return currentX + 1;
        }
        if (dir == 3){
            return currentX - 1;
        }
        return currentX;
    }
    /**
     * Get the neighbor for the pipe in the Y position
     * @return the neighbor
     */
    public float getNeighborY(float currentY, int dir){
        if (dir == 0){
            return currentY - 1;
        }
        if (dir == 2){
            return currentY + 1;
        }
        return currentY;
    }

    /**
     * Search to see if there are any downstream of this pipe
     *
     * This does a simple depth-first search to find any connections
     * that are not, in turn, connected to another pipe. It also
     * set the visited flag in all pipes it does visit, so you can
     * tell if a pipe is reachable from this pipe by checking that flag.
     * @return True if no leaks in the pipe
     */
    public boolean search() {
        visited = true;

        for(int d=0; d<4; d++) {
            /*
             * If no connection this direction, ignore
             */
            if(!connect[d]) {
                continue;
            }

            // What is the matching location on
            // the other pipe. For example, if
            // we are looking in direction 1 (east),
            // the other pipe must have a connection
            // in direction 3 (west)
            int dp = (d + 2) % 4;

            Pipe n = neighbor(d);
            if(n == null) {
                // We leak
                // We have a connection with nothing on the other side

                for(int i = 0; i < 4; i++){
                    if (neighbor(i) == null && connect[i]){
                        getPlayingArea().addLeak(getNeighborX(getX(), i), getNeighborY(getY(), i), i);
                    }
                }

                return false;
            }

            if(!n.connect[dp]) {
                // We have a bad connection, the other side is not
                // a flange to connect to

                for(int i = 0; i < 4; i++){
                    if (neighbor(i) == null && connect[i]){
                        getPlayingArea().addLeak(getNeighborX(getX(), i), getNeighborY(getY(), i), i);
                    }
                }

                return false;
            }

            if(n.visited) {
                // Already visited this one, so no leaks this way
                continue;
            } else {
                // Is there a lead in that direction
                if(!n.search()) {
                    // We found a leak downstream of this pipe
                    for(int i = 0; i < 4; i++){
                        if (neighbor(i) == null && connect[i]){
                            getPlayingArea().addLeak(getNeighborX(getX(), i), getNeighborY(getY(), i), i);
                        }
                    }
                    return false;
                }
            }
        }
        // Yah, no leaks
        return true;
    }

}




/**  test **/