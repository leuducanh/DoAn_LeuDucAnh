package leu.doan_datdoan.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.RemoteMessage;

import leu.doan_datdoan.R;
import leu.doan_datdoan.fragment.cuahang.CuaHang_ChiTietPheDuyetFragment;
import leu.doan_datdoan.fragment.cuahang.CuaHang_PheDuyetFragment;

/**
 * Created by MyPC on 15/10/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private final String CUAHANG_THEMDONHANG = "themdonhang";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null){
            String chuDe = remoteMessage.getNotification().getTitle();
            String thongDiep = remoteMessage.getNotification().getBody();

            xuLiBroadcast(chuDe,thongDiep,remoteMessage.getData().get("message"));
        }
    }

    private void xuLiBroadcast(String chuDe,String thongDiep,String json) {
        Intent intent = new Intent();
        switch (chuDe){
            case CUAHANG_THEMDONHANG:
                pushNoti("Thêm đơn hàng mới",thongDiep);
                intent.setAction(CuaHang_PheDuyetFragment.CUAHANG_PHEDUYETBROADCAST);
                intent.putExtra("donhang",json);
                sendBroadcast(intent);
                break;
        }

        sendBroadcast(intent);
    }

    private void pushNoti(String chuDe,String thongDiep){
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),new Intent(),PendingIntent.FLAG_UPDATE_CURRENT  | PendingIntent.FLAG_ONE_SHOT);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.noti);
        contentView.setImageViewResource(R.id.iv_noti,R.drawable.ic_store_black_24dp);
        contentView.setTextViewText(R.id.tvtitle_noti, chuDe);
        contentView.setTextViewText(R.id.tvcontent_noti, thongDiep);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int notifiId = 1;
        Notification n  = new NotificationCompat.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_search_black_24dp)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(uri)
                .build();

        n.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notifiId, n);
    }

}
