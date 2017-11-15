package leu.doan_datdoan.fragment.sign_modul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import leu.doan_datdoan.R;
import leu.doan_datdoan.events.SignInEvent;
import leu.doan_datdoan.events.SignUpEvent;
import leu.doan_datdoan.fragment.FragmentLifecycle;


public class SignUp extends Fragment implements FragmentLifecycle {

    public static final String LOAIKHACHHANG = "khachhang";
    public static final String LOAICUAHANG = "cuahang";
    public static final String LOAINGUOIVANCHUYEN = "nguoivanchuyen";

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_sign_up)
    Button btnSignup;
    @BindView(R.id.radigroup_signup)
    RadioRealButtonGroup radioRealButtonGroup;

    private String username = "";
    private String password = "";
    private int loai = -1;

    public SignUp() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_sign_up)
    public void onClickSignUp(){
        if(ktraDauVao()){
            EventBus.getDefault().post(new SignUpEvent(username,password,layLoaiNguoiDung(loai)));
        }
    }

    private String layLoaiNguoiDung(int pos){
        switch (pos){
            case(0):{
                return LOAICUAHANG;
            }
            case(1):{
                return LOAIKHACHHANG;
            }
            case(2):{
                return LOAINGUOIVANCHUYEN;
            }
        }
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        setupUI(v);

        radioRealButtonGroup.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                loai = currentPosition;
            }
        });

        return v;
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

        if(loai == -1){
            flag = false;
        }

        if (password.equals("")) {
            etPassword.setError("Cần điền thông tin");
            etPassword.setText("");
            flag = false;
        }

        return flag;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this,v);

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }
}
