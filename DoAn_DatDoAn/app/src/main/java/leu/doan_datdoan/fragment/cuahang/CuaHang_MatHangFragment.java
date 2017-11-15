package leu.doan_datdoan.fragment.cuahang;


import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.ipaulpro.afilechooser.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.anim.MyBounceInterpolator;
import leu.doan_datdoan.events.cuahang.CuaHang_OnClickFabMatHang;
import leu.doan_datdoan.events.cuahang.CuaHang_OnClickMatHang;
import leu.doan_datdoan.model.LoaiHang;
import leu.doan_datdoan.model.MatHang;
import leu.doan_datdoan.model.Util;
import leu.doan_datdoan.network.LoadPicture;
import leu.doan_datdoan.network.Message;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import leu.doan_datdoan.network.loaihang.LoaiHangService;
import leu.doan_datdoan.network.mathang.MatHangService;
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
public class CuaHang_MatHangFragment extends Fragment {

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    private static final int  CAMERA_IMAGE_REQUEST = 6385; // onActivityResult request
    private static final int PERMISSIONS_REQUEST_READ = 100;
    private static final int PERMISSIONS_REQUEST_WRITE = 200;
    private static final int PLACE_PICKER_REQUEST = 434;

    @BindView(R.id.edtenmathang_cuahang_mathangfragment)
    EditText edTen;
    @BindView(R.id.edmotamathang_cuahang_mathangfragment)
    EditText edMoTa;
    @BindView(R.id.edgia_cuahang_mathangfragment)
    EditText edGia;
    @BindView(R.id.autolh_cuahang_mathangfragment)
    AutoCompleteTextView auto;
    @BindView(R.id.rlcamera_cuahang_mathangfragment)
    RelativeLayout rl;
    @BindView(R.id.ivmathang_cuahang_mathangfragment)
    ImageView ivMatHang;
    @BindView(R.id.ivcuahang_cuahang_mathangfragment)
    ImageView ivCuaHang;
    @BindView(R.id.btncapnhapmh_cuahang_mathangfragment)
    CircularProgressButton lbCapNhap;
    @BindView(R.id.btntaomh_cuahang_mathangfragment)
    CircularProgressButton lbTao;
    @BindView(R.id.btnxoamh_cuahang_mathangfragment)
    CircularProgressButton lbxoa;

    private List<LoaiHang> loaiHangList;
    private String tenAnhCuaHang;
    private MatHang matHang;
    Animation myAnim;
    Bitmap icon;
    MyBounceInterpolator interpolator;
    private ArrayList<String> list;

    private Context c;
    private Activity activity;
    private Dialog cameraDialog;
    private LinearLayout lnChupAnh;
    private LinearLayout lnThuVien;
    private String mCurrentPhotoPath;
    private File file = null;
    private Uri uri;
    private int idCuaHang;
    private int idLoaiHang;

    public void setC(Context c) {
        this.c = c;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private CuaHang_OnClickFabMatHang cuaHang_onClickFabMatHang = null;
    private CuaHang_OnClickMatHang cuaHang_onClickMatHang = null;

    public CuaHang_MatHangFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btncapnhapmh_cuahang_mathangfragment)
    public void onClickCapNhapMh(){
        if(!check()){
            Toast.makeText(activity,"Điền đủ thông tin và ảnh",Toast.LENGTH_SHORT).show();
        }else{
            lbCapNhap.startAnimation();

            setEnable(false);
            lbxoa.setEnabled(false);

            final MatHang mh = new MatHang(matHang.getId(),edTen.getText().toString(),Integer.parseInt(edGia.getText().toString()),edMoTa.getText().toString(),"");

            RetrofitFactory.getInstance().createService(MatHangService.class).suaMatHang(mh).enqueue(new Callback<MatHang>() {
                @Override
                public void onResponse(Call<MatHang> call, Response<MatHang> response) {
                    if(response.isSuccessful()){
                        if(file != null){
                            uploadFile(file);
                        }else{
                            lbCapNhap.doneLoadingAnimation(Color.WHITE,icon);
                            ((CuaHangActivity)activity).onBackPressed();
                        }
                    }else{
                        lbxoa.setEnabled(true);
                        setEnable(true);
                        lbCapNhap.revertAnimation();
                    }
                }

                @Override
                public void onFailure(Call<MatHang> call, Throwable t) {
                    Log.d("Abc"," 1" +t.getLocalizedMessage() + " " + t.getCause() + " " + t.getMessage());
                    lbxoa.setEnabled(true);
                    setEnable(true);
                    lbCapNhap.revertAnimation();
                }
            });
        }

    }
    @OnClick(R.id.btnxoamh_cuahang_mathangfragment)
    public void onClickXoaMh(){
        lbxoa.startAnimation();
        setEnable(false);
        lbCapNhap.setEnabled(false);

        RetrofitFactory.getInstance().createService(MatHangService.class).xoaMatHang(matHang.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    lbxoa.revertAnimation(new OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            lbxoa.setText("Thành công");
                            ((CuaHangActivity)activity).onBackPressed();
                        }
                    });
                }else{
                    setEnable(true);
                    lbCapNhap.setEnabled(true);
                    lbxoa.revertAnimation();
                    Toast.makeText(activity,"Lỗi!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setEnable(true);
                lbCapNhap.setEnabled(true);
                lbxoa.revertAnimation();
                Toast.makeText(activity,"Lỗi!!!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @OnClick(R.id.btntaomh_cuahang_mathangfragment)
    public void onClickThemMoiMh(){
        if(!check()){
            Toast.makeText(activity,"Điền đủ thông tin và ảnh",Toast.LENGTH_SHORT).show();
        }else{
            setEnable(false);
            lbTao.startAnimation();

            final MatHang mh = new MatHang(0,edTen.getText().toString(),Integer.parseInt(edGia.getText().toString()),edMoTa.getText().toString(),"");

            int idLoaiHang = -1;
            String s = Util.formatName(auto.getText().toString());
            for(int i = 0;i < loaiHangList.size();i++){
                if(s.equals(Util.formatName(loaiHangList.get(i).getTen()))){
                    idLoaiHang = loaiHangList.get(i).getId();
                }
            }

            if(idLoaiHang == -1){
                RetrofitFactory.getInstance().createService(CuaHangService.class).themLoaiHang(idCuaHang,new LoaiHang(0,auto.getText().toString(),null)).enqueue(new Callback<LoaiHang>() {
                    @Override
                    public void onResponse(Call<LoaiHang> call, Response<LoaiHang> response) {
                        if(response.isSuccessful()){
                           uploadMatHang(mh,response.body().getId());
                        }else{
                           setEnable(true);
                            Toast.makeText(activity,"Lỗi!!!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoaiHang> call, Throwable t) {
                        lbTao.revertAnimation();
                        Toast.makeText(activity,"Lỗi!!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                uploadMatHang(mh,idLoaiHang);
            }
        }

    }

    private void uploadMatHang(MatHang mh,int idLoaiHang){
        RetrofitFactory.getInstance().createService(LoaiHangService.class).themMatHangVaoLoaiHang(idLoaiHang,
                mh).enqueue(new Callback<MatHang>() {
            @Override
            public void onResponse(Call<MatHang> call, Response<MatHang> response) {
                if(response.isSuccessful()){
                    matHang = response.body();
                    if(file != null){
                        uploadFile(file);
                    }else{
                        if(lbTao.getVisibility() == View.VISIBLE){
                            lbTao.revertAnimation(new OnAnimationEndListener() {
                                @Override
                                public void onAnimationEnd() {
                                    lbxoa.setText("Thành công");
                                    ((CuaHangActivity)activity).onBackPressed();
                                }
                            });
                        }else{
                            lbCapNhap.revertAnimation(new OnAnimationEndListener() {
                                @Override
                                public void onAnimationEnd() {
                                    lbxoa.setText("Thành công");
                                    ((CuaHangActivity)activity).onBackPressed();
                                }
                            });
                        }
                    }
                }else{
                    setEnable(true);
                    lbTao.revertAnimation();
                    Toast.makeText(activity,"Lỗi!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MatHang> call, Throwable t) {
                lbTao.revertAnimation();
                setEnable(true);
                Toast.makeText(activity,"Lỗi!!!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupCameraDialog() {
        cameraDialog = new Dialog(this.getContext());


        View dialogCameraView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_camera, null, false);

        lnChupAnh = (LinearLayout) dialogCameraView.findViewById(R.id.lnchupanh_dialog);
        lnThuVien = (LinearLayout) dialogCameraView.findViewById(R.id.lnthuvien_dialog);
        cameraDialog.setContentView(dialogCameraView);
        cameraDialog.setCanceledOnTouchOutside(true);
        lnChupAnh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                cameraDialog.dismiss();
                rl.setClickable(true);
                dispatchTakePictureIntent();
            }
        });

        lnThuVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialog.dismiss();
                rl.setClickable(true);
                showChooser();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cua_hang_mat_hang, container, false);

        icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.check);
        myAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        setupUI(v);

        setupEvents();
        return v;
    }

    private void setupEvents() {
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.startAnimation(myAnim);
                cameraDialog.show();
            }
        });
    }

    private boolean check(){
        boolean flag = true;
        if(edTen.getText().toString().equals(""))flag = false;
        if(edGia.getText().toString().equals(""))flag = false;
        if(edMoTa.getText().toString().equals(""))flag = false;
        if(file == null && matHang == null)flag = false;
        if(auto.getText().toString().equals(""))flag = false;
        return flag;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this,v);
        setupCameraDialog();

        EventBus.getDefault().register(this);

    }


    @Subscribe(sticky = true)
    public void onClickMatHangRvMatHang(CuaHang_OnClickMatHang cuaHang_onClickMatHang){
        this.cuaHang_onClickMatHang = cuaHang_onClickMatHang;
        idLoaiHang = cuaHang_onClickMatHang.getIdLoaiHang();
        matHang = cuaHang_onClickMatHang.getMatHang();
        tenAnhCuaHang = cuaHang_onClickMatHang.getTenAnhCuaHang();
        edTen.setText(matHang.getTen());
        edMoTa.setText(matHang.getMota());
        edGia.setText(matHang.getGia() + "");
        LoadPicture.loadBlur(getContext(),tenAnhCuaHang,ivCuaHang);
        LoadPicture.load(getContext(),matHang.getTenAnh(),ivMatHang);


        lbTao.setVisibility(View.GONE);

        lbxoa.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                ((CuaHangActivity)activity).onBackPressed();
            }
        });
        lbCapNhap.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                ((CuaHangActivity)activity).onBackPressed();
            }
        });
        auto.setText(cuaHang_onClickMatHang.getTenLoaiHang());
        auto.setKeyListener(null);

        EventBus.getDefault().removeStickyEvent(cuaHang_onClickMatHang);
    }

    @Subscribe(sticky = true)
    public void onClickFabMatHang(CuaHang_OnClickFabMatHang cuaHang_onClickFabMatHang){
        this.cuaHang_onClickFabMatHang = cuaHang_onClickFabMatHang;
        idCuaHang = cuaHang_onClickFabMatHang.getIdCuaHang();
        loaiHangList = cuaHang_onClickFabMatHang.getLoaiHangs();
        tenAnhCuaHang = cuaHang_onClickFabMatHang.getTenAnh();
        list = new ArrayList<>();

        for(int i = 0;i < loaiHangList.size();i++){
            list.add(loaiHangList.get(i).getTen());
        }

        auto.setDropDownBackgroundResource(R.color.black);

        auto.setAdapter(new AutoCompleteAdapter(c, android.R.layout.simple_dropdown_item_1line));

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = (String) adapterView.getItemAtPosition(position);
                auto.setText(str);
            }
        });
        auto.showDropDown();

        LoadPicture.loadBlur(getContext(),tenAnhCuaHang,ivCuaHang);

        edTen.setText("");
        edMoTa.setText("");
        edGia.setText("");
        lbTao.setVisibility(View.VISIBLE);
        lbCapNhap.setVisibility(View.GONE);
        lbxoa.setVisibility(View.GONE);
        lbTao.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                ((CuaHangActivity)activity).onBackPressed();
            }
        });
        EventBus.getDefault().removeStickyEvent(cuaHang_onClickFabMatHang);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> mData;

        public AutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            mData = new ArrayList<String>();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int index) {
            return mData.get(index);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(final CharSequence constraint) {
                    final FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        ArrayList<String> matchList = new ArrayList<String>();
                        for (int i =0;i<loaiHangList.size();i++){
                            String word = loaiHangList.get(i).getTen().toLowerCase();
                            word = Normalizer.normalize(word, Normalizer.Form.NFKD); // Decompose.
                            word = word.replaceAll("\\p{M}", ""); // Remove diacriticals.
                            word = word.replaceAll("\\p{C}", "");
                            String word1 = constraint.toString();
                            word1 = Normalizer.normalize(word1, Normalizer.Form.NFKD); // Decompose.
                            word1 = word1.replaceAll("\\p{M}", ""); // Remove diacriticals.
                            word1 = word1.replaceAll("\\p{C}", "");
                            if(word.contains(word1)){
                                matchList.add(loaiHangList.get(i).getTen());
                            }
                        }
                        mData = matchList;
                        filterResults.values = mData;
                        filterResults.count = mData.size();
                    }
                    Log.d("abc","-" + mData.size() + "- ");
                    return filterResults;
        }

        @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
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
                            ivMatHang.setImageBitmap(bitmap);
                            rl.setClickable(true);
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
                    ivMatHang.setImageBitmap(bitmap);
                    rl.setClickable(true);
                }
                break;
        }
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
                target, "Chọn ảnh cho mặt hàng");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
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

        final String descriptionString = "mathang" + " " + matHang.getId();
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        Call<Message> call = service.upload(description, body);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    Message m = response.body();

                    if(lbTao.getVisibility() == View.VISIBLE){
                        lbTao.revertAnimation(new OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                lbTao.setText("Thành công");
                                ((CuaHangActivity)activity).onBackPressed();
                            }
                        });
                        ((CuaHangActivity)activity).onBackPressed();
                    }else{
                        lbCapNhap.revertAnimation(new OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                lbCapNhap.setText("Thành công");
                                ((CuaHangActivity)activity).onBackPressed();
                            }
                        });
                    }
                }else{
                    Toast.makeText(activity,"Lỗi up ảnh!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(activity,"Lỗi up ảnh!!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEnable(boolean flag){
        edMoTa.setEnabled(flag);
        edGia.setEnabled(flag);
        edTen.setEnabled(flag);
        auto.setEnabled(flag);
        rl.setEnabled(flag);
    }
}
