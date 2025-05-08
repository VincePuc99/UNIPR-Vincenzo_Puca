package it.vinzdevelop.weatherextra.Data;

import android.graphics.Bitmap;

public class WeatherCity {
    private Bitmap mWeatherIcon;
    private String mRefreshDate,mCity,mCountry,mActualTemp,mMinTemp,mMaxTemp,mClouds,mRain,mWindDegree,
    mWindSpeed,mWindGuts,mDescription,mHumidity,mLat,mLong;

    public WeatherCity(){}

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLong() {
        return mLong;
    }

    public void setLong(String aLong) {
        mLong = aLong;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Bitmap getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(Bitmap weatherIcon) {
        mWeatherIcon = weatherIcon;
    }

    public String getRefreshDate() {
        return mRefreshDate;
    }

    public void setRefreshDate(String refreshDate) {
        mRefreshDate = refreshDate;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getActualTemp() {
        return mActualTemp;
    }

    public void setActualTemp(String actualTemp) {
        mActualTemp = actualTemp;
    }

    public String getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(String minTemp) {
        mMinTemp = minTemp;
    }

    public String getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        mMaxTemp = maxTemp;
    }

    public String getClouds() {
        return mClouds;
    }

    public void setClouds(String clouds) {
        mClouds = clouds;
    }

    public String getRain() {
        return mRain;
    }

    public void setRain(String rain) {
        mRain = rain;
    }

    public String getWindDegree() {
        return mWindDegree;
    }

    public void setWindDegree(String windDegree) {
        mWindDegree = windDegree;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        mWindSpeed = windSpeed;
    }

    public String getWindGuts() {
        return mWindGuts;
    }

    public void setWindGuts(String windGuts) {
        mWindGuts = windGuts;
    }
}
