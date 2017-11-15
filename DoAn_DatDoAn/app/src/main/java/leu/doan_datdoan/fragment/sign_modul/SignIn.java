package leu.doan_datdoan.fragment.sign_modul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leu.doan_datdoan.R;
import leu.doan_datdoan.events.SignInEvent;
import leu.doan_datdoan.events.SignUpEvent;
import leu.doan_datdoan.fragment.FragmentLifecycle;


public class SignIn extends Fragment implements FragmentLifecycle{
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.tv_forgotPassword)
    TextView tvForgotPassword;

    private String username = "";
    private String password = "";

    @OnClick(R.id.btn_sign_in)
    public void onClickSignIn(){
        if(ktraDauVao()){
            EventBus.getDefault().post(new SignInEvent(username,password));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in,container,false);
        setupUI(v);

        return v;
    }


    private void setupUI(View v) {
        ButterKnife.bind(this,v);
    }

    private boolean ktraDauVao(){
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();


        boolean flag = true;
        if(username.equals("")){
            etUsername.setError("Cần điền thông tin");
            etUsername.setText("");
            flag = false;

        }

        if (password.equals("")) {
            etPassword.setError("Cần điền thông tin");
            etPassword.setText("");
            flag = false;
        }

        return flag;
    }

    @Override
    public void onResumeFragment() {
    }

    @Override
    public void onPauseFragment() {
    }

}
