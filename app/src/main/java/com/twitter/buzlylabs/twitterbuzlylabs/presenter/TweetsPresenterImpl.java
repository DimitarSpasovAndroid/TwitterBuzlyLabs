package com.twitter.buzlylabs.twitterbuzlylabs.presenter;

import android.content.Context;

import com.twitter.buzlylabs.twitterbuzlylabs.network.NetworkService;
import com.twitter.buzlylabs.twitterbuzlylabs.view.TweetsView;
import com.twitter.buzlylabs.twitterbuzlylabs.view.TwitterSearchActivity;

/**
 * Created by Dimitar Spasov .
 */

public class TweetsPresenterImpl implements TweetsPresenter{

    TweetsView view;
    NetworkService networkService;;

    public TweetsPresenterImpl(TweetsView view, NetworkService networkService){
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
