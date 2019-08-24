package com.mosis.treasurehunt.models;

import android.media.Image;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int points;
    private List<Hunt> createdHunts;
    private List<Hunt> joinedHunts;
    // Array of usernames
    private List<User> friendList;
    private Location currentLocation;
    @Exclude
    private String key;
    @Exclude
    public Image profileImage;


    public User() {
        createdHunts = new ArrayList<>();
        joinedHunts = new ArrayList<>();
        friendList = new ArrayList<>();
    }

    public User(String firstName, String lastName, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.points = 0;
    }

    public User(String firstName, String lastName, String username, String password, int points) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.points = points;
    }

    public User(String firstName, String lastName, String username) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.points = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() { return String.format("%s %s", firstName, lastName); }

    public void setPoints(int points) { this.points = points; }

    public int getPoints() { return this.points; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() { return this.password; }

    public String getKey() { return this.key; }

    public void setKey(String key) { this.key = key; }

//    @Override
//    public String toString() { return String.format("%s %s", firstName, lastName); }

    public List<Hunt> getCreatedHunts() { return this.createdHunts; }

    public void addHunt(Hunt hunt) { this.createdHunts.add(hunt); }

    public List<Hunt> getJoinedHunst() { return this.joinedHunts; }

    public void joinHunt(Hunt hunt) { this.joinedHunts.add(hunt); }

    public int getNumOfCreatedHunts() { return  this.createdHunts.size(); }

    public int getNumOfJoinedHunts() { return this.joinedHunts.size(); }

   public List<User> getFriendList() { return this.friendList; }

   public void setCurrentLocation(Location loc) { this.currentLocation = loc; }

   public Location getCurrentLocation() { return this.currentLocation; }

   public void addFriend(User friend) {
        this.friendList.add(friend);
   }
}
