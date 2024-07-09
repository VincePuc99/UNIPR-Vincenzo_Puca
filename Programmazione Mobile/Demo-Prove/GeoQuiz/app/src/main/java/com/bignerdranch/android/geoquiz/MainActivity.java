package com.bignerdranch.android.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="AttivitàMainGeoQuiz";
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER="HaveCheated";
    private static final String KEY_SAVED_TOKENS="TokensSaved";
    private static int REQUEST_CODE_CHEAT;
    private TextView mCHEAT_TOKENS_DISPLAY;
    private Button mTrueButton;
    private int mCorrectAnswer=0;
    private boolean mIsCheater;
    private int mTokens=3;
    private TextView mDisplayText;
    private ImageButton mNextButton;
    private ImageButton mUndoButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Question[] mQuestionBank=new Question[]{
            new Question(R.string.question_australia,true,false,false),
            new Question(R.string.question_oceans,false,false,false),
            new Question(R.string.question_mideast,false,false,false),
            new Question(R.string.question_africa,true,false,false),
            new Question(R.string.question_americas,true,false,false),
            new Question(R.string.question_asia,false,false,false),};
    private int mCurrentIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate(Bundle) called");
        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater=savedInstanceState.getBoolean(KEY_IS_CHEATER,false);
            mQuestionBank[mCurrentIndex].setHavePressedCheat(mIsCheater);
            mTokens=savedInstanceState.getInt(KEY_SAVED_TOKENS,0);
        }

        mCHEAT_TOKENS_DISPLAY=(TextView) findViewById(R.id.Cheat_TOKENS);
        UpdateTokensDisplay();

        mCheatButton=(Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(v-> {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
            startActivityForResult(intent,REQUEST_CODE_CHEAT);
            mTokens--;
            UpdateTokensDisplay();
                });
        mDisplayText=(TextView) findViewById(R.id.DisplayQuestion);

        mTrueButton=(Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(v -> { checkAnswer(true);DisablePressedQuestion();FinalResult();});

        mFalseButton=(Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(v ->{ checkAnswer(false);DisablePressedQuestion();FinalResult();});

        mNextButton=(ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener( v-> {
            mCurrentIndex=(mCurrentIndex+1) % mQuestionBank.length;
            UpdateQuestion();
            mIsCheater=false;
        });
        UpdateQuestion();

        mUndoButton=(ImageButton) findViewById(R.id.undo_button);
        mUndoButton.setOnClickListener(v->{
            if (mCurrentIndex==0){
                mCurrentIndex=mQuestionBank.length-1;
            }
            else{
                mCurrentIndex=(mCurrentIndex-1) % mQuestionBank.length; }
            UpdateQuestion();
            mIsCheater=false;
        });
    }
    private void UpdateTokensDisplay(){
        mCHEAT_TOKENS_DISPLAY.setText("Remaining Tokens: "+ mTokens);
        if(mTokens==0){
            mCheatButton.setEnabled(false);
        }
    }
    private void DisablePressedQuestion(){
        if(mQuestionBank[mCurrentIndex].isPressed()){
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
        }else{
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
        }
    }
    private void FinalResult(){
        int checking_final=0;
        for(int i=0;i<mQuestionBank.length-1;i++){
            if(mQuestionBank[i].isPressed()){
                checking_final++;
            }
        }
        if(checking_final==(mQuestionBank.length-1)){
            int finalpercent=0;
            finalpercent=(100*mCorrectAnswer)/(mQuestionBank.length-1);
            Toast tostinofinale=Toast.makeText(MainActivity.this,"Corrette:"+Integer.toString(finalpercent)+"%",Toast.LENGTH_SHORT);
            tostinofinale.show();
        }
    }
    private void UpdateQuestion(){
        int question=mQuestionBank[mCurrentIndex].getTextResId();
        mDisplayText.setText(question);
        DisablePressedQuestion();
        FinalResult();
    }
    private void checkAnswer(boolean UserPressed){
        boolean OGReply=mQuestionBank[mCurrentIndex].isAnswerTrue();
        mQuestionBank[mCurrentIndex].setPressed(true);

        int Toasttext=0;
        if (mQuestionBank[mCurrentIndex].isHavePressedCheat()) {
            Toasttext = R.string.toast_giudicante;
        } else {

            if (OGReply == UserPressed) {
                Toasttext = R.string.correct_toast;
                mCorrectAnswer++;
            } else {
                Toasttext = R.string.incorrect_toast;
            }
        }
        Toast pressed = Toast.makeText(MainActivity.this,Toasttext,Toast.LENGTH_SHORT);
        pressed.setGravity(Gravity.TOP,0,0);
        pressed.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mQuestionBank[mCurrentIndex].setHavePressedCheat(mIsCheater);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart: Start, partito");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume: Resume,Partito");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putBoolean(KEY_IS_CHEATER,mIsCheater);
        savedInstanceState.putInt(KEY_SAVED_TOKENS, mTokens);
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop: Stop partito");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy: Destroy partito");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause: Pause,partito");
    }
}