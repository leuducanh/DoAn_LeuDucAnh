package leu.doan_datdoan.network;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MyPC on 11/10/2017.
 */

public class RetrofitFactory {
    private static Retrofit retrofit;
    public static String apiBaseUrl = "http://192.168.0.101:14915";
    private static RetrofitFactory retrofitFactory = new RetrofitFactory();
    private static OkHttpClient.Builder httpbuilder = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create());

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    public static RetrofitFactory getInstance(){
        return retrofitFactory;
    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;
        Log.d("re123","" + newApiBaseUrl);

        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiBaseUrl);

        retrofit = builder.build();
    }


    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass){
        if (!httpbuilder.interceptors().contains(logging)) {
            httpbuilder.addInterceptor(logging);
            builder.client(httpbuilder.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
