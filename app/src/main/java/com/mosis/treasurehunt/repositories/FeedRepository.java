package com.mosis.treasurehunt.repositories;

import com.mosis.treasurehunt.data.FeedDao;
import com.mosis.treasurehunt.models.Feed;

import java.util.ArrayList;

public class FeedRepository {
    private static FeedRepository instance;
    private ArrayList<Feed> mDataSet;
    private FeedDao mFeedDao;

    public static FeedRepository getInstance() {
        if (instance == null) {
            instance = new FeedRepository();
        }

        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new FeedRepository();
        }
    }

    private FeedRepository() {
        this.mFeedDao = new FeedDao();
        mDataSet = this.mFeedDao.getAll();
    }

    public void update() {
        if (instance != null) {
            mDataSet = this.mFeedDao.getAll();
        }
    }

    public ArrayList<Feed> getFeeds() {
        return this.mDataSet;
    }

    public void addFeed(Feed feed) {
        this.mFeedDao.save(feed);
    }

}
