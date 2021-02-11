package com.reino.assignment.db.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.reino.assignment.model.UserModel;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface UserDao {

    @Insert
    Completable insertUser(UserModel user);

    @Update
    Completable updateUser(UserModel user);

    @Delete
    Completable deleteUser(UserModel user);

    @Query("DELETE FROM UserDB")
    Completable deleteAllUser();

    @Query("SELECT * FROM UserDB ORDER BY id DESC")
    DataSource.Factory<Integer, UserModel> fetchAllUsers();

    @Query("select * from UserDB where user_name like :query " +
            "or phone_number like :query order by user_name asc")
    DataSource.Factory<Integer, UserModel> fetchUsersOnQuery(String query);
}
