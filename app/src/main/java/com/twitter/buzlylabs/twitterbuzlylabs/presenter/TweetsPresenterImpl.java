package com.twitter.buzlylabs.twitterbuzlylabs.presenter;

import com.twitter.buzlylabs.twitterbuzlylabs.network.NetworkService;
import com.twitter.buzlylabs.twitterbuzlylabs.view.TwitterSearchView;

/**
 * Created by Dimitar Spasov .
 */

public class TweetsPresenterImpl implements TweetsPresenter{

    TwitterSearchView view;
    NetworkService networkService;;

    public TweetsPresenterImpl(TwitterSearchView view, NetworkService networkService){
        this.view = view;
        this.networkService = networkService;
    }

    @Override
    public void onStop() {
        networkService.disposeAsyncTask();
    }

    @Override
    public void queryTweets(String query) {
        networkService.fetchTweets(query,view);
    }
}
