package leu.doan_datdoan.events.khachhang;


import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.KhachHang;

/**
 * Created by MyPC on 24/10/2017.
 */

public class OnClickBtnDatHang {
    private DonHang donHang;

    public OnClickBtnDatHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }
}
