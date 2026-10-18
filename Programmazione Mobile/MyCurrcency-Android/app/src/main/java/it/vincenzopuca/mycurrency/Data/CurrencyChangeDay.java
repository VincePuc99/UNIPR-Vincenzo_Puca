package it.vincenzopuca.mycurrency.Data;

public class CurrencyChangeDay {
    private String mStartCurrency, mEndCurrenct, mRate, mSuccess,mDate;

    public CurrencyChangeDay(String mStartCurrency, String mEndCurrenct, String mRate, String mSuccess, String mDate) {
        this.mStartCurrency = mStartCurrency;
        this.mEndCurrenct = mEndCurrenct;
        this.mRate = mRate;
        this.mSuccess = mSuccess;
        this.mDate = mDate;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }


    public String getmStartCurrency() {
        return mStartCurrency;
    }

    public void setmStartCurrency(String mStartCurrency) {
        this.mStartCurrency = mStartCurrency;
    }

    public String getmEndCurrenct() {
        return mEndCurrenct;
    }

    public void setmEndCurrenct(String mEndCurrenct) {
        this.mEndCurrenct = mEndCurrenct;
    }

    public String getmRate() {
        return mRate;
    }

    public void setmRate(String mRate) {
        this.mRate = mRate;
    }
}
