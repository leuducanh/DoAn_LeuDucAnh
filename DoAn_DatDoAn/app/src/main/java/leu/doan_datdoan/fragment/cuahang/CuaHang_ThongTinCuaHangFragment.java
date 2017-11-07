package leu.doan_datdoan.fragment.cuahang;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.network.LoadPicture;
import leu.doan_datdoan.network.Message;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import leu.doan_datdoan.network.taikhoan.TaiKhoanService;
import leu.doan_datdoan.network.uploadAnh.UploadService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuaHang_ThongTinCuaHangFragment extends Fragment{

    @BindView(R.id.ivanhcuahang_cuahang_thongtincuahangfragment)
    ImageView ivAnhCuaHang;
    @BindView(R.id.fab_cuahang_thongtincuahangfragment)
    FloatingActionButton fab;
    @BindView(R.id.edten_cuahang_thongtincuahangfragment)
    EditText edTen;
    @BindView(R.id.edsdt_cuahang_thongtincuahangfragment)
    EditText edSdt;
    @BindView(R.id.edmota_cuahang_thongtincuahangfragment)
    EditText edMota;
    @BindView(R.id.ln_cuahang_thongtincuahangfragment)
    LinearLayout ln;
    @BindView(R.id.tvdiadiem_cuahang_thongtincuahangfragment)
    TextView tvDiaDiem;
    @BindView(R.id.tvluuketqua_cuahang_thongtincuahangfragment)
    TextView tvLuu;
    @BindView(R.id.tvchondiadiem_cuahang_thongtincuahangfragment)
    TextView tvChonDD;
    @BindView(R.id.tvthoigianmocua_cuahang_thongtincuahangfragment)
    TextView tvMoCua;
    @BindView(R.id.tvthoigiandongcua_cuahang_thongtincuahangfragment)
    TextView tvDongCua;

    String thoiGianMoCua = "";
    String thoiGianDongCua = "";
    double lat;
    double lng;
    Dialog loadingDialog;
    Dialog cameraDialog;
    LinearLayout lnChupAnh;
    LinearLayout lnThuVien;
    CuaHang cuaHang = null;
    Activity activity ;
    Context context ;

    boolean daTaoCuaHang = false;
    int idTaiKhoan = 0;

    private Uri uri;

    private String mCurrentPhotoPath;
    private File file = null;
    private String imageName;

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    private static final int  CAMERA_IMAGE_REQUEST = 6385; // onActivityResult request
    private static final int PERMISSIONS_REQUEST_READ = 100;
    private static final int PERMISSIONS_REQUEST_WRITE = 200;
    private static final int PLACE_PICKER_REQUEST = 434;

    public CuaHang_ThongTinCuaHangFragment() {
        // Required empty public constructor
    }

    public void setActivityAndContext(Activity activity,Context context){
        this.activity = activity;
        this.context = context;
    }

    @OnClick(R.id.tvthoigiandongcua_cuahang_thongtincuahangfragment)
    public void chonThoiGianDongCua(){
        tvLuu.setVisibility(View.VISIBLE);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                thoiGianDongCua = hourOfDay + " " + minute;

                if(!tvMoCua.getText().toString().equals("")){
                    String[] l = thoiGianMoCua.split(" ");
                    long sub = subTime(l[0] + ":" + l[1],hourOfDay + ":" + minute);

                    if(sub < 0){
                        Toast.makeText(context,"Thời gian đóng cửa phải muộn hơn mở cửa.",Toast.LENGTH_SHORT).show();
                    }else{
                        tvDongCua.setText(hourOfDay + ":" + minute);
                    }
                }else{
                    tvMoCua.setText(hourOfDay + ":" + minute);
                }
            }
        }, hour, minute,
                DateFormat.is24HourFormat(getActivity())).show();
    }

    @OnClick(R.id.tvthoigianmocua_cuahang_thongtincuahangfragment)
    public void chonThoiGianMoCua(){
        tvLuu.setVisibility(View.VISIBLE);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                thoiGianMoCua = hourOfDay + " " + minute;

                if(!tvDongCua.getText().toString().equals("")){
                    String[] l = thoiGianDongCua.split(" ");
                    long sub = subTime(hourOfDay + ":" + minute,l[0] + ":" + l[1]);

                    if(sub < 0){
                        Toast.makeText(context,"Thời gian mở cửa phải sớm hơn đóng cửa.",Toast.LENGTH_SHORT).show();
                    }else{
                        tvMoCua.setText(hourOfDay + ":" + minute);
                    }
                }else {
                    tvMoCua.setText(hourOfDay + ":" + minute);
                }
            }
        }, hour, minute,
                DateFormat.is24HourFormat(getActivity())).show();
    }

    @OnClick(R.id.tvluuketqua_cuahang_thongtincuahangfragment)
    public void luuKetQua(){
        String m = "Chưa có thông tin ";
        String ten = edTen.getText().toString();
        if(ten.equals(""))m += " tên ";
        String sdt = edSdt.getText().toString();
        if(sdt.equals(""))m += "Sđt ";
        String diaDiem = tvDiaDiem.getText().toString();
        if(diaDiem.equals(""))m += " địa điểm ";
        if(thoiGianMoCua.equals(""))m += " thời gian mở cửa ";
        if(thoiGianDongCua.equals(""))m += " thời gian đóng cửa ";
        String mota = edMota.getText().toString();
        if(!daTaoCuaHang && file == null){
            m += " Chưa có ảnh.";
        }

        if(!m.equals("Chưa có thông tin ")){
            Toast.makeText(getContext(),m,Toast.LENGTH_LONG).show();
        }else {
            ((CuaHangActivity)activity).showingDialog();

            if(daTaoCuaHang){
                final CuaHang ch = new CuaHang(cuaHang.getId(), ten,diaDiem,lat,lng,mota,sdt,"",thoiGianMoCua,thoiGianDongCua);
                RetrofitFactory.getInstance().createService(CuaHangService.class).capNhapCuaHang(ch).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            if(file != null){
                                ch.setUrl(cuaHang.getUrl());
                                cuaHang = ch;
                                uploadFile(file);
                            }else{
                                ((CuaHangActivity)activity).dismissingDialog();
                            }

                            ((CuaHangActivity)activity).setCuaHang(cuaHang);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ((CuaHangActivity)activity).dismissingDialog();
                    }
                });
            }else{
                RetrofitFactory.getInstance().createService(TaiKhoanService.class).khoiTaoCuaHang(idTaiKhoan,new CuaHang(0, ten,diaDiem,lat,lng,mota,sdt,"",thoiGianMoCua,thoiGianDongCua)).enqueue(new Callback<CuaHang>() {
                    @Override
                    public void onResponse(Call<CuaHang> call, Response<CuaHang> response) {
                        if(response.isSuccessful()){
                            cuaHang = response.body();
                                uploadFile(file);
                        }else{
                            ((CuaHangActivity)activity).dismissingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<CuaHang> call, Throwable t) {
                        ((CuaHangActivity)activity).dismissingDialog();
                    }
                });
            }

        }
    }
    public void timDiaDiem(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
        tvLuu.setVisibility(View.VISIBLE);
        try {
            ln.setClickable(false);
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_cua_hang_thong_tin_cua_hang, container, false);

        addPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        addPermission(Manifest.permission.CAMERA);

        Bundle bundle = getArguments();
        daTaoCuaHang = bundle.getBoolean(CuaHangActivity.KEY_BUNDLE_DATAOCUAHANG);
        idTaiKhoan = bundle.getInt(CuaHangActivity.KEY_BUNDLE_IDTAIKHOAN);
        setupUI(v);
        setupEvents();
        return v;
    }

    private void addPermission(String s) {
        if(ContextCompat.checkSelfPermission(this.getContext(), s) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{s},100);
        }
    }

    private void setupEvents() {
        lnChupAnh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                cameraDialog.dismiss();
                dispatchTakePictureIntent();
            }
        });



        lnThuVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialog.dismiss();
                showChooser();
            }
        });

        final Context context = this.getContext();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    cameraDialog.show();
                }else{
                    addPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    addPermission(Manifest.permission.CAMERA);
                }
            }
        });

        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLuu.setVisibility(View.VISIBLE);
                timDiaDiem();
            }
        });

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tvLuu.setVisibility(View.VISIBLE);
            }
        };
        edMota.setOnFocusChangeListener(onFocusChangeListener);
        edSdt.setOnFocusChangeListener(onFocusChangeListener);
        edTen.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void setupUI(View v) {
        ButterKnife.bind(this,v);

        if(!daTaoCuaHang){
            tvLuu.setVisibility(View.VISIBLE);
        }else{
            tvLuu.setVisibility(View.GONE);

            cuaHang = (CuaHang) getArguments().getSerializable(CuaHangActivity.KEY_BUNDLE_CUAHANG);
            capNhapThongTin();

        }

        setupCameraDialog();
    }

    private void capNhapThongTin() {
        edTen.setText(cuaHang.getTen());
        edSdt.setText(cuaHang.getDienthoai());
        tvDiaDiem.setText(cuaHang.getDiaDiem());
        thoiGianMoCua = cuaHang.getThoiGianMoCua();
        thoiGianDongCua = cuaHang.getThoiGianDongCua();
        String[] l1 = thoiGianMoCua.split(" ");
        String[] l2 = thoiGianDongCua.split(" ");

        tvMoCua.setText(l1[0] + ":" + l1[1]);
        tvDongCua.setText(l2[0] + ":" + l2[1]);
        edMota.setText(cuaHang.getMota());
        LoadPicture.load(context,cuaHang.getUrl(),ivAnhCuaHang);
    }

    private void setupCameraDialog() {
        cameraDialog = new Dialog(this.getContext());

        View dialogCameraView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_camera, null, false);

        lnChupAnh = (LinearLayout) dialogCameraView.findViewById(R.id.lnchupanh_dialog);
        lnThuVien = (LinearLayout) dialogCameraView.findViewById(R.id.lnthuvien_dialog);
        cameraDialog.setContentView(dialogCameraView);
        cameraDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        uri = data.getData();

                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this.getContext(), uri);
                            file = FileUtils.getFile(this.getContext(),uri);
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
                            if(bitmap == null) file = null;
                            ivAnhCuaHang.setImageBitmap(bitmap);
                        } catch (Exception e) {
                        }
                    }
                }
                break;
            case CAMERA_IMAGE_REQUEST:
                if(resultCode == Activity.RESULT_OK){
                    file = new File(mCurrentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
                    ivAnhCuaHang.setImageBitmap(bitmap);
                }
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this.getContext());
                    tvDiaDiem.setText(place.getAddress());
                    lat = place.getLatLng().latitude;
                    lng = place.getLatLng().longitude;
                    ln.setClickable(true);
                }
                break;
        }
    }

    private void uploadFile(File file) {
        // create upload service client
        UploadService service =
                RetrofitFactory.getInstance().createService(UploadService.class);


        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(
                                getMimeType(file.getAbsolutePath())),
                        file
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        final String descriptionString = "cuahang" + " " + cuaHang.getId();
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        Call<Message> call = service.upload(description, body);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message m = response.body();

                    ((CuaHangActivity)activity).dismissingDialog();
                    tvLuu.setVisibility(View.GONE);
                    LoadPicture.load(context,m.getMessage(),ivAnhCuaHang);
                    daTaoCuaHang = true;
                    ((CuaHangActivity)activity).setDaTaoCuaHang(true);
                    ((CuaHangActivity)activity).dismissingDialog();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(this.getContext(),
                        "doan.my.package.name.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, CAMERA_IMAGE_REQUEST);
            }
        }
    }

    public  String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void showChooser() {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(
                target, "Chọn ảnh cho cửa hàng");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private long subTime(String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d1 = null;
        try {
            d1 = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d2 = null;
        try {
            d2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long elapsed = d2.getTime() - d1.getTime();
        return  elapsed;
    }
}
