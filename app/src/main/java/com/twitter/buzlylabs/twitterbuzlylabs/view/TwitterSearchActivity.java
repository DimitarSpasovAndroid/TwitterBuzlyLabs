package com.twitter.buzlylabs.twitterbuzlylabs.view;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.twitter.buzlylabs.twitterbuzlylabs.R;
import com.twitter.buzlylabs.twitterbuzlylabs.model.TweetModel;
import com.twitter.buzlylabs.twitterbuzlylabs.network.NetworkServiceImpl;
import com.twitter.buzlylabs.twitterbuzlylabs.presenter.TweetsPresenter;
import com.twitter.buzlylabs.twitterbuzlylabs.presenter.TweetsPresenterImpl;

/**
 * Created by Dimitar Spasov .
 */

public class TwitterSearchActivity extends AppCompatActivity implements TweetsView {

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    String searchText;

    TweetsRecyclerAdapter tweetsAdapter;

    private long lastTweetId = -1;

    TweetsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        presenter = new TweetsPresenterImpl(this, new NetworkServiceImpl());

        initUI();

    }

    @Override
    protected void onStop() {

        super.onStop();
        presenter.onStop();

    }

    @Override
    public void setTweets(ArrayList<TweetModel> tweets) {
        tweetsAdapter.addItems(tweets);
    }

    @Override
    public void setLastTweetId(long id) {
        this.lastTweetId = id;
    }

    public long getLastTweetId() {
        return lastTweetId;
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initUI() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                TwitterSearchActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        );

        recyclerView.setLayoutManager(linearLayoutManager);
        tweetsAdapter = new TweetsRecyclerAdapter(TwitterSearchActivity.this);
        recyclerView.setAdapter(tweetsAdapter);

        final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    presenter.queryTweets(searchText);
                }
            }

        };

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                recyclerView.removeOnScrollListener(scrollListener);
                tweetsAdapter.clearAdapter();
                return true;
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                TwitterSearchActivity.this.searchText = s;
                recyclerView.addOnScrollListener(scrollListener);
                tweetsAdapter = new TweetsRecyclerAdapter(TwitterSearchActivity.this);
                recyclerView.setAdapter(tweetsAdapter);
                presenter.queryTweets(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });

    }
}

