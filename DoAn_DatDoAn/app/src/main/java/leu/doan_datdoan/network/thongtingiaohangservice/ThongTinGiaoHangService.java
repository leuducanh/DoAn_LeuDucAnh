package leu.doan_datdoan.network.thongtingiaohangservice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by MyPC on 27/10/2017.
 */

public interface ThongTinGiaoHangService {

    @GET("https://maps.googleapis.com/maps/api/directions/json")
    Call<ThongTin> getThongTinGiaoHang(@Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode,
                                       @Query("key") String key);
}
