package com.mosis.treasurehunt.repositories;

import com.mosis.treasurehunt.data.HuntDao;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;

public class HuntRepository {
    private static HuntRepository instance;
    private ArrayList<Hunt> mDataSet;
    private HuntDao mHuntDao;

    public static HuntRepository getInstance() {
        if (instance == null) {
            instance = new HuntRepository();
        }

        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new HuntRepository();
        }
    }

    private HuntRepository() {
        this.mHuntDao = new HuntDao();
        mDataSet = this.mHuntDao.getAll();
    }

    public void update() {
        if (instance != null) {
            mDataSet = this.mHuntDao.getAll();
        }
    }

    public ArrayList<Hunt> getUserHunts(User owner) {
        ArrayList<Hunt> result = new ArrayList<>();
        for (Hunt hunt : this.mDataSet) {
            if (hunt.getOwner() == owner) {
                result.add(hunt);
            }
        }

        return result;
    }

    public void addHunt(Hunt hunt) {
        this.mHuntDao.save(hunt);
    }

    public void updateHunt(Hunt updatedHunt, User owner) {
        for (Hunt hunt : this.mDataSet) {
            if (hunt.getOwner() == owner && hunt.getTitle().equals(updatedHunt.getTitle())) {
                this.mDataSet.remove(hunt);
                this.mDataSet.add(updatedHunt);
                this.mHuntDao.update(updatedHunt);

                break;
            }
        }
    }

}
