package com.mosis.treasurehunt.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mosis.treasurehunt.activities.LoginActivity;
import com.mosis.treasurehunt.data.UserDao;
import com.mosis.treasurehunt.models.User;

public class NetworkRequestTaskRegisterUser extends AsyncTask<Void, Void, Boolean> {
    // ne treba async task za ovo, ne blokira se UI nikakav prilikom registracije
    // remove
    private final User mNewUser;
    private UserDao mUserDao;
    Context context;

    public NetworkRequestTaskRegisterUser(User user, Context ctx) {
        this.mNewUser = user;
        this.mUserDao = new UserDao();
        this.context = ctx.getApplicationContext();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        this.mUserDao.save(this.mNewUser);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean b) {
       if(b) {
           if(this.mUserDao.getQuerySuccess() == true) {
               this.mUserDao.setQuerySuccess(false);
               Toast.makeText(this.context, "Successfully registered", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(this.context, LoginActivity.class));
           } else {
               Toast.makeText(this.context, "Failed to register new user", Toast.LENGTH_SHORT).show();
           }
       }
    }
}

