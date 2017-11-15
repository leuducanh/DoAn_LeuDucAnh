package leu.doan_datdoan.events.cuahang;

import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;

/**
 * Created by MyPC on 06/11/2017.
 */

public class CuaHang_OnClickChiTietPheDuyetMatHang {
    private DonHang donHang;
    private CuaHang cuaHang;


    public CuaHang_OnClickChiTietPheDuyetMatHang(DonHang donHang, CuaHang cuaHang) {
        this.donHang = donHang;
        this.cuaHang = cuaHang;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public CuaHang getCuaHang() {
        return cuaHang;
    }

    public void setCuaHang(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }
}
