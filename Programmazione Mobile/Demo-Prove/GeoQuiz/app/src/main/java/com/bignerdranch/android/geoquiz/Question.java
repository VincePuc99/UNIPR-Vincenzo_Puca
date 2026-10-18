package com.bignerdranch.android.geoquiz;
public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mHavePressedCheat;

    public boolean isHavePressedCheat() {
        return mHavePressedCheat;
    }

    public void setHavePressedCheat(boolean havePressedCheat) {
        mHavePressedCheat = havePressedCheat;
    }

    private boolean mPressed;

    public Question(int textResId, boolean answerTrue,boolean pressed,boolean hvprsscht){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
        mPressed=pressed;
        mHavePressedCheat=hvprsscht;
    }

    public boolean isPressed() {
        return mPressed;
    }

    public void setPressed(boolean pressed) {
        mPressed = pressed;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
