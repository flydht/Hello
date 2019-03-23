package com.example.trendchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.trendchart.model.BesselPoint;
import com.example.trendchart.model.BesselPointData;
import com.example.trendchart.model.BesselViewData;
import com.example.trendchart.util.DataUtils;
import com.example.trendchart.util.DensityUtils;
import com.example.trendchart.view.BesselCalculator;
import com.example.trendchart.view.DataCard;
import com.example.trendchart.view.DataCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    DataCardView mTempCardView;
    DataCardView mAqiCardView;
    private ArrayList<String> dateList = new ArrayList<>();
    private int mHighTestTemps[] = {16,14,20,22,24};
    private int mLowTestTemps[] = {3,1,4,5,2};
    private int mTestAqis[] = {50,60,40,200,250};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTempCardView = (DataCardView)findViewById(R.id.card_view_temp);
        mAqiCardView = (DataCardView)findViewById(R.id.card_view_aqi);
        dateList = initDate();
//        initCardView();

        //设置测试数据
        BesselViewData tempData = DataUtils.setTestTempData(this,5,mHighTestTemps,mLowTestTemps,dateList,5.5f);
        if(tempData !=null){
            mTempCardView.setData(tempData);
            BesselCalculator tempBesselCalculator = new BesselCalculator(tempData);
            tempBesselCalculator.compute();
            mTempCardView.refreshUI();
        }
        BesselViewData aqiData = DataUtils.setAqiData(this,5,mTestAqis,dateList,5.5f);
        if(aqiData !=null){
            mAqiCardView.setData(aqiData);
            BesselCalculator tempBesselCalculator = new BesselCalculator(aqiData);
            tempBesselCalculator.compute();
            mAqiCardView.refreshUI();
        }
    }

    private ArrayList<String> initDate(){
        ArrayList<String> dateList = new ArrayList<>();
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            for (int i = 0; i < 5; i++) {
                if (i == 0) {
                    calendar.add(calendar.DATE, 0);
                } else {
                    calendar.add(calendar.DATE, 1);
                }
                date = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("M/d");
                String dateString = formatter.format(date);
                dateList.add(dateString);
            }
            return dateList;
    }
}
