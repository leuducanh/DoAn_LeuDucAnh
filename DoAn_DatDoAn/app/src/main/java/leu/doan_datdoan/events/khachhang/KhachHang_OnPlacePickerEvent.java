package leu.doan_datdoan.events.khachhang;

import com.google.android.gms.location.places.Place;

/**
 * Created by MyPC on 14/11/2017.
 */

public class KhachHang_OnPlacePickerEvent {
    private Place place;

    public KhachHang_OnPlacePickerEvent(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
