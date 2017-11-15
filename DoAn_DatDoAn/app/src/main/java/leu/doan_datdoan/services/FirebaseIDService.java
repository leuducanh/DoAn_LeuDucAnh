package leu.doan_datdoan.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import leu.doan_datdoan.R;
import leu.doan_datdoan.database.RealmHandler;
import leu.doan_datdoan.database.TokenFirebaseContainer;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.taikhoan.TaiKhoanService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MyPC on 15/10/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    public static final String KEY_THONGTINSHIPPER = "Thongtinshipper";

    public List<TokenFirebaseContainer> lst;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        saveTokenToServer(token);
    }

    private void saveTokenToServer(String token) {

//        RetrofitFactory.getInstance().createService(TaiKhoanService.class).capNhapTokenChoTaiKhoan(2,token).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });


        Realm.init(this);
        RealmHandler r = new RealmHandler();
        lst = new ArrayList<>();
        lst = r.getTokenFirebaseContainerList();
        if(lst.size() == 0){
            r.addTokenFirebaseContainer(new TokenFirebaseContainer(token));
        }else{
            r.setToken(lst.get(0),token);
        }
        r.close();
    }

}
