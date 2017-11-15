package leu.doan_datdoan.events.khachhang;

import android.location.Location;

/**
 * Created by MyPC on 26/10/2017.
 */

public class OnGpsTracker {
    private Location location;

    public OnGpsTracker(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
