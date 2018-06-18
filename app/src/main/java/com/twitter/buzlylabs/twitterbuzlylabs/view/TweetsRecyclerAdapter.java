package com.twitter.buzlylabs.twitterbuzlylabs.view;

/**
 * Created by Dimitar Spasov .
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.twitter.buzlylabs.twitterbuzlylabs.Constants;
import com.twitter.buzlylabs.twitterbuzlylabs.R;
import com.twitter.buzlylabs.twitterbuzlylabs.model.TweetModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TweetsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TweetModel> tweetList;
    private Context context;

    @BindView(R.id.txtTweet)
    TextView txtTweet;

    @BindView(R.id.txtTweetBy)
    TextView txtTweetBy;

    @BindView(R.id.avatar)
    ImageView imageView;

    public TweetsRecyclerAdapter(Context context) {

        this.tweetList = new ArrayList<TweetModel>();
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tweet_list_item, parent, false);

        ButterKnife.bind(this, view);

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((TweetViewHolder) holder).setTweetData(tweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {

        View view;

        public TweetViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public void setTweetData(final TweetModel tweetData) {

            txtTweet.setText(tweetData.getTweet());
            txtTweetBy.setText("@" + tweetData.getUserName());

            int avatarWidthPX = context.getResources().getDimensionPixelOffset(R.dimen.avatar_width);
            int avatarHeightPX = context.getResources().getDimensionPixelOffset(R.dimen.avatar_height);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.override(avatarWidthPX, avatarHeightPX).
                    timeout(Constants.GLIDE_TIMEOUT).
                    placeholder(R.drawable.placeholder).
                    circleCrop();

            Glide
                    .with(context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(tweetData.getProfileImageUrl())
                    .transition(withCrossFade(Constants.CORSSFADE_DURATION_IN_MS))
                    .into(imageView);

            setOnClickListener(view, tweetData);
        }
    }

    public void addItems(ArrayList<TweetModel> tweetList) {

        this.tweetList.addAll(tweetList);
        notifyDataSetChanged();

    }

    public void clearAdapter() {
        tweetList.clear();
        notifyDataSetChanged();
    }

    private void setOnClickListener(View view, TweetModel tweetData) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                WebView wv = new WebView(context);
                WebSettings webSettings = wv.getSettings();
                webSettings.setJavaScriptEnabled(true);
                dialog.setContentView(wv);

                String url = Constants.BASE_TWITTER_URL + tweetData.getUserName() + Constants.TWITTER_STATUS + tweetData.getTweetId();

                wv.loadUrl(url);

                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        dialog.show();
                    }

                });
            }
        });
    }

}
