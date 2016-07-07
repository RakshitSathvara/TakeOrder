package in.vaksys.takeorder.extras;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dell980 on 6/18/2016.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .name("takeordernew10.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(configuration);
    }
}
