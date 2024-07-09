package com.bignerdranch.android.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    public static final String TAG="AttivitàCheat";
    private static final String KEY_INDEX = "saving_prssdcheat";
    private TextView mAPI_Display;
    private boolean mHavePressed=false;
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    public static final String EXTRA_ANSWER_SHOWN="com.bignerdranch.android.geoquiz.answer_shown";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";

public static Intent newIntent(Context packageContext, boolean answerIsTrue){
    Intent intent = new Intent(packageContext, CheatActivity.class);
    intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
    return  intent;
}
public static boolean wasAnswerShown(Intent result){
    return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //riprendi i dati se salvati
        setContentView(R.layout.activity_cheat); //crea layout da xml

        if(savedInstanceState!=null){
            mHavePressed=savedInstanceState.getBoolean(KEY_INDEX,false);
            if(mHavePressed){setAnswerShownResult(true);}
        }

        mAPI_Display=(TextView) findViewById(R.id.api_display);
        int api_int = Build.VERSION.SDK_INT;
        mAPI_Display.setText("API Level "+Integer.toString(api_int));

        mAnswerIsTrue=getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(v -> {
            if(mAnswerIsTrue){
                mAnswerTextView.setText(R.string.true_button);
            }else{ mAnswerTextView.setText(R.string.false_button);}
            setAnswerShownResult(true);
        });
    }

    private void setAnswerShownResult(boolean isShowed){
    Intent data=new Intent();
    data.putExtra(EXTRA_ANSWER_SHOWN,isShowed);
    setResult(RESULT_OK,data);
    mHavePressed=true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putBoolean(KEY_INDEX,mHavePressed);
    }


}