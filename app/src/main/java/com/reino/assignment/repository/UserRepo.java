package com.reino.assignment.repository;

import android.content.Context;

import androidx.paging.DataSource;

import com.reino.assignment.db.dao.UserDao;
import com.reino.assignment.db.Database;
import com.reino.assignment.model.UserModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class UserRepo {

    private UserDao userDao;
    private Flowable<List<UserModel>> allUsers;

    public UserRepo(Context context){
        userDao = Database.getInstance(context).userDao();
    }

    /**
     * This method is used for inserting data into DB.
     * @param user, contains actual User - info.
     */
    public Completable insert(UserModel user) {
        return userDao.insertUser(user);
    }

    public DataSource.Factory<Integer, UserModel> fetchAllUsers() {
        return userDao.fetchAllUsers();
    }
    public DataSource.Factory<Integer, UserModel> fetchUsersOnQuery(String query) {
        return userDao.fetchUsersOnQuery(query);
    }
    public Flowable<List<UserModel>> getAllUsers(){
        return allUsers;
    }

    /**
     * This method is used for updating the same record.
     * @param user, with this new User Object data.
     * @return
     */
    public Completable update(UserModel user){
        return userDao.updateUser(user);
    }

    /**
     * This method is used for deleting a single User record.
     * @param user, based on this data.
     * @return
     */
    public Completable delete(UserModel user){
        return userDao.deleteUser(user);
    }

    /**
     * This method is used for deleting whole User record.
     * @return
     */
    public Completable deleteAllUsers(){
        return userDao.deleteAllUser();
    }
}
