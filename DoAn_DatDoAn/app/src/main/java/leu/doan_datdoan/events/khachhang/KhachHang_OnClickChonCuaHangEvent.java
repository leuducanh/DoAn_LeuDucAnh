package leu.doan_datdoan.events.khachhang;

import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.KhachHang;

/**
 * Created by MyPC on 13/11/2017.
 */

public class KhachHang_OnClickChonCuaHangEvent {
    private CuaHang cuaHang;


    public KhachHang_OnClickChonCuaHangEvent(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public CuaHang getCuaHang() {
        return cuaHang;
    }

    public void setCuaHang(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }
}
