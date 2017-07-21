package com.peoplehero.mauriciomartins.peoplehero.ViewModel;

import android.arch.lifecycle.ViewModel;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;

/**
 * Created by mauriciomartins on 20/07/17.
 */

public class UserProfileViewModel extends ViewModel {
    private String userId;
    private User user;

    public void init(String userId) {
        this.userId = userId;
    }
    public User getUser() {
        return user;
    }
}
