package leu.doan_datdoan.database;

import io.realm.RealmObject;

/**
 * Created by MyPC on 08/11/2017.
 */

public class TokenFirebaseContainer extends RealmObject{
    private String tokenFirebase;

    public TokenFirebaseContainer() {
    }

    public TokenFirebaseContainer(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }
}
