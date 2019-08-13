package com.mosis.treasurehunt.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.List;

public class FriendListActivityViewModel extends ViewModel {
    private MutableLiveData<List<User>> mFriendList;
    private UserRepository mUserRepo;
    private SharedPreferencesWrapper mSharedPrefWrapper;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init(Context context) {
        if(mFriendList != null) {
            return;
        }

        mUserRepo = UserRepository.getInstance();
        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();
        String username = mSharedPrefWrapper.getUsername();
        User user = mUserRepo.getUserByUsername(username);
        mFriendList = mUserRepo.getFriendList(user);
    }

    public void addNewValue(final User newFriend){
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                List<User> friendList = mFriendList.getValue();
                friendList.add(newFriend);
                mFriendList.postValue(friendList);
                mIsUpdating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public LiveData<List<User>> getFriendList() {
        return  mFriendList;
    }

    public LiveData<Boolean> getIsUpdating(){
        return mIsUpdating;
    }
}
