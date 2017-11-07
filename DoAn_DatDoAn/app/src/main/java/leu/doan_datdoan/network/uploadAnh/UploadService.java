package leu.doan_datdoan.network.uploadAnh;

import leu.doan_datdoan.network.Message;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by MyPC on 11/10/2017.
 */

public interface UploadService {
    @Multipart
    @POST("/DoAn/webapi/cuahang/upload")
    Call<Message> upload(@Part("des") RequestBody des, @Part MultipartBody.Part file);
}
