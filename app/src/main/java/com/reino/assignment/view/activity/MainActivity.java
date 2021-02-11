package com.reino.assignment.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

import com.reino.assignment.model.UserModel;
import com.reino.assignment.view.fragment.MainFragment;
import com.reino.assignment.R;
import com.reino.assignment.view.fragment.EditUserFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainContainer, MainFragment.newInstance(),TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void launchEditUser(UserModel user){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainContainer, EditUserFragment.newInstance(user),TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}