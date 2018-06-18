package com.twitter.buzlylabs.twitterbuzlylabs.view;

import android.content.Context;

import com.twitter.buzlylabs.twitterbuzlylabs.model.TweetModel;

import java.util.ArrayList;

/**
 * Created by Dimitar Spasov .
 */

public interface TwitterSearchView {

    void addNewTweets(ArrayList<TweetModel> tweets);

    long getLastTweetId();

    void setLastTweetId(long id);

    Context getContext();
}
