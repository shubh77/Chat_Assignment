package com.reino.assignment.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.reino.assignment.db.dao.ContactDao;
import com.reino.assignment.db.dao.UserDao;
import com.reino.assignment.model.ContactModel;
import com.reino.assignment.model.UserModel;

@androidx.room.Database(entities = {UserModel.class, ContactModel.class}, version = 2)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract UserDao userDao();
    public abstract ContactDao contactDao();

    static final Migration MIGRATION_UPGRADE = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table 'UserDb' add 'timestamp' String ");
        }
    };

    public static synchronized Database getInstance(Context context){
        if (instance ==null){
            /*instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    Database.class,
                    "user_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();*/
            instance = Room.databaseBuilder(
                    context,
                    Database.class,
                    "User_Database")
                    .addMigrations(MIGRATION_UPGRADE)
                    .build();
        }
        return instance;
    }

    public static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
