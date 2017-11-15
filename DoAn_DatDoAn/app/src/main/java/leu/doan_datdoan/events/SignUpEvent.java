package leu.doan_datdoan.events;

/**
 * Created by MyPC on 10/11/2017.
 */

public class SignUpEvent {
    private String username;
    private String password;
    private String loai;

    public SignUpEvent(String username, String password, String loai) {
        this.username = username;
        this.password = password;
        this.loai = loai;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
