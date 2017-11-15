package leu.doan_datdoan.events.cuahang;

import java.util.List;

import leu.doan_datdoan.model.LoaiHang;

/**
 * Created by MyPC on 03/11/2017.
 */

public class CuaHang_OnClickFabMatHang {
    private String tenAnh;
    private List<LoaiHang> loaiHangs;
    private int idCuaHang;


    public CuaHang_OnClickFabMatHang(String tenAnh, List<LoaiHang> loaiHangs, int idCuaHang) {
        this.tenAnh = tenAnh;
        this.loaiHangs = loaiHangs;
        this.idCuaHang = idCuaHang;
    }

    public String getTenAnh() {
        return tenAnh;
    }

    public void setTenAnh(String tenAnh) {
        this.tenAnh = tenAnh;
    }

    public List<LoaiHang> getLoaiHangs() {
        return loaiHangs;
    }

    public void setLoaiHangs(List<LoaiHang> loaiHangs) {
        this.loaiHangs = loaiHangs;
    }

    public int getIdCuaHang() {
        return idCuaHang;
    }

    public void setIdCuaHang(int idCuaHang) {
        this.idCuaHang = idCuaHang;
    }
}
