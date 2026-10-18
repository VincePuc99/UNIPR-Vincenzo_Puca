package it.vinzdevelop.weatherextra.UI;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.vinzdevelop.weatherextra.R;

public class LoginFragment extends Fragment {
    ///// interface for passing data between fragment and activty
    private OnDataPass mVarForPassData;
    public interface OnDataPass {
        void onDataPass(String paramUsrNm,String paramPsw,boolean register);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mVarForPassData = (OnDataPass) context;
    }

    public void passData(String paramUsrNm,String paramPsw,boolean register) {
        mVarForPassData.onDataPass(paramPsw,paramUsrNm,register);
    }
    /////// end interface pass data to activity
    private TextInputLayout mTxtInLayUsrn,mTxtInLayPsw;
    private ConstraintLayout mCL;
    private AnimationDrawable mAD;
    private Button mLoginBtn,mRegisterBtn;
    private TextInputEditText mInputUsername,mInputPassword;
    private static boolean sLoginResultToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView= inflater.inflate(R.layout.login_layout_fragment, container, false);

        //inizialize ViewsID
        FindID(loginView);

        //Hide actionbar for full screen view
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);

        //strat background animation
        mAD= (AnimationDrawable) mCL.getBackground();
        mAD.setEnterFadeDuration(10);
        mAD.setExitFadeDuration(5000);
        mAD.start();

        //View Logic
        mLoginBtn.setOnClickListener(view -> {
            String usrName = String.valueOf(mInputUsername.getText());
            String psw = String.valueOf(mInputPassword.getText());

            if(usrName.isEmpty() || psw.isEmpty()){
                ErrorField();
            }else {
                mVarForPassData.onDataPass(usrName, psw,false);
                if (!sLoginResultToken) {
                    ErrorField();
                }else{
                    Handler handler = new Handler();
                    handler.postDelayed(this::CorrectField, 1000);
                }
            }
        });
        mRegisterBtn.setOnClickListener(view -> {
            mVarForPassData.onDataPass("","",true);
        });
        return loginView;
    }

    public static void CheckLoginResultToken(boolean resultToken){
        sLoginResultToken = resultToken;
    }
    private void FindID(View v){
        sLoginResultToken =false;
        mCL= v.findViewById(R.id.constraint_login_background);
        mLoginBtn=v.findViewById(R.id.btn_login);
        mRegisterBtn=v.findViewById(R.id.btn_register);
        mInputUsername=v.findViewById(R.id.ti_username);
        mInputPassword=v.findViewById(R.id.ti_password);
        mTxtInLayPsw = v.findViewById(R.id.txtinlay_password);
        mTxtInLayUsrn= v.findViewById(R.id.txtinlay_username);
    }
    private void ErrorField(){
            Toast.makeText(getContext(), "Ricontrolla i campi", Toast.LENGTH_SHORT).show();
            mTxtInLayUsrn.setDefaultHintTextColor(ColorStateList.valueOf(Color.RED));
            mTxtInLayPsw.setDefaultHintTextColor(ColorStateList.valueOf(Color.RED));
        mInputPassword.setActivated(false);
        mInputUsername.setActivated(false);////////////////
    }
    private void CorrectField(){
        mInputPassword.setText("");
        mInputUsername.setText("");
        mTxtInLayUsrn.setDefaultHintTextColor(ColorStateList.valueOf(Color.WHITE));
        mTxtInLayPsw.setDefaultHintTextColor(ColorStateList.valueOf(Color.WHITE));
        mInputPassword.setActivated(false);
        mInputUsername.setActivated(false);
    }

}//Fragment Class ending