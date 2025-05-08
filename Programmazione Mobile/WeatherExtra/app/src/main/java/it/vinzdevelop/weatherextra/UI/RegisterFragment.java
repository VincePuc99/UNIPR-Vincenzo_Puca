package it.vinzdevelop.weatherextra.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import it.vinzdevelop.weatherextra.MasterActivity;
import it.vinzdevelop.weatherextra.R;

public class RegisterFragment extends Fragment {
    ///// interface for passing data between fragment and activty
    private RegisterFragment.OnDataPass mVarForPassData;
    public interface OnDataPass {
        void onDataPass(String paramUsrNm,String paramPsw);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mVarForPassData = (RegisterFragment.OnDataPass) context;
    }

    public void passData(String paramUsrNm,String paramPsw) {
        mVarForPassData.onDataPass(paramPsw,paramUsrNm);
    }
    /////// end interface pass data to activity
private Button mBtnSendInfo;
    TextInputEditText mtietUsername,mtietPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.register_layout_fragment, container, false);
mBtnSendInfo=registerView.findViewById(R.id.btn_submitinfo);
mtietPassword=registerView.findViewById(R.id.tiet_password);
        mtietUsername=registerView.findViewById(R.id.tiet_username);

mBtnSendInfo.setOnClickListener(view -> {
mVarForPassData.onDataPass(String.valueOf(mtietUsername.getText()),String.valueOf(mtietPassword.getText()));

});


        return registerView;
    }
}