package com.twitter.buzlylabs.twitterbuzlylabs.model;

/**
 * Created by Dimitar Spasov .
 */

public class TweetModel {

    private long tweetId;
    private String userName;
    private String tweet;
    private String profileImageUrl;

    public TweetModel(long tweetId, String userName, String tweet, String profileImageUrl) {

        this.tweetId = tweetId;
        this.userName = userName;
        this.tweet = tweet;
        this.profileImageUrl = profileImageUrl;

    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {

        this.profileImageUrl = profileImageUrl;

    }

}
