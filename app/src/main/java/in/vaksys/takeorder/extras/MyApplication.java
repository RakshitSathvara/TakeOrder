package in.vaksys.takeorder.extras;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
                .name("takeordernew122.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(configuration);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void hideKeyboard(Activity context) {
        // Check if no view has focus:
        View view1 = context.getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }
}
