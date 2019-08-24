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

    public MutableLiveData<List<User>> getUsers() {
//        mDataSet = this.mUserDao.getAll();
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(mDataSet);
        return data;
    }

    public MutableLiveData<List<User>> getFriendList (User user) {
        mDataSet = this.mUserDao.getAll();
        ArrayList<User> result = new ArrayList<>();
        for (User u : this.mUserDao.getAll()) {
            if (user.getFriendList().indexOf(u.getUsername()) != -1
                && !user.getUsername().equals(u.getUsername()))
                result.add(u);
        }

        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(result);
        return data;
    }

    public ArrayList<User> getFriendArrayList (User user) {
        mDataSet = this.mUserDao.getAll();
        ArrayList<User> result = new ArrayList<>();
        for (User u : this.mUserDao.getAll()) {
            if (user.getFriendList().indexOf(u.getUsername()) != -1
                    && !user.getUsername().equals(u.getUsername()))
                result.add(u);
        }

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

}
