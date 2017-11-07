package leu.doan_datdoan.network.donhang;

import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by MyPC on 07/11/2017.
 */

public interface DonHangService {
    @PUT("/DoAn/webapi/donhang/{id}")
    Call<ResponseBody> capNhapDonHang(@Path("id")int id, @Body DonHang dh);
    @DELETE("/DoAn/webapi/donhang/{id}")
    Call<ResponseBody> huyDonHang(@Path("id")int id);
}
