package leu.doan_datdoan.network.khachhang;

import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.model.TaiKhoan;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MyPC on 14/11/2017.
 */

public interface KhachHangService {
    @POST("/DoAn/webapi/khachhang/{id}/themdonhang")
    Call<ResponseBody> taoDonHang(@Path("id")int id, @Body DonHang donHang);
}
