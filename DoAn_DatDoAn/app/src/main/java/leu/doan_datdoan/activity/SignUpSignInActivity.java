package leu.doan_datdoan.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import leu.doan_datdoan.R;
import leu.doan_datdoan.adapters.ViewPagerAdapterForSign;
import leu.doan_datdoan.anim.ZoomOutPageTransformer;
import leu.doan_datdoan.database.RealmHandler;
import leu.doan_datdoan.database.TokenFirebaseContainer;
import leu.doan_datdoan.events.SignInEvent;
import leu.doan_datdoan.events.SignUpEvent;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.fragment.sign_modul.SignIn;
import leu.doan_datdoan.fragment.sign_modul.SignUp;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.model.TaiKhoan;
import leu.doan_datdoan.network.Message;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.taikhoan.TaiKhoanService;
import leu.doan_datdoan.utils.TypefaceFactory;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpSignInActivity extends AppCompatActivity{


    private final String KEY_ACTIVITY = "signinsignupactivity";

    @BindView(R.id.iv_AppPic)
    ImageView ivAppPic;
    @BindView(R.id.iv_UserPic)
    ImageView ivUserPic;
    @BindView(R.id.tabl_main)
    TabLayout tablayoutMain;
    @BindView(R.id.vp_main)
    ViewPager vpMain;

    private PinEntryEditText edPin;
    private TextView btnMoGuiMa;
    private Button btnKiemtra;

    private AVLoadingIndicatorView rotateLoading;

    private LinearGradient linearGradient;
    private LinearGradient linearGradient1;
    private ArrayList<TextView> textViewArrayList;

    private ViewPagerAdapterForSign viewPagerAdapter;

    private EditText edGuiMa;
    private TextView btnGuiMa;

    private Dialog dialog;
    private Dialog pinDialog;
    private Dialog guiDialog;

    private TaiKhoan taiKhoan;
    private String loai = "";
    private boolean dangTao;
    private String username;
    String token = null;

    public SignUpSignInActivity() {
        int[] color = {Color.parseColor("#FF2140F0"), Color.parseColor("#86292993")};
        float[] position = {0, 1};
        Shader.TileMode tile_mode = Shader.TileMode.MIRROR;
        linearGradient = new LinearGradient(0, 50, 0, 0, color, position, tile_mode);

        int[] color1 = {Color.GRAY, Color.BLACK};
        linearGradient1 = new LinearGradient(0, 50, 0, 0, color1, position, tile_mode);

        textViewArrayList = new ArrayList<>();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        dialog.setContentView(R.layout.dialog_chuyen);
//        rotateLoading = (AVLoadingIndicatorView) dialog.findViewById(R.id.rotatechuyen);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//
//
//        View v = LayoutInflater.from(this).inflate(R.layout.dialog_pincode,null,false);
//
//        edPin = (PinEntryEditText) v.findViewById(R.id.edpin_pincodedialog);
//        btnMoGuiMa = (TextView) v.findViewById(R.id.btndialogguima_pincodedialog);
//        btnKiemtra = (Button) v.findViewById(R.id.btnkiemtra_pincodedialog);
//
//        pinDialog.setContentView(v);
//        pinDialog.setCancelable(false);
//        pinDialog.setCanceledOnTouchOutside(false);
//
//
//        View v1 = LayoutInflater.from(this).inflate(R.layout.dialog_guicode,null,false);
//
//        btnGuiMa = (TextView) v1.findViewById(R.id.btn_dialogguicode);
//        edGuiMa = (EditText) v1.findViewById(R.id.ed_dialogguicode);
//
//        guiDialog.setContentView(v1);
//        guiDialog.setCancelable(false);
//        guiDialog.setCanceledOnTouchOutside(false);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_sign_up_sign_in);


        setupUI();
        setupEvent();
        EventBus.getDefault().register(this);
    }

    private void setupEvent() {
        tablayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                textViewArrayList.get(tab.getPosition()).getPaint().setShader((Shader) linearGradient);
                textViewArrayList.get(tab.getPosition()).invalidate();
                FragmentLifecycle lifecycle2 = (FragmentLifecycle) viewPagerAdapter.getItem(tab.getPosition());
                lifecycle2.onResumeFragment();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                FragmentLifecycle lifecycle1 = (FragmentLifecycle) viewPagerAdapter.getItem(tab.getPosition());
                lifecycle1.onPauseFragment();
                textViewArrayList.get(tab.getPosition()).getPaint().setShader((Shader) linearGradient1);
                textViewArrayList.get(tab.getPosition()).invalidate();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnGuiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiDialog.dismiss();
                RetrofitFactory.getInstance().createService(TaiKhoanService.class).guiSmsXacNhan(taiKhoan.getId(),edGuiMa.getText().toString()).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {

                        if(response.isSuccessful()){
                           pinDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {

                    }
                });
            }
        });

        btnKiemtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitFactory.getInstance().createService(TaiKhoanService.class).xacNhanMa(taiKhoan.getId(),edPin.getText().toString()).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()){
                            Message m = response.body();
                            if(m.getMessage().equals("Thanh cong")){
                                pinDialog.dismiss();
                                dangNhap();

                            }else{
                                showMess("Mã sai!!!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {

                    }
                });
            }
        });

        btnMoGuiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinDialog.dismiss();
                showGuiMa();
            }
        });
    }

    private void dangNhap() {

        switch (loai){
            case ("cuahang"):{
                dismissDialog();

                Intent i = new Intent(this,CuaHangActivity.class);
                i.putExtra(CuaHangActivity.KEY_BUNDLE_DATAOCUAHANG,dangTao);
                i.putExtra(CuaHangActivity.KEY_BUNDLE_IDTAIKHOAN,taiKhoan.getId());
                startActivity(i);

                break;
            }
            case("khachhang"):{
                dismissDialog();

                Intent i = new Intent(this,KhachHangActivity.class);
                i.putExtra(KhachHangActivity.KEY_BUNDLE_IDTAIKHOAN,taiKhoan.getId());
                i.putExtra(KhachHangActivity.KEY_BUNDLE_DATAOKHACHHANG,dangTao);
                i.putExtra(KhachHangActivity.KEY_BUNDLE_USERNAME,username);
                startActivity(i);

                break;
            }
            case ("nguoivanchuyen"):{
                dismissDialog();

                Log.d("abcd",""+taiKhoan.getId());
                Intent i = new Intent(this,NguoiVanChuyenActivity.class);
                i.putExtra(NguoiVanChuyenActivity.KEY_BUNDLE_IDTAIKHOAN,taiKhoan.getId());
                i.putExtra(NguoiVanChuyenActivity.KEY_BUNDLE_DATAONVC,dangTao);
                startActivity(i);

                break;
            }
        }

    }

    private void setupUI() {
        ButterKnife.bind(this);
        setupViewPager();
        setupTablayout();

        ViewGroup vg = (ViewGroup) tablayoutMain.getChildAt(0);
        ViewGroup tabSelected = (ViewGroup) vg.getChildAt(0);
        ViewGroup tabUnselected = (ViewGroup) vg.getChildAt(1);

        for (int i = 0; i < tabSelected.getChildCount(); i++) {
            View tvSelected = tabSelected.getChildAt(i);
            if (tvSelected instanceof TextView) {
                textViewArrayList.add((TextView) tvSelected);
            }
        }

        for (int i = 0; i < tabUnselected.getChildCount(); i++) {
            View tvUnselected = tabUnselected.getChildAt(i);
            if (tvUnselected instanceof TextView) {
                textViewArrayList.add((TextView) tvUnselected);
            }
        }

        textViewArrayList.get(0).getPaint().setShader(linearGradient);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_chuyen);
        rotateLoading = (AVLoadingIndicatorView) dialog.findViewById(R.id.rotatechuyen);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        pinDialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_pincode,null,false);

        edPin = (PinEntryEditText) v.findViewById(R.id.edpin_pincodedialog);
        btnMoGuiMa = (TextView) v.findViewById(R.id.btndialogguima_pincodedialog);
        btnKiemtra = (Button) v.findViewById(R.id.btnkiemtra_pincodedialog);

        pinDialog.setContentView(v);
        pinDialog.setCancelable(false);
        pinDialog.setCanceledOnTouchOutside(false);


        guiDialog = new Dialog(this);
        View v1 = LayoutInflater.from(this).inflate(R.layout.dialog_guicode,null,false);

        btnGuiMa = (TextView) v1.findViewById(R.id.btn_dialogguicode);
        edGuiMa = (EditText) v1.findViewById(R.id.ed_dialogguicode);

        guiDialog.setContentView(v1);
        guiDialog.setCancelable(false);
        guiDialog.setCanceledOnTouchOutside(false);
    }

    @Subscribe
    public void onClickDangNhap(SignInEvent signInEvent){
        Realm.init(this);
        RealmHandler r = new RealmHandler();
        List<TokenFirebaseContainer> tokens = r.getTokenFirebaseContainerList();
        if(!(tokens.size() == 0))token = tokens.get(0).getTokenFirebase();
        r.close();
        showDialog();

        username = signInEvent.getUsername();
        String password = signInEvent.getPassword();

        Activity activity = this;
        RetrofitFactory.getInstance().createService(TaiKhoanService.class).kiemTraTaiKhoan(new TaiKhoan(0,username,password)).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message m = response.body();

                    String[] l = m.getMessage().split(" ");
                    if(l[0].equals("Thanhcong")){
                        loai = l[1];
                        taiKhoan = new TaiKhoan(Integer.parseInt(l[2]),"","");
                        dangTao = Boolean.parseBoolean(l[3]);

                        capNhapTokenFirebase(taiKhoan.getId());
                    }else if(l[0].equals("Canmaxacnhan")){
                        loai = l[1];
                        taiKhoan = new TaiKhoan(Integer.parseInt(l[2]),"","");
                        dangTao = false;
                        pinDialog.show();
                    }else{
                        dismissDialog();
                        Toast.makeText(activity,"Không tồn tại tài khoản",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });

    }

    private void capNhapTokenFirebase(int id) {
        if(token != null){
            RetrofitFactory.getInstance().createService(TaiKhoanService.class).capNhapTokenChoTaiKhoan(id,token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.isSuccessful()){
                        dangNhap();
                    }else {
                        showMess("Lỗi xảy ra");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showMess("Lỗi xảy ra");
                }
            });

        }
    }

    @Subscribe
    public void onClickDangKi(SignUpEvent signUpEvent){

        showDialog();
        dangTao = false;

        username = signUpEvent.getUsername();
        String password = signUpEvent.getPassword();
        loai = signUpEvent.getLoai();
        Realm.init(this);

        RealmHandler r = new RealmHandler();
        List<TokenFirebaseContainer> tokens = r.getTokenFirebaseContainerList();
        if(!(tokens.size() == 0))token = tokens.get(0).getTokenFirebase();
        r.close();

        RetrofitFactory.getInstance().createService(TaiKhoanService.class).taoTaiKhoan(token,loai,new TaiKhoan(0,username,password)).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if(response.isSuccessful()){


                    taiKhoan = response.body();
                    dismissDialog();
                    if(taiKhoan.getMatkhau() == null){
                        showMess("Đã tồn tại tài khoản!!!");
                    }else{
                        showGuiMa();
                    }
                }else{
                    dismissDialog();
                    showMess("Lỗi");
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                dismissDialog();
                showMess("Lỗi mạng");
            }
        });

    }

    private void showGuiMa(){
        guiDialog.show();
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            mPhoneNumber = mPhoneNumber.substring(1,mPhoneNumber.length());
            edGuiMa.setText(mPhoneNumber + "");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_PHONE_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String mPhoneNumber = tMgr.getLine1Number();
                        mPhoneNumber = mPhoneNumber.substring(1,mPhoneNumber.length());
                        edGuiMa.setText(mPhoneNumber + "");
                    }
                    return;
                }
            }
        }
    }

    public void showMess(String mess){
        Toast.makeText(this,mess,Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        rotateLoading.show();
        dialog.show();
    }

    private void dismissDialog() {
        rotateLoading.hide();
        dialog.dismiss();
    }

    private void setupTablayout() {
        tablayoutMain.setupWithViewPager(vpMain);

        ViewGroup vg = (ViewGroup) tablayoutMain.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(TypefaceFactory.getInstance().factoryTypeface(getString(R.string.sweet), getApplicationContext()));
                }
            }
        }

    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapterForSign(getSupportFragmentManager());


        SignIn signInFragment = new SignIn();
        SignUp signUpFragment = new SignUp();
        viewPagerAdapter.addFragment(signInFragment, "Sign in");
        viewPagerAdapter.addFragment(signUpFragment, "Sign up");


        vpMain.setAdapter(viewPagerAdapter);
        vpMain.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void loginFail(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        dismissDialog();
    }

    public void loginSucess() {
//        Intent i = new Intent(this, MainActivity.class);
        dismissDialog();
//        startActivity(i);
    }
}
