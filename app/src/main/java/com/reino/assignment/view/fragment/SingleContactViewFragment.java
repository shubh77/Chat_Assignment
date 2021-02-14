package com.reino.assignment.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reino.assignment.R;
import com.reino.assignment.adapter.SingleContactAdapter;
import com.reino.assignment.model.ContactModel;
import com.reino.assignment.model.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleContactViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleContactViewFragment extends Fragment {

    private static final String TAG = "SingleContactViewFragment";
    private NavController navController;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CircleImageView ivUser;
    private ImageView backIcon;
    private TextView tvName, tvTitle;
    private RecyclerView rvContact;
    private SingleContactAdapter adapter;

    public SingleContactViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleContactViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleContactViewFragment newInstance(String param1, String param2) {
        SingleContactViewFragment fragment = new SingleContactViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_contact_view, container, false);
        ivUser = view.findViewById(R.id.iv_user);
        tvName = view.findViewById(R.id.tv_name);
        backIcon = view.findViewById(R.id.backIcon);
        tvTitle = view.findViewById(R.id.tv_title);
        rvContact = view.findViewById(R.id.rv_contact);
        rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContact.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvContact.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        tvTitle.setText("Contact Details");
        if (getArguments() != null) {
            ContactModel contactModel = (ContactModel) getArguments().getSerializable("contact_details");
            tvName.setText(contactModel.getName());
            adapter = new SingleContactAdapter(contactModel);
            rvContact.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getSupportFragmentManager().popBackStackImmediate();
                navController.navigateUp();
            }
        });
    }
}