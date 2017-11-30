package leu.doan_datdoan.network.nguoivanchuyen;

import leu.doan_datdoan.model.TaiKhoan;
import leu.doan_datdoan.network.Message;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MyPC on 17/11/2017.
 */

public interface NguoiVanChuyenService {

    @PUT("/DoAn/webapi/nguoivanchuyen/{id}/trangthai")
    Call<ResponseBody> capNhapTrangThai(@Path("id")int id, @Query("trangthai")int trangthai);

    @PUT("/DoAn/webapi/nguoivanchuyen/{id}/toado")
    Call<ResponseBody> capNhapToaDo(@Path("id")int id, @Query("lat")double lat,@Query("lng")double lng);
}
