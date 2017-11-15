package leu.doan_datdoan.model;

import java.io.Serializable;

/**
 * Created by MyPC on 05/11/2017.
 */
public class NguoiVanChuyen implements Serializable{
    private int id;
    private String bienSo;
    private String dienThoai;
    private double lat;
    private double lng;
    private int trangThai;
    public NguoiVanChuyen() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NguoiVanChuyen(int id, String bienSo, String dienThoai, double lat, double lng, int trangThai) {
        super();
        this.id = id;
        this.bienSo = bienSo;
        this.dienThoai = dienThoai;
        this.lat = lat;
        this.lng = lng;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBienSo() {
        return bienSo;
    }
    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }
    public String getDienThoai() {
        return dienThoai;
    }
    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public int getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
