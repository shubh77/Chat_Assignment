package com.reino.assignment.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.reino.assignment.R;
import com.reino.assignment.adapter.SectionsPagerAdapter;
import com.reino.assignment.viewmodel.AppViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private AppViewModel viewModel;
    private ImageView deleteUserIcon;
    private RelativeLayout initialLayout, searchLayout;
    private static final String TAG = "MainFragment";
    private ImageView searchIcon;
    private EditText searchEditText;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        viewModel.getIsMultiSelectOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: " + aBoolean);
                deleteUser(aBoolean);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getActivity().getSupportFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        deleteUserIcon = view.findViewById(R.id.deleteUserIcon);

        initialLayout = view.findViewById(R.id.initialLayout);
        searchLayout = view.findViewById(R.id.searchLayout);
        searchIcon = view.findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
            }
        });

        ImageView backIcon = view.findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
            }
        });

        searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.setInputSearchText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImageView clearText = view.findViewById(R.id.clearText);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.getText().clear();
            }
        });

        deleteUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(false);
                viewModel.setIsDeleteClick(true);
            }
        });

        viewPager.addOnPageChangeListener(this);
        return view;
    }

    private void deleteUser(boolean toDelete) {
        deleteUserIcon.setVisibility(toDelete ? View.VISIBLE : View.GONE);
        initialLayout.setVisibility(toDelete ? View.GONE : View.VISIBLE);
        searchLayout.setVisibility(View.GONE);
        searchEditText.getText().clear();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        deleteUser(false);
        searchIcon.setVisibility(position == 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}