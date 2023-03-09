package edu.msu.sharinic.examsharinic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context context;

    public ImageAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 16;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;


        if (convertView == null)
        {
            imageView = new ImageView((this.context));
            imageView.setLayoutParams(new GridView.LayoutParams(200,200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(R.drawable.darkgreensquare);
        return imageView;
    }


}


/**
 * References:
 * For ImageAdapter:
 * https://stuff.mit.edu/afs/sipb/project/android/docs/guide/topics/ui/layout/gridview.html
 */