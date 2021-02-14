package com.reino.assignment.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reino.assignment.R;
import com.reino.assignment.adapter.UserAdapter;
import com.reino.assignment.callback.EditDeleteClickListener;
import com.reino.assignment.callback.SearchUser;
import com.reino.assignment.callback.SelectClickListener;
import com.reino.assignment.model.UserModel;
import com.reino.assignment.view.activity.MainActivity;
import com.reino.assignment.viewmodel.AppViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowListFragment extends Fragment implements SearchUser, EditDeleteClickListener, SelectClickListener {
    public AppViewModel appViewModel;
    private static final String TAG = "ShowListFragment";
    private RecyclerView rv_list;
    private UserAdapter adapter;
    ArrayList<UserModel> deleteUserList;
    private View view;

    boolean multiSelectStatus = false;
    private NavController navController;

    public ShowListFragment() {
        // Required empty public constructor
    }

    public static ShowListFragment newInstance() {
        ShowListFragment fragment = new ShowListFragment();
        return fragment;
    }

    private EditDeleteClickListener mListener = this::click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deleteUserList = new ArrayList<>();
        adapter = new UserAdapter(getContext(), mListener, this);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
        appViewModel.getUsersList();
        appViewModel.getInputSearchText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String key) {
                key = "%" + key + "%";
                appViewModel.fetchUsersOnQuery(key);
                updateUI();     //To get the updated Filtered list.
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    /**
     * This method is used for fetching the updated List based on Either filtered or whole list.
     */
    public void updateUI() {
        appViewModel.usersList.observe(getViewLifecycleOwner(), new Observer<PagedList<UserModel>>() {
            @Override
            public void onChanged(PagedList<UserModel> userModels) {
                adapter.submitList(userModels);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_show_list, container, false);
        }
        rv_list = view.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rv_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateUI();
        observeMultiSelectStatus();
        deleteSelectedUsers();
        return view;
    }

    @Override
    public void searchName(String name) {
        Log.d(TAG, "searchName: " + name);
    }


    private void observeMultiSelectStatus() {
        appViewModel.getIsMultiSelectOn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                multiSelectStatus = aBoolean;
            }
        });
    }

    public void deleteSelectedUsers() {
        appViewModel.getIsDeleteClick().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                for (int pos = 0; pos < deleteUserList.size(); pos++) {
                    appViewModel.delete(deleteUserList.get(pos));
                }
                deleteUserList.clear();
                appViewModel.setIsMultiSelect(false);
            }
        });
    }

    @Override
    public void click(boolean isDelete, UserModel user) {
        if (isDelete) {
            appViewModel.delete(user);
        }
    }

    @Override
    public void onItemClicked(View view, UserModel user) {
        Log.d("TAG", "itemclicked: multi");

        if (multiSelectStatus) {
            if (!deleteUserList.contains(user)) {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white_sky));
                deleteUserList.add(user);
            } else {
                deleteUserList.remove(user);
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
            }
            if (deleteUserList.isEmpty()) {
                appViewModel.setIsMultiSelect(false);
            }
        } else {
//            ((MainActivity)getActivity()).launchEditUser(user);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_details", user);
            navController.navigate(R.id.action_mainFragment_to_editUserFragment, bundle);
        }

    }

    @Override
    public void onItemLongClicked(View view, UserModel user, int index) {
        Log.d("TAG", "index: " + index + "Name: " + user.getName());

        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white_sky));
        deleteUserList.add(user);
        Log.d("TAG", "LongItemClick: " + index);
        appViewModel.setIsMultiSelect(true);
    }
}