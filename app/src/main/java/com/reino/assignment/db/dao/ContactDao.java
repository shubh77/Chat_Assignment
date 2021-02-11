package com.reino.assignment.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import androidx.paging.DataSource;

import com.reino.assignment.model.ContactModel;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ContactModel contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertList(List<ContactModel> contactList);

    @Query("select * from ContactDB order by contact_name asc")
    DataSource.Factory<Integer, ContactModel> getAllContacts();

    @Query("select * from ContactDB where contact_name like :query " +
            "or contact_numbers like :query order by contact_name asc")
    DataSource.Factory<Integer, ContactModel> getQueryContact(String query);
}
