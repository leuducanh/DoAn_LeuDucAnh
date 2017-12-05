package leu.doan_datdoan.model;
import java.util.List;

public class ThongKe {
	private List<ThongKeLoaiHang> thongKeLoaiHangs;
	private List<ThongKeMatHang> thongKeMatHangs;
	public ThongKe(List<ThongKeLoaiHang> thongKeLoaiHangs, List<ThongKeMatHang> thongKeMatHangs) {
		super();
		this.thongKeLoaiHangs = thongKeLoaiHangs;
		this.thongKeMatHangs = thongKeMatHangs;
	}
	public ThongKe() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<ThongKeLoaiHang> getThongKeLoaiHangs() {
		return thongKeLoaiHangs;
	}
	public void setThongKeLoaiHangs(List<ThongKeLoaiHang> thongKeLoaiHangs) {
		this.thongKeLoaiHangs = thongKeLoaiHangs;
	}
	public List<ThongKeMatHang> getThongKeMatHangs() {
		return thongKeMatHangs;
	}
	public void setThongKeMatHangs(List<ThongKeMatHang> thongKeMatHangs) {
		this.thongKeMatHangs = thongKeMatHangs;
	}
	
	
	
}
