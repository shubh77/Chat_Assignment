package com.reino.assignment.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.reino.assignment.R;
import com.reino.assignment.adapter.ContactsRecyclerViewAdapter;
import com.reino.assignment.callback.ContactItemClickListener;
import com.reino.assignment.utils.ContactChangeListener;
import com.reino.assignment.callback.SearchUser;
import com.reino.assignment.model.ContactModel;
import com.reino.assignment.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment implements SearchUser, ContactItemClickListener {
    private static final String TAG = "ContactListFragment";
    private RecyclerView recyclerView;
    private ContactsRecyclerViewAdapter adapter;
    private AppViewModel viewModel;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Activity mActivity;
    private ProgressBar progressBar;
    private NavController navController;

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        viewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
        viewModel.getInputSearchText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String key) {
                key = "%" + key + "%";
                viewModel.queryContactListSearch(key);
                viewModel.contactList.observe(getViewLifecycleOwner(), new Observer<PagedList<ContactModel>>() {
                    @Override
                    public void onChanged(PagedList<ContactModel> contactModels) {
                        Log.d("ContactListFragment", " UpdateUI onChanged(): "+contactModels.size() + " ---- "+contactList.size());
                        adapter.submitList(contactModels);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    ContactChangeListener.IChangeListener contactChangeListener = new ContactChangeListener.IChangeListener() {
        @Override
        public void onContactChanged() {
            if (isAdded()) {
                Log.d("Observer_contact", "Thread onContactChanged: ");
                showContacts();
                updateUI(true);
            }
        }
    };

    private List<ContactModel> contactList = new ArrayList<>();
    public void updateUI(Boolean isClear) {
        Log.d("ContactListFragment", " UpdateUI called.");
        viewModel.contactList.observe(getViewLifecycleOwner(), new Observer<PagedList<ContactModel>>() {

            @Override
            public void onChanged(PagedList<ContactModel> contactModels) {
                contactList.addAll(contactModels);
                Log.d("ContactListFragment", " UpdateUI onChanged(): "+contactModels.size() + " ---- "+contactList.size());
                adapter.submitList(contactModels);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            ContactChangeListener.getInstance(getActivity()).startContactObservation(contactChangeListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.contacts_list);
        adapter = new ContactsRecyclerViewAdapter(getContext(), this::onItemClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(getContext().getDrawable(R.drawable.divider));
//        recyclerView.addItemDecoration(itemDecoration);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        showContacts();
        viewModel.queryContactList();
        /*updateUI(false);

        manageProgressBar();*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        updateUI(false);
        manageProgressBar();
    }

    /**
     * This method sets the ProgressBar visibility to GONE, once the data got fetched completely.
     */
    public void manageProgressBar() {
        viewModel.getIsProgress().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            progressBar.setVisibility(View.VISIBLE);
            viewModel.syncContacts();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(mActivity, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ContactChangeListener.getInstance(getActivity()).stopContactObservation();
    }

    @Override
    public void searchName(String name) {
        Log.d(TAG, "searchName: "+name);
    }

    @Override
    public void onItemClick(ContactModel contactModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact_details", contactModel);
        navController.navigate(R.id.action_mainFragment_to_singleContactViewFragment, bundle);
    }
}
