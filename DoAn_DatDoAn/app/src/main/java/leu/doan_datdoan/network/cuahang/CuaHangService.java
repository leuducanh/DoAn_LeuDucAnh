package leu.doan_datdoan.network.cuahang;

import java.util.List;

import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.LoaiHang;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by MyPC on 31/10/2017.
 */

public interface CuaHangService {
    @PUT("/DoAn/webapi/cuahang")
    Call<ResponseBody> capNhapCuaHang(@Body CuaHang ch);

    @GET("/DoAn/webapi/cuahang/{id}/loaihang")
    Call<List<LoaiHang>> layToanBoLoaiHangTuCuaHang(@Path("id")int idCuaHang);

    @POST("/DoAn/webapi/cuahang/{id}/loaihang")
    Call<LoaiHang> themLoaiHang(@Path("id")int idCuaHang, @Body LoaiHang loaiHang);

    @GET("/DoAn/webapi/cuahang/{id}/donhang")
    Call<List<DonHang>> layDonHangTuCuaHang(@Path("id")int id);
}
