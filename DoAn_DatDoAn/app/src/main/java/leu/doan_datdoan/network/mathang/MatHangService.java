package leu.doan_datdoan.network.mathang;

import leu.doan_datdoan.model.MatHang;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by MyPC on 05/11/2017.
 */

public interface MatHangService {

    @PUT("/DoAn/webapi/mathang")
    Call<MatHang> suaMatHang(@Body MatHang matHang);

    @DELETE("/DoAn/webapi/mathang/{id}")
    Call<ResponseBody> xoaMatHang(@Path("id")int idMatHang);
}
