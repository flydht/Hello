package com.example.trendchart.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.trendchart.model.BesselViewData;
import com.example.trendchart.R;

public class DataCardView extends DataCard {

    private TextView cardTitle;

    public DataCardView(Context context) {
        this(context, null);
    }

    public DataCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.data_card, this, true);
        besselTrendView = (BesselTrendView) findViewById(R.id.trend_view);
        cardTitle = (TextView) findViewById(R.id.indoor_view_title);
    }

    public void setTitle(String title) {
        this.cardTitle.setText(title);
    }

    public void setData(BesselViewData data) {
        besselTrendView.setData(data);
    }
}
