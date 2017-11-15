package leu.doan_datdoan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MyPC on 25/10/2017.
 */

public class DonHang implements Serializable {
    private int id;
    private KhachHang khachHang;
    private CuaHang cuaHang;
    private NguoiVanChuyen nguoiVanChuyen;
    private double lat;
    private double lng;
    private String diaChi;
    private int giaVanChuyen;
    private int giaHang;
    private String trangThai;
    private String tenNguoiDat;
    private String ghiChu;
    private String sdt;
    private List<HangDat> hangDatList;

    public DonHang() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DonHang(int id, String trangThai) {
        super();
        this.id = id;
        this.trangThai = trangThai;
    }

    public DonHang(KhachHang khachHang, CuaHang cuaHang, int giaVanChuyen, int giaHang, List<HangDat> hangDatList) {
        super();
        this.khachHang = khachHang;
        this.cuaHang = cuaHang;
        this.giaVanChuyen = giaVanChuyen;
        this.giaHang = giaHang;
        this.hangDatList = hangDatList;
    }


    public DonHang(int id, KhachHang khachHang, CuaHang cuaHang, NguoiVanChuyen nguoiVanChuyen, double lat, double lng,
                   String diaChi, int giaVanChuyen, int giaHang, String trangThai, String tenNguoiDat, String ghiChu,
                   String sdt, List<HangDat> hangDatList) {
        super();
        this.id = id;
        this.khachHang = khachHang;
        this.cuaHang = cuaHang;
        this.nguoiVanChuyen = nguoiVanChuyen;
        this.lat = lat;
        this.lng = lng;
        this.diaChi = diaChi;
        this.giaVanChuyen = giaVanChuyen;
        this.giaHang = giaHang;
        this.trangThai = trangThai;
        this.tenNguoiDat = tenNguoiDat;
        this.ghiChu = ghiChu;
        this.sdt = sdt;
        this.hangDatList = hangDatList;
    }


    public DonHang(int id, CuaHang cuaHang, NguoiVanChuyen nguoiVanChuyen, double lat, double lng, String diaChi,
                   int giaVanChuyen, int giaHang, String trangThai, String tenNguoiDat, String ghiChu, String sdt,
                   List<HangDat> hangDatList) {
        super();
        this.id = id;
        this.cuaHang = cuaHang;
        this.nguoiVanChuyen = nguoiVanChuyen;
        this.lat = lat;
        this.lng = lng;
        this.diaChi = diaChi;
        this.giaVanChuyen = giaVanChuyen;
        this.giaHang = giaHang;
        this.trangThai = trangThai;
        this.tenNguoiDat = tenNguoiDat;
        this.ghiChu = ghiChu;
        this.sdt = sdt;
        this.hangDatList = hangDatList;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }


    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }


    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CuaHang getCuaHang() {
        return cuaHang;
    }

    public void setCuaHang(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public NguoiVanChuyen getNguoiVanChuyen() {
        return nguoiVanChuyen;
    }

    public void setNguoiVanChuyen(NguoiVanChuyen nguoiVanChuyen) {
        this.nguoiVanChuyen = nguoiVanChuyen;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getGiaVanChuyen() {
        return giaVanChuyen;
    }

    public void setGiaVanChuyen(int giaVanChuyen) {
        this.giaVanChuyen = giaVanChuyen;
    }

    public int getGiaHang() {
        return giaHang;
    }

    public void setGiaHang(int giaHang) {
        this.giaHang = giaHang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenNguoiDat() {
        return tenNguoiDat;
    }

    public void setTenNguoiDat(String tenNguoiDat) {
        this.tenNguoiDat = tenNguoiDat;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<HangDat> getHangDatList() {
        return hangDatList;
    }

    public void setHangDatList(List<HangDat> hangDatList) {
        this.hangDatList = hangDatList;
    }

}
