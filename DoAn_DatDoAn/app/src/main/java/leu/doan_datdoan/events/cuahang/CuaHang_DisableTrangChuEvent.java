package leu.doan_datdoan.events.cuahang;

/**
 * Created by MyPC on 30/10/2017.
 */

public class CuaHang_DisableTrangChuEvent {
    private boolean disable;

    public CuaHang_DisableTrangChuEvent(boolean disable) {
        this.disable = disable;
    }

    public CuaHang_DisableTrangChuEvent() {
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
