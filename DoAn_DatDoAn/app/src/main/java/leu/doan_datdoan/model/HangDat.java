package leu.doan_datdoan.model;

import java.io.Serializable;

/**
 * Created by MyPC on 22/10/2017.
 */

public class HangDat implements Serializable{

    private MatHang matHang;
    private int soluong;

    public HangDat(MatHang matHang, int soluong) {
        this.matHang = matHang;
        this.soluong = soluong;
    }

    public MatHang getMatHang() {
        return matHang;
    }

    public void setMatHang(MatHang matHang) {
        this.matHang = matHang;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
