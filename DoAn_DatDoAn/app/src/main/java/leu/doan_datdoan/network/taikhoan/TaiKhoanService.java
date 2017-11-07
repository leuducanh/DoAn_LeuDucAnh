package leu.doan_datdoan.network.taikhoan;

import leu.doan_datdoan.model.CuaHang;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by MyPC on 01/11/2017.
 */

public interface TaiKhoanService {

    @POST("/DoAn/webapi/taikhoan/{id}/cuahang")
    Call<CuaHang> khoiTaoCuaHang(@Path("id")int idTaiKhoan, @Body CuaHang cuaHang);

    @GET("/DoAn/webapi/taikhoan/{id}/cuahang")
    Call<CuaHang> layCuaHangBangIdTaiKhoan(@Path("id")int idTaiKhoan);

}
