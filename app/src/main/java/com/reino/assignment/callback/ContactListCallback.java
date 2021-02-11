package com.reino.assignment.callback;

import com.reino.assignment.model.ContactModel;

import java.util.ArrayList;

public interface ContactListCallback {
    void getContactList(ArrayList<ContactModel> usersList);
}
