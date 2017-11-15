package leu.doan_datdoan.database;

import java.util.List;

import io.realm.Realm;

/**
 * Created by MyPC on 08/11/2017.
 */

public class RealmHandler {
    private Realm realm;

    public RealmHandler() {
        realm = Realm.getDefaultInstance();
    }

    public void close(){
        realm.close();
    }

    public void addTokenFirebaseContainer(TokenFirebaseContainer tokenFirebaseContainer) {
        realm.beginTransaction();
        realm.copyToRealm(tokenFirebaseContainer);
        realm.commitTransaction();
    }

    public List<TokenFirebaseContainer> getTokenFirebaseContainerList() {
        return realm.where(TokenFirebaseContainer.class).findAll();
    }

    public void setToken(TokenFirebaseContainer tokenFirebaseContainer, String token) {
        realm.beginTransaction();
        tokenFirebaseContainer.setTokenFirebase(token);
        realm.commitTransaction();
    }

}

