package it.vincenzopuca.mycurrency.Data;

public class ConvertedResponse {
    private String mSuccess,mFrom,mTo,mAmount,mTimeStamp,mRate,mDate,mResult;
    public ConvertedResponse(String success,String from, String to,String amount,String timestamp,
                             String rate,String date,String result)
    {
        mSuccess=success; mFrom=from;mTo=to;mAmount=amount;mTimeStamp=timestamp;mRate=rate;mDate=date;mResult=result;
    }

    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmTo() {
        return mTo;
    }

    public void setmTo(String mTo) {
        this.mTo = mTo;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getmRate() {
        return mRate;
    }

    public void setmRate(String mRate) {
        this.mRate = mRate;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }
}
