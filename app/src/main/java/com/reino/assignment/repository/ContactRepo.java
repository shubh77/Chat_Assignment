package com.reino.assignment.repository;

import android.content.Context;

import androidx.paging.DataSource;

import com.reino.assignment.db.dao.ContactDao;
import com.reino.assignment.db.Database;
import com.reino.assignment.model.ContactModel;

import java.util.List;

import io.reactivex.Completable;

public class ContactRepo {

    private ContactDao contactDao;
    public ContactRepo(Context context) {
        contactDao = Database.getInstance(context).contactDao();
    }

    public Completable insertContactList(List<ContactModel> contactList) {
        return contactDao.insertList(contactList);
    }

    public DataSource.Factory<Integer, ContactModel> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public DataSource.Factory<Integer, ContactModel> getQueryContact(String query) {
        return contactDao.getQueryContact(query);
    }

}
