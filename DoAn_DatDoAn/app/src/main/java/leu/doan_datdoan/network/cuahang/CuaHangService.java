package leu.doan_datdoan.network.cuahang;

import java.util.List;

import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.LoaiHang;
import leu.doan_datdoan.model.ThongKe;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/DoAn/webapi/cuahang")
    Call<List<CuaHang>> layCuaHangTheoTen(@Query("ten") String ten);

    @GET("/DoAn/webapi/cuahang/gandiadiem")
    Call<List<CuaHang>> layCuaHangGanViTri(@Query("lat") double lat,@Query("lng") double lng);

    @GET("/DoAn/webapi/cuahang/{id}/thongke")
    Call<ThongKe> thongKeCuaHang(@Path("id")int id,@Query("ngay")int ngay,@Query("thang")int thang,@Query("nam")int nam,@Query("chedo")int cheDo);
}
