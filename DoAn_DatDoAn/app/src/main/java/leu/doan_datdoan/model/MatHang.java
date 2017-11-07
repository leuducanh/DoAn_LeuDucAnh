package leu.doan_datdoan.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MyPC on 21/10/2017.
 */

public class MatHang implements Serializable{
    private int id;
    private String ten;
    private int gia;
    @SerializedName("moTa")
    private String mota;
    @SerializedName("urlAnh")
    private String tenAnh;


    public MatHang(String ten, int gia) {
        this.ten = ten;
        this.gia = gia;
    }

    public MatHang(int id, String ten, int gia, String mota, String tenAnh) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.mota = mota;
        this.tenAnh = tenAnh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTenAnh() {
        return tenAnh;
    }

    public void setTenAnh(String tenAnh) {
        this.tenAnh = tenAnh;
    }

    public MatHang() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
