package edu.msu.singhk12.steampunked;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


/**
 * Custom view class for our Puzzle.
 */
public class GameView extends View {

    /**
     * The PlayingArea
     */
    private PlayingArea game;
    /**
     * The current turn for the player
     */
    private int currentTurn = 1;
    /**
     * The actual size of the playing area
     */
    private int size;
    /**
     * The name for player1
     */
    private String player1;
    /**
     * The name for player2
     */
    private String player2;

    /**
     * Set Player1's name
     * @param name player1's name
     */
    public void setPlayer1(String name) {player1 = name; }
    /**
     * Set Player2's name
     * @param name player2's name
     */
    public void setPlayer2(String name) {player2 = name; }

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
    }
    /**
     * Get the current game
     * @return the current game
     */
    public PlayingArea getGame() {
        return game;
    }
    /**
     * Set the game's size
     * @param pos the size of the game
     */
    public void setSize(int pos) {
        game = null;
        switch (pos) {
            case 0:
                size = 5;
                break;

            case 1:
                size = 10;
                break;

            case 2:
                size = 20;
                break;
        }
        game = new PlayingArea(size, getContext());
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        invalidate();
    }

    /**
     * The overriden draw function for the GameView
     * @param canvas where the playing area will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        game.draw(canvas);
    }

    /**
     * A touch event for the GameView
     * @param event the touch
     * @return calls the playing area function
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.onTouchEvent(this, event);
    }
    /**
     * Changes the turn
     */
    public void changeTurn() {
        currentTurn *= -1;
        game.setTurn(currentTurn);
        invalidate();
    }
    /**
     * Get the current turn
     * @return the current turn
     */
    public int getTurn() {
        return currentTurn;
    }

    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        game.saveInstanceState(bundle);
    }

    /**
     * Load the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        game.loadInstanceState(bundle, getContext());
        currentTurn = game.getTurn();
    }


}