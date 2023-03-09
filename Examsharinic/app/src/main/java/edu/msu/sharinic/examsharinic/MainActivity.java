package edu.msu.sharinic.examsharinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String IDS = "MatchGrid.ids";

    GridView matchGrid;

    //GridView backupGrid = null;

    ImageView pieceView = null;

    private boolean isPeak = false;

    //private int pairCnt = 0;

    int [] positions = {0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};
    int curPos = -1;

    List<Integer> pairlist = new ArrayList<Integer>();


    // references to my images
    private Integer[] mIds = {
            R.drawable.chess_bdt45, R.drawable.chess_kdt45,
            R.drawable.chess_ndt45, R.drawable.chess_pdt45,
            R.drawable.chess_qdt45, R.drawable.chess_rdt45,
            R.drawable.sparty, R.drawable.helmet,
            R.drawable.chess_bdt45, R.drawable.chess_kdt45,
            R.drawable.chess_ndt45, R.drawable.chess_pdt45,
            R.drawable.chess_qdt45, R.drawable.chess_rdt45,
            R.drawable.sparty, R.drawable.helmet,
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);


        matchGrid = (GridView) findViewById(R.id.matchGrid);

        matchGrid.setAdapter(new ImageAdapter(this));


        matchGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                if(curPos < 0 ){
                    curPos = position;
                    pieceView = (ImageView) v;
                    ((ImageView) v).setImageResource(mIds[positions[position]]);

                }

                else{
                    if(curPos == position){
                        //((ImageView) v).setImageResource(R.drawable.darkgreensquare);
                        Toast.makeText(getApplicationContext(),"Same tile clicked",Toast.LENGTH_SHORT).show();
                    }

                    else if (positions[curPos] != positions[position])
                    {
                        ((ImageView) v).setImageResource(mIds[positions[position]]);
                        Toast.makeText(getApplicationContext(),"not matched",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //((ImageView) v).setImageResource(mIds[positions[position]]);
                        Toast.makeText(getApplicationContext(),"matched",Toast.LENGTH_SHORT).show();
                        ((ImageView) v).setImageResource(R.drawable.lightgreensquare);
                        pieceView.setImageResource(R.drawable.lightgreensquare);
                        if (!pairlist.contains(position) && !pairlist.contains(curPos)){
                            pairlist.add(position);
                            pairlist.add(curPos);
                        }

                        if(pairlist.size() == 16)
                        {
                            Toast.makeText(getApplicationContext(),"winner",Toast.LENGTH_SHORT).show();
                        }


                    }
                curPos = -1;
                }

            }
        });
    }

    /**
     * "Peek" button should display the images behind all the tiles.
     * Touching the button should flip all the tiles again.
     * @param view
     */
    public void Peek(View view){

        if (!isPeak) {
            //backupGrid = matchGrid;
            for (int i = 0; i < 16; i++) {

                ImageView curview = (ImageView) matchGrid.getChildAt(i);
                //int q = curview.getId();
                curview.setImageResource(mIds[positions[i]]);

            }
            isPeak = true;
        }
        else{
            //matchGrid = backupGrid;
            for (int i = 0; i < 16; i++) {

                ImageView curview = (ImageView) matchGrid.getChildAt(i);
                curview.setImageResource(R.drawable.darkgreensquare);

            }
            isPeak = false;
        }
    }

    public void Reset(View view){
        for (int i = 0; i < 16; i++) {

            ImageView curview = (ImageView) matchGrid.getChildAt(i);
            curview.setImageResource(R.drawable.darkgreensquare);

        }

        pairlist.clear();
        ImageView pieceView = null;

        isPeak = false;

    }


    /**
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        final ArrayList<Integer> ids = new ArrayList<>();

        for (int i = 0; i < 16; i++) {

            ImageView curview = (ImageView) matchGrid.getChildAt(i);
            //int q = curview.;
            ids.add( matchGrid.getChildAt(i).getId() );

        }
        outState.putIntegerArrayList(IDS, ids);

    }
    */
/**
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final ArrayList<Integer> ids = savedInstanceState.getIntegerArrayList(IDS);

        for (int i = 0; i < 16; i++) {

            ImageView curview = (ImageView) matchGrid.getChildAt(i);
            curview.setImageResource(ids.get(i));

        }

    }
*/
}




/**
 * References
 * 1. https://developer.android.com/reference/android/view/ViewGroup#getChildAt%28int%29
 * 2. https://stackoverflow.com/questions/6583053/gridview-get-a-view-by-position
 */