package leu.doan_datdoan.fragment.khachhang;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leu.doan_datdoan.R;
import leu.doan_datdoan.model.KhachHang;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThongTinKhachHangFragment extends Fragment {
    private KhachHang kh;

    public ThongTinKhachHangFragment() {
        // Required empty public constructor
    }
    public void setKh(KhachHang kh) {
        this.kh = kh;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_khachhang_thong_tin, container, false);
    }

}
