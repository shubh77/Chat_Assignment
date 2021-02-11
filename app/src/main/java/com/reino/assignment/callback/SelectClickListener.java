package com.reino.assignment.callback;

import android.view.View;

import com.reino.assignment.model.UserModel;

public interface SelectClickListener {
    void onItemClicked(View view, UserModel user);

    void onItemLongClicked(View view, UserModel user, int index);
}
