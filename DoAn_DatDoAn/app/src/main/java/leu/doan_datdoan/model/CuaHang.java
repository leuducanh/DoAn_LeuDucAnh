package leu.doan_datdoan.model;

import java.io.Serializable;

/**
 * Created by MyPC on 31/10/2017.
 */

public class CuaHang implements Serializable{

    private int id;
    private String ten;
    private String diaDiem;
    private double lat;
    private double lng;
    private String mota;
    private String dienthoai;
    private String url;
    private String thoiGianMoCua;
    private String thoiGianDongCua;

    public CuaHang() {
    }

    public CuaHang(int id, String ten, String diaDiem, double lat, double lng, String mota, String dienthoai,
                   String url, String thoiGianMoCua, String thoiGianDongCua) {
        super();
        this.id = id;
        this.ten = ten;
        this.diaDiem = diaDiem;
        this.lat = lat;
        this.lng = lng;
        this.mota = mota;
        this.dienthoai = dienthoai;
        this.url = url;
        this.thoiGianMoCua = thoiGianMoCua;
        this.thoiGianDongCua = thoiGianDongCua;
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

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
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

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThoiGianMoCua() {
        return thoiGianMoCua;
    }

    public void setThoiGianMoCua(String thoiGianMoCua) {
        this.thoiGianMoCua = thoiGianMoCua;
    }

    public String getThoiGianDongCua() {
        return thoiGianDongCua;
    }

    public void setThoiGianDongCua(String thoiGianDongCua) {
        this.thoiGianDongCua = thoiGianDongCua;
    }
}
