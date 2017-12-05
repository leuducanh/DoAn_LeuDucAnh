package leu.doan_datdoan.model;

public class ThongKeLoaiHang {

	private String loaiHang;
	private int tongGia;
	public ThongKeLoaiHang(String loaiHang, int tongGia) {
		super();
		this.loaiHang = loaiHang;
		this.tongGia = tongGia;
	}
	public ThongKeLoaiHang() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getLoaiHang() {
		return loaiHang;
	}
	public void setLoaiHang(String loaiHang) {
		this.loaiHang = loaiHang;
	}
	public int getTongGia() {
		return tongGia;
	}
	public void setTongGia(int tongGia) {
		this.tongGia = tongGia;
	}
	
	
}
