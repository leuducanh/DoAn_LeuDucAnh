package leu.doan_datdoan.network;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by MyPC on 01/11/2017.
 */

public class LoadPicture {
    public static void load(Context context, String picName, ImageView iv){
        Picasso.with(context).load("http://192.168.0.101:11324/DoAn/picture?name=" + picName).fit().into(iv);
    }
    public static void loadBlur(Context context, String picName, ImageView iv){
        Picasso.with(context).load("http://192.168.0.101:11324/DoAn/picture?name=" + picName).transform(new BlurTransformation(context)).fit().into(iv);
    }
}
