package com.example.trendchart.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.trendchart.view.BesselTrendView;

public class DataCard extends RelativeLayout {

    protected BesselTrendView besselTrendView;
    public DataCard(Context context) {
        this(context, null);
    }

    public DataCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawBesselLine(boolean isDraw){

//        besselTrendView.setDrawBesselLine(isDraw);
    }

    public void refreshUI(){
        besselTrendView.invalidate();
    }
}
