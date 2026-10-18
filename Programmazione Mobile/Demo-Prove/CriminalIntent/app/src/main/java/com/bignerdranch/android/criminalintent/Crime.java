package com.bignerdranch.android.criminalintent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequirePolice;
    private String mSuspect;

    public String getSuspect() {
        return mSuspect;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public boolean isRequirePolice() {
        return mRequirePolice;
    }

    public void setRequirePolice(boolean requirePolice) {
        mRequirePolice = requirePolice;
    }

    public Crime(){
      this(UUID.randomUUID());
    }
    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }


    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }
}
