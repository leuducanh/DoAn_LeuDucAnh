package leu.doan_datdoan.network.taikhoan;

import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.model.TaiKhoan;
import leu.doan_datdoan.network.Message;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MyPC on 01/11/2017.
 */

public interface TaiKhoanService {

    @POST("/DoAn/webapi/taikhoan/{id}/cuahang")
    Call<CuaHang> khoiTaoCuaHang(@Path("id")int idTaiKhoan, @Body CuaHang cuaHang);

    @POST("/DoAn/webapi/taikhoan/{id}/khachhang")
    Call<KhachHang> khoiTaoKhachHang(@Path("id")int idTaiKhoan, @Body KhachHang khachHang);

    @GET("/DoAn/webapi/taikhoan/{id}/cuahang")
    Call<CuaHang> layCuaHangBangIdTaiKhoan(@Path("id")int idTaiKhoan);

    @GET("/DoAn/webapi/taikhoan/{id}/khachhang")
    Call<KhachHang> layKhachHangBangIdTaiKhoan(@Path("id")int idTaiKhoan);

    @PUT("/DoAn/webapi/taikhoan/{id}/tokenfirebase")
    Call<ResponseBody> capNhapTokenChoTaiKhoan(@Path("id")int id, @Query("token")String token);

    @POST("/DoAn/webapi/taikhoan")
    Call<TaiKhoan> taoTaiKhoan(@Query("token_firebase")String token, @Query("loainguoidung")String loai, @Body TaiKhoan taiKhoan);

    @GET("/DoAn/webapi/taikhoan/{id}/guimaxacnhan")
    Call<Message> guiSmsXacNhan(@Path("id") int id,@Query("sdt")String sdt);

    @GET("/DoAn/webapi/taikhoan/{id}/xacnhanma")
    Call<Message> xacNhanMa(@Path("id") int id,@Query("maxacnhan")String maXacNhan);

    @PUT("/DoAn/webapi/taikhoan/kiemtrataikhoan")
    Call<Message> kiemTraTaiKhoan(@Body TaiKhoan tk);
}
