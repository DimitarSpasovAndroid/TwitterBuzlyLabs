package com.twitter.buzlylabs.twitterbuzlylabs;

import android.app.Application;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import timber.log.Timber;


/**
 * Created by Dimitar Spasov .
 */

public class TwitterBuzlyLabsApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        MultiDex.install(this);

    }
}
