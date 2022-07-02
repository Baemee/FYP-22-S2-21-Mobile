package com.example.fyp_22_s22_21_mobile;

import android.app.Application;
import com.example.fyp_22_s22_21_mobile.API.WebAPI;
import com.example.fyp_22_s22_21_mobile.API.API;

public class Model {

    private static Model sInstance = null;
    private final API mApi;

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    private User mUser;

    public static Model getInstance(Application application) {
        if (sInstance == null) {
            sInstance = new Model(application);
        }
        return sInstance;
    }

    private final Application mApplication;

    private Model(Application application) {
        mApplication = application;
        mApi = new WebAPI(mApplication);
    }

    public Application getmApplication() {
        return mApplication;
    }

    public void login(String username, String password) {
        mApi.login(username, password);

    }


}
