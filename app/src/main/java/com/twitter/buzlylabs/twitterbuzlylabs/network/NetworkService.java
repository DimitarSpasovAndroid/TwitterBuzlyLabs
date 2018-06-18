package com.twitter.buzlylabs.twitterbuzlylabs.network;

import android.content.Context;

import com.twitter.buzlylabs.twitterbuzlylabs.view.TweetsView;

/**
 * Created by Dimitar Spasov .
 */

public interface NetworkService {

    void fetchTweets(String queryString,TweetsView listener);
    void disposeAsyncTask();
}
