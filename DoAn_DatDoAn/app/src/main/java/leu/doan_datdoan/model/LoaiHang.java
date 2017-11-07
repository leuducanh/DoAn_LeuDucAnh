package leu.doan_datdoan.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MyPC on 21/10/2017.
 */

public class LoaiHang {
    private int id;
    private String ten;

    @SerializedName("dsMatHang")
    List<MatHang> matHangs;

    public LoaiHang(int id, String ten, List<MatHang> matHangs) {
        this.id = id;
        this.ten = ten;
        this.matHangs = matHangs;
    }

    public LoaiHang(String ten, List<MatHang> matHangs) {
        this.ten = ten;
        this.matHangs = matHangs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public List<MatHang> getMatHangs() {
        return matHangs;
    }

    public void setMatHangs(List<MatHang> matHangs) {
        this.matHangs = matHangs;
    }
}
