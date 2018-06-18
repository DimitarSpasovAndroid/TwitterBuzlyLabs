package com.twitter.buzlylabs.twitterbuzlylabs.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;
import com.twitter.buzlylabs.twitterbuzlylabs.Constants;
import com.twitter.buzlylabs.twitterbuzlylabs.R;
import com.twitter.buzlylabs.twitterbuzlylabs.model.TweetModel;
import com.twitter.buzlylabs.twitterbuzlylabs.view.TwitterSearchView;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import timber.log.Timber;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Dimitar Spasov .
 */

public class FetchTweetsAsyncTask extends AsyncTask<String, Void, Integer> {

    private Context context;
    private ArrayList<TweetModel> tweets;
    private final int SUCCESS = 0;
    private final int FAILURE = SUCCESS + 1;
    private ProgressDialog dialog;
    private TwitterSearchView listener;
    private OAuth2Token token;
    private QueryResult result;

    public FetchTweetsAsyncTask(TwitterSearchView listener) {
        this.listener = listener;
        context = listener.getContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "", context.getString(R.string.searching));
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {

            authenticate();

            query(strings[0]);

            parseData();

            return SUCCESS;
        } catch (TwitterException e) {
            Timber.d(e);
        }

        return FAILURE;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        dialog.dismiss();

        if (integer == SUCCESS) {
            listener.addNewTweets(tweets);
        } else {
            Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        dialog.dismiss();
    }

    private void authenticate() throws TwitterException {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        String tokenString = sharedPreferences.getString(Constants.SHARED_PREFS_TOKEN_KEY, null);

        //Fetch token only if we don't have it stored.
        if (tokenString == null) {

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setApplicationOnlyAuthEnabled(true);
            builder.setOAuthConsumerKey(context.getString(R.string.TWIT_CONS_KEY));
            builder.setOAuthConsumerSecret(context.getString(R.string.TWIT_CONS_SEC_KEY));

            token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

            SharedPreferences.Editor editor = sharedPreferences.edit();

            try {

                String encryptedMsg = AESCrypt.encrypt(Constants.ENCRYPTION_KEY, token.getAccessToken());
                editor.putString(Constants.SHARED_PREFS_TOKEN_KEY, encryptedMsg);
                editor.apply();

            } catch (GeneralSecurityException e) {
                Timber.e(e);
            }

        } else {
            //If we have the token stored used stored token. That way we won't have to fetch the token every time we make an API request.
            try {

                String messageAfterDecrypt = AESCrypt.decrypt(Constants.ENCRYPTION_KEY, tokenString);
                token = new OAuth2Token(Constants.TOKEN_TYPE, messageAfterDecrypt);

            } catch (GeneralSecurityException e) {
                Timber.e(e);
            }

        }
    }

    private void query(String queryString) throws TwitterException {

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setRestBaseURL(Constants.BASE_API_URL);
        builder.setHttpRetryCount(Constants.RETRY_COUNT);
        builder.setApplicationOnlyAuthEnabled(true);
        builder.setOAuthConsumerKey(context.getString(R.string.TWIT_CONS_KEY));
        builder.setOAuthConsumerSecret(context.getString(R.string.TWIT_CONS_SEC_KEY));
        builder.setOAuth2TokenType(token.getTokenType());
        builder.setOAuth2AccessToken(token.getAccessToken());

        Twitter twitter = new TwitterFactory(builder.build()).getInstance();

        Query query = new Query(queryString);
        query.setCount(Constants.NUMBER_OF_TWEETS_PER_QUERY);

        if (listener.getLastTweetId() != -1) {
            query.setMaxId(listener.getLastTweetId());
        }
        result = twitter.search(query);
    }

    private void parseData() {
        List<twitter4j.Status> tweets = result.getTweets();
        if (tweets != null) {
            this.tweets = new ArrayList<TweetModel>();
            for (twitter4j.Status tweet : tweets) {

                //If last tweet ID is -1 it means its our first fetch so we add all results.

                if (listener.getLastTweetId() == -1) {
                    this.tweets.add(new TweetModel(tweet.getId(), tweet.getUser().getScreenName(), tweet.getText(), tweet.getUser().getOriginalProfileImageURL()));

                    // This is neccesserry because the API parameter max_id returns results less or equal to the passed in tweet ID, meaning it will always have a duplicate per fetch unless it is filtered out here.

                } else if (listener.getLastTweetId() > tweet.getId()) {

                    this.tweets.add(new TweetModel(tweet.getId(), tweet.getUser().getScreenName(), tweet.getText(), tweet.getUser().getOriginalProfileImageURL()));
                }
            }
            if (tweets != null && tweets.size() != 0) {
                listener.setLastTweetId(tweets.get(tweets.size() - 1).getId());
            }
        }
    }

}

