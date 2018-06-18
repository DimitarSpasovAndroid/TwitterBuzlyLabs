package com.twitter.buzlylabs.twitterbuzlylabs.presenter;

/**
 * Created by Dimitar Spasov .
 */

public interface TweetsPresenter {

    void queryTweets(String query);
    void onStop();

}
