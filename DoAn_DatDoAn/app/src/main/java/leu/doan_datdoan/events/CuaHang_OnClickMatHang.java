package leu.doan_datdoan.events;

import java.util.List;

import leu.doan_datdoan.model.LoaiHang;
import leu.doan_datdoan.model.MatHang;

/**
 * Created by MyPC on 03/11/2017.
 */

public class CuaHang_OnClickMatHang {
    private MatHang matHang;
    private String tenAnhCuaHang;
    private String tenLoaiHang;
    private int idLoaiHang;

    public CuaHang_OnClickMatHang(MatHang matHang, String tenAnhCuaHang, String tenLoaiHang, int idLoaiHang) {
        this.matHang = matHang;
        this.tenAnhCuaHang = tenAnhCuaHang;
        this.tenLoaiHang = tenLoaiHang;
        this.idLoaiHang = idLoaiHang;
    }

    public MatHang getMatHang() {
        return matHang;
    }

    public void setMatHang(MatHang matHang) {
        this.matHang = matHang;
    }

    public String getTenAnhCuaHang() {
        return tenAnhCuaHang;
    }

    public void setTenAnhCuaHang(String tenAnhCuaHang) {
        this.tenAnhCuaHang = tenAnhCuaHang;
    }

    public String getTenLoaiHang() {
        return tenLoaiHang;
    }

    public void setTenLoaiHang(String tenLoaiHang) {
        this.tenLoaiHang = tenLoaiHang;
    }

    public int getIdLoaiHang() {
        return idLoaiHang;
    }

    public void setIdLoaiHang(int idLoaiHang) {
        this.idLoaiHang = idLoaiHang;
    }
}
