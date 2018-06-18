package com.twitter.buzlylabs.twitterbuzlylabs.network;

import android.content.Context;
import android.os.AsyncTask;

import com.twitter.buzlylabs.twitterbuzlylabs.view.TweetsView;

/**
 * Created by Dimitar Spasov .
 */

public class NetworkServiceImpl implements NetworkService {

    FetchTweetsAsyncTask asyncTask;

    @Override
    public void disposeAsyncTask() {

        if (asyncTask != null) {

            if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {

                asyncTask.cancel(true);
                asyncTask = null;

            }

        }

    }

    @Override
    public void fetchTweets(String queryString, TweetsView listener) {

        asyncTask = new FetchTweetsAsyncTask(listener);
        asyncTask.execute(queryString);

    }
}
