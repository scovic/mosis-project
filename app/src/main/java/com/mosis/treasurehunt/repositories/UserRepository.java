package com.mosis.treasurehunt.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.mosis.treasurehunt.data.UserDao;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.lang.reflect.Array;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;
    private ArrayList<User> mDataSet = new ArrayList<>();
    private UserDao mUserDao;
    private SharedPreferencesWrapper mSharedPref;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return  instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new UserRepository();
        }
    }

    private UserRepository() {
        this.mUserDao = new UserDao();
        mDataSet = this.mUserDao.getAll();
    }

    /**
     * Fetches updated user list from server.
     **/
    public void update () {
        if (instance != null) {
            mDataSet = this.mUserDao.getAll();
        }
    }

    /**
     * Fetches updated user list from server,
     * and also updates friend list of the user.
     **/
    public void update (User user) {
        if (instance != null) {
            mDataSet = this.mUserDao.getAll();
            List<User> updatedUserList = new ArrayList<>();
            for (User u : user.getFriendList()) {
                updatedUserList.add(this.getUserByUsername(u.getUsername()));
            }

            user.setFriendList(updatedUserList);
            this.updateUser(user);

        }
    }

    public MutableLiveData<List<User>> getUsers() {
//        mDataSet = this.mUserDao.getAll();
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(mDataSet);
        return data;
    }

    public MutableLiveData<List<User>> getFriendList (final User user) {
        List result = user.getFriendList();

        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(result);
        return data;
    }

    public List<User> getFriendArrayList (User user) {
        mDataSet = this.mUserDao.getAll();
        List<User> result = user.getFriendList();


//        MutableLiveData<List<User>> data = new MutableLiveData<>();
//        data.setValue(result);
        return result;
    }

    public void updateUser(User user) {
        String username = user.getUsername();
        for (User u: this.mDataSet) {
            if (username.equals(u.getUsername())) {
                mDataSet.remove(u);
                mDataSet.add(user);
                mUserDao.update(user);
                break;
            }
        }
    }

    public void addHunt(User user, Hunt hunt) {
        String username = user.getUsername();
        for (User u : this.mDataSet) {
            if (u.getUsername().equals(username)) {
                u.addHunt(hunt);
                mUserDao.update(u);
                break;
            }
        }
    }

    public void addFriend (User friend) {
        mSharedPref = SharedPreferencesWrapper.getInstance();
        String username = mSharedPref.getUsername();
        for (User u : this.mDataSet) {
            if (u.getUsername().equals(username)) {
                u.addFriend(friend);
                mUserDao.update(u);
                break;
            }
        }
    }


    public User getFriend(User user, int position) {
        return  user.getFriend(position);
    }


    public ArrayList<User> getUsers1 () {
        return mUserDao.getAll();
    }

    @Nullable
    public User getUserByUsername (String username) {
        User user = null;
        for (User u : mDataSet) {
            if (u.getUsername().equals(username)) {
                user = u;
                break;
            }
        }
        return user;
    }

    public List<Hunt> getCreatedHunts(User user) {
        List<Hunt> createdHunts = null;
        for (User u : mDataSet) {
            if (u.getUsername().equals(user.getUsername())) {
                createdHunts = u.getCreatedHunts();
                break;
            }
        }
        return createdHunts;
    }

    public List<Hunt> getActiveHunts(User user) {
        List<Hunt> activeHunts = null;
        List<Hunt> joinedHunts = null;
        for (User u : mDataSet) {
            if (u.getUsername().equals(user.getUsername())) {
                joinedHunts = u.getJoinedHunst();
                for(Hunt joinedHunt : joinedHunts) {
                    if (!joinedHunt.checkCompleted()) {
                        activeHunts.add(joinedHunt);
                    }
                }
                break;
            }
        }
        return  activeHunts;
    }

    public List<Hunt> getCompletedHunts(User user) {
        List<Hunt> completedHunts = null;
        List<Hunt> joinedHunts = null;
        for (User u : mDataSet) {
            if (u.getUsername().equals(user.getUsername())) {
                joinedHunts = u.getJoinedHunst();
                for(Hunt joinedHunt : joinedHunts) {
                    if (joinedHunt.checkCompleted()) {
                        completedHunts.add(joinedHunt);
                    }
                }
                break;
            }
        }
        return  completedHunts;
    }

}
