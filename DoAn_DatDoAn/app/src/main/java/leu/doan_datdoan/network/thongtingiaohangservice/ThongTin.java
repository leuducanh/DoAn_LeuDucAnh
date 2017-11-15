package leu.doan_datdoan.network.thongtingiaohangservice;

import java.util.List;

/**
 * Created by MyPC on 27/10/2017.
 */

public class ThongTin {
    private String start_address;
    private String end_address;
    private List<Routes> routes;

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }
}
