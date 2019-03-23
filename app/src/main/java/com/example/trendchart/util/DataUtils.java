package com.example.trendchart.util;

import android.content.Context;
import android.graphics.Color;

import com.example.trendchart.model.BesselPoint;
import com.example.trendchart.model.BesselPointData;
import com.example.trendchart.model.BesselViewData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtils {

    private static int times;
    public static int[] getYAxisData(float min, float max, float average){
        times = 0;
        boolean isNegative = false;
        int[] result = new int[2];
        int maxTemp,minTemp,remainderMax,tenDigitMax,remainderMin,tenDigitMin;
        if(max <= 100){
            remainderMax = (int)max % 10;
            tenDigitMax = (int)max /10;
            if(max < 0)
                isNegative = true;
            if(Math.abs(remainderMax) < 5){
                if(isNegative){
                    maxTemp = tenDigitMax * 10;
                }else {
                    maxTemp = tenDigitMax * 10 +5;
                }
            }else{
                if(isNegative)
                    maxTemp = tenDigitMax * 10 -5;
                else
                    maxTemp = tenDigitMax * 10 + 10;
            }

            isNegative = false;

            if(min < 0)
                isNegative = true;
            remainderMin = (int)min % 10;
            tenDigitMin = (int)min / 10;
            if(Math.abs(remainderMin) < 5)
                if(isNegative){
                    minTemp = tenDigitMin * 10 -5;
                }
                else {
                    minTemp = tenDigitMin * 10;
                }
            else{
                if(isNegative)
                    minTemp = tenDigitMin * 10 -10;
                else
                    minTemp = tenDigitMin * 10 + 5;
            }

            getYAxisValue(minTemp,maxTemp,average,5,result);
        }else if(max > 100){
            remainderMax = (int)max % 10;
            tenDigitMax = (int)max /10;
            tenDigitMin = (int)min / 10;
            if(remainderMax == 0){
                maxTemp = tenDigitMax * 10;
            }else
                maxTemp =tenDigitMax * 10 +10;
            minTemp = tenDigitMin * 10;
            getYAxisValue(minTemp,maxTemp,average,10,result);
        }
        return result;
    }

    private static void getYAxisValue(int min, int max,float average,int space,int[] result){
        int spaceValue = max - min;
        if(spaceValue == 0){
            max += space;
            min -= space;
        }
        if(spaceValue % 4 == 0){
            result[0] = min;
            result[1] = max;
        }else {
            float maxTemp = (float) max;
            float minTemp = (float) min;
            float averageTemp = (maxTemp + minTemp)/2;
            if(average > averageTemp){
                max += space;
            }else{
                min -= space;
                if(min < 0){
                    min = min + space;
                    max += space;
                }
            }
            if(times <4){
                times++;
                getYAxisValue(min,max,average,space,result);
            }
        }
    }

    //适配线上的数值不会出届
    public static int[] getYAxisDataResultForTemp(int[] source, float max, float min) {
        int result[] = new int[2];
        int minTemp = source[0];
        int maxTemp = source[1];
        int tempStep;
        int tempLevel = (maxTemp - minTemp) / 4;
        if ((float) (maxTemp - max) / (float) tempLevel >= 0.5
                && (float) (min - minTemp) / (float) tempLevel >= 0.5) {
            result = source;
        } else {
            if(tempLevel == 5){
                tempStep = 5;
            }else
                tempStep = tempLevel / 2;
            maxTemp = maxTemp + 2 * tempStep;
            minTemp = minTemp - 2 * tempStep;
            result[0] = minTemp;
            result[1] = maxTemp;
        }
        return result;
    }

    public static int[] getYAxisDataResultForPm(int[] source,float max){
        int result[] = new int[2];
        int minTemp = source[0];
        int maxTemp = source[1];
        int tempStep;
        int tempLevel = (maxTemp - minTemp) /4;
        int minWithZero = minTemp;
        if((float)(maxTemp - max)/(float)tempLevel >= 0.5){
            result = source;
        }else {
            if(tempLevel == 5){
                tempStep = 5;
            }else
                tempStep = tempLevel / 2;
            minTemp = minTemp - 2 * tempStep;
            if(minTemp <= 0){
                minTemp = 0;
                maxTemp = maxTemp + minWithZero;
            }else {
                maxTemp = maxTemp + 2 * tempStep;
            }
            result[0] = minTemp;
            result[1] = maxTemp;

        }
        return result;
    }

    public static int getColor(float radio ,int startColor, int endColor) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255,red, greed, blue);
    }



    //填充温度数据（两条线）
    public static BesselViewData setTestTempData(Context context,int nums,int[] highTemps,int[] lowTemps,ArrayList<String> dates,float scaleValue) {
        BesselViewData resultData;

        int smallScale = DensityUtils.dp2px(context, scaleValue);
        int bigScale = DensityUtils.dp2px(context, scaleValue);

        List<BesselPoint> tempPoints1 = new ArrayList<>();
        List<BesselPoint> tempPoints2 = new ArrayList<>();
        List<BesselPoint> tempBesselPoints1 = new ArrayList<>();
        List<BesselPoint> tempBesselPoints2 = new ArrayList<>();
        List<BesselPointData> besselPointDatas = new ArrayList<>();
        float maxTempValue = 0, minTempValue = 10000, sumTempValue = 0, averageTempValue = 0;
        List highTestList = Arrays.stream(highTemps).boxed().collect(Collectors.toList());
        List lowTestList = Arrays.stream(lowTemps).boxed().collect(Collectors.toList());
//        if(tempdecForecast == null || tempdecForecast.size() == 0)
//            return;
        for (int i = 0; i < highTestList.size(); i++) {
            int fromValue = (int)highTestList.get(i);
            sumTempValue += fromValue;
            if (fromValue > maxTempValue) {
                maxTempValue = fromValue;
            }
            tempPoints1.add(new BesselPoint(i, fromValue, true, true));
            int toValue =(int)lowTestList.get(i);
            sumTempValue += toValue;
            if (toValue < minTempValue) {
                minTempValue = toValue;
            }
            tempPoints2.add(new BesselPoint(i, toValue, true, true));
        }
        averageTempValue = sumTempValue / (highTestList.size() + lowTestList.size());
        int[] resultTemp = getYAxisData(minTempValue, maxTempValue, averageTempValue);
        int[] changeresultTemp = getYAxisDataResultForTemp(resultTemp, maxTempValue, minTempValue);
        int minTemp = changeresultTemp[0];
        int maxTemp = changeresultTemp[1];
        BesselPointData besselPointData1 = new BesselPointData(tempPoints1, tempBesselPoints1);
        BesselPointData besselPointData2 = new BesselPointData(tempPoints2, tempBesselPoints2);
        besselPointData1.setPaintColor(0xFFF35018);
        besselPointData2.setPaintColor(0xFF00C7FF);
        besselPointData1.setDrawPointValue(true);
        besselPointData1.setDrawPointValueAbove(true);
        besselPointData2.setDrawPointValue(true);
        besselPointData2.setDrawPointValueAbove(false);
        besselPointDatas.add(besselPointData2);
        besselPointDatas.add(besselPointData1);
        resultData = new BesselViewData(context);
        resultData.setBesselPointDataList(besselPointDatas);
        resultData.setGradient(false);
        resultData.setX_axisScaleHeightSame(true);
        resultData.setmX_axisScaleHeightBig(bigScale);
        resultData.setmX_axisScaleHeightSmall(smallScale);
        resultData.setDataNum(nums);
        int tempLevel = (maxTemp - minTemp) / (nums -1);
        resultData.setLevelValue(tempLevel);
        resultData.setMaxValue(maxTemp);
        resultData.setStartValue(minTemp);
        resultData.setX_axisItemNum(nums);
        resultData.setmX_axisTextItemNum(nums);
        resultData.setX_axisTextList(dates);
        resultData.setX_axisTextWidthBig(false);
        resultData.setDrawPoint(true);

        return resultData;
    }

    public static BesselViewData setAqiData(Context context,int nums,int[] aqiDatas,ArrayList<String> dates,float scaleValue) {
        BesselViewData resultData;
        int smallScale = DensityUtils.dp2px(context, scaleValue);
        int bigScale = DensityUtils.dp2px(context, scaleValue);
        boolean isFlag = false;
        List<BesselPoint> aqiPoints = new ArrayList<>();
        List<BesselPoint> aqiBesselPoints = new ArrayList<>();
        List<BesselPointData> besselPointDatas = new ArrayList<>();
        float maxPmValue = 0, minPmValue = 10000, sumPmValue = 0, averagePmValue = 0;

        List aqiTestList = Arrays.stream(aqiDatas).boxed().collect(Collectors.toList());
        for (int i = 0; i < aqiTestList.size(); i++) {
            int value = (int) aqiTestList.get(i);
            aqiPoints.add(new BesselPoint(i,value,true,true));
            sumPmValue += value;
            if (value >= maxPmValue) {
                maxPmValue = value;
            }
            if (value <= minPmValue) {
                minPmValue = value;
            }
        }
        if (aqiTestList.size() > 0) {
            averagePmValue = sumPmValue / aqiTestList.size();
        }

        int[] resultPM = getYAxisData(minPmValue, maxPmValue, averagePmValue);
        int[] changerResultPM = getYAxisDataResultForPm(resultPM, maxPmValue);
        int minPM = changerResultPM[0];
        int maxPM = changerResultPM[1];
        int pmLevel = (maxPM - minPM) / (nums-1);
        BesselPointData besselPointData = new BesselPointData(aqiPoints, aqiBesselPoints);
        besselPointData.setDrawPointValue(true);
        besselPointData.setDrawPointValueAbove(true);
        besselPointDatas.add(besselPointData);
        resultData = new BesselViewData(context);
        resultData.setBesselPointDataList(besselPointDatas);
        int[] pmGradientColor = {0xFFDF1444, 0xFFF68E49, 0xFF3CEDF9};
//        pmData.setGradientColor(pmGradientColor);
        resultData.setGradient(true);
        resultData.setX_axisScaleHeightSame(true);
        resultData.setmX_axisScaleHeightBig(bigScale);
        resultData.setmX_axisScaleHeightSmall(smallScale);
        resultData.setDataNum(nums);
        resultData.setLevelValue(pmLevel);
        resultData.setMaxValue(maxPM);
        resultData.setStartValue(minPM);
        resultData.setX_axisItemNum(nums);
        resultData.setmX_axisTextItemNum(nums);
        resultData.setX_axisTextList(dates);
        resultData.setX_axisTextWidthBig(false);
        resultData.setDrawPoint(true);
        int[] range = {0, 500};

        float ratio;
        int startColorResult, endColorResult;
        int midColorRangeValue = (range[0] + range[1]) / 2;
        int[] gradientColorResult;
        if (minPM < midColorRangeValue) {
            ratio = (float) (midColorRangeValue - minPM) / (float) midColorRangeValue;
            endColorResult = getColor(ratio, pmGradientColor[1], pmGradientColor[2]);
            if (maxPM < midColorRangeValue) {
                ratio = (float) (midColorRangeValue - maxPM) / (float) midColorRangeValue;
                startColorResult = getColor(ratio, pmGradientColor[1], pmGradientColor[2]);
                gradientColorResult = new int[2];
                gradientColorResult[0] = startColorResult;
                gradientColorResult[1] = endColorResult;
            } else {
                ratio = (float) (range[1] - maxPM) / (float) midColorRangeValue;
                startColorResult = getColor(ratio, pmGradientColor[0], pmGradientColor[1]);
                gradientColorResult = new int[3];
                gradientColorResult[0] = startColorResult;
                gradientColorResult[1] = pmGradientColor[1];
                gradientColorResult[2] = endColorResult;
            }
        } else {
            ratio = (float) (range[1] - minPM) / (float) midColorRangeValue;
            endColorResult = getColor(ratio, pmGradientColor[0], pmGradientColor[1]);
            ratio = (float) (range[1] - maxPM) / (float) midColorRangeValue;
            startColorResult = getColor(ratio, pmGradientColor[0], pmGradientColor[1]);
            gradientColorResult = new int[2];
            gradientColorResult[0] = startColorResult;
            gradientColorResult[1] = endColorResult;
        }
        resultData.setGradientColor(gradientColorResult);
        return resultData;
    }
}
