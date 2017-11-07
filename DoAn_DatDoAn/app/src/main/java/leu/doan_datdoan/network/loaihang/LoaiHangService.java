package leu.doan_datdoan.network.loaihang;

import leu.doan_datdoan.model.LoaiHang;
import leu.doan_datdoan.model.MatHang;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by MyPC on 04/11/2017.
 */

public interface LoaiHangService {

    @POST("/DoAn/webapi/loaihang/{id}/mathang")
    Call<MatHang> themMatHangVaoLoaiHang(@Path("id")int idLoaiHang, @Body MatHang matHang);
}
