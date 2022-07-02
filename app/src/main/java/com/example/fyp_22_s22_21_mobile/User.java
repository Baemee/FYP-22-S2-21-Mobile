package com.example.fyp_22_s22_21_mobile;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public static  User getUser(JSONObject jsonObject) throws JSONException {

        String userId = jsonObject.getString("userId");
        String username = jsonObject.getString("username");
        String token = jsonObject.getString("token");
        User user = new User(userId, username, token);

        return user;
    }

    private String userId;
    private String username;
    private String token;

    public User(String userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String userId) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj != null && obj instanceof  User) {
            User that = (User) obj;
            if(this.userId.equalsIgnoreCase((that.userId))) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.userId + "(" + this.username + ")";
    }
}
