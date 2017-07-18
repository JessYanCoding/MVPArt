package me.jessyan.art.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * Created by jess on 18/07/2017 19:06
 * Contact with jess.yan.effort@gmail.com
 */

public interface AppLifecycles {
    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
