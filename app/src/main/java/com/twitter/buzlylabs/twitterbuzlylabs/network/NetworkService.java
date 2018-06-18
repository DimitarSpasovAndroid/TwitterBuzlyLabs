package com.twitter.buzlylabs.twitterbuzlylabs.network;

import com.twitter.buzlylabs.twitterbuzlylabs.view.TwitterSearchView;

/**
 * Created by Dimitar Spasov .
 */

public interface NetworkService {

    void fetchTweets(String queryString,TwitterSearchView listener);
    void disposeAsyncTask();

}
