package com.twitter.buzlylabs.twitterbuzlylabs;

/**
 * Created by Dimitar Spasov .
 */

public class Constants {

    public static final String BASE_API_URL = "https://api.twitter.com/1.1/";
    public static final String BASE_TWITTER_URL = "https://twitter.com/";
    public static final String TWITTER_STATUS = "/status/";

    public static final int NUMBER_OF_TWEETS_PER_QUERY = 20;
    public static final int CORSSFADE_DURATION_IN_MS = 200;
    public static final int GLIDE_TIMEOUT = 500;
    public static final int RETRY_COUNT = 5;

    public static final String SHARED_PREFS_FILE = "SHARED_PREFS_TWITTER_BUZLY";
    public static final String SHARED_PREFS_TOKEN_KEY = "SHARED_PREFS_TOKEN_KEY";
    public static final String TOKEN_TYPE = "bearer";

    //NOTE this should be obtained from a backend! For the purpose of the exercise they key is defined here.
    public static final String ENCRYPTION_KEY = "YKr2wXR53#YW9F8E+1X>L1%T";

}
