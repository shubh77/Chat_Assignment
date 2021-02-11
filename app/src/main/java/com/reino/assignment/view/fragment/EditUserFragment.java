package com.reino.assignment.view.fragment;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reino.assignment.R;
import com.reino.assignment.db.dao.ContactDao;
import com.reino.assignment.model.UserModel;
import com.reino.assignment.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserFragment extends Fragment implements View.OnClickListener {

    private CircleImageView iv_profile;
    private EditText et_name, et_phone1, et_phone2, et_phone3, et_countryCode1, et_countryCode2, et_countryCode3, et_dob;
    private ImageView iv_cancel2, iv_cancel3;
    private LinearLayout ll_phone1, ll_phone2, ll_phone3;

    private Button btn_update;
    private static final String TAG = "EditUserFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private UserModel user;
    private String picturePath = "";
    private String countryCode = "+91";
    private ArrayList<String> phoneList;
    private AppViewModel appViewModel;


    public EditUserFragment() {
        // Required empty public constructor
    }

    public static EditUserFragment newInstance(UserModel user) {
        EditUserFragment fragment = new EditUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
    }

    public void inflateUI(UserModel user) {
        String number="";
        String[] numbers;
        picturePath = user.getPic();
        if (picturePath != null && !picturePath.equals("")) {
            Uri uri = Uri.parse(user.getPic());
            iv_profile.setImageURI(uri);
        } else {
            picturePath = "";
            iv_profile.setImageResource(R.drawable.ic_user);
        }
        et_name.setText(user.getName());
        et_dob.setText(user.getDob());
        if (user.getPhone() != null) {
            number="";
                for (int pos = 0; pos < user.getPhone().size(); pos++) {
                    switch (pos) {
                        case 0:
                            ll_phone1.setVisibility(View.VISIBLE);
                            numbers = user.getPhone().get(pos).split("-");
                            if (numbers.length==2) {
                                et_countryCode1.setText(numbers[0]);
                                et_phone1.setText(numbers[1]);
                            }
                            break;
                        case 1:
                            ll_phone2.setVisibility(View.VISIBLE);
                            numbers = user.getPhone().get(pos).split("-");
                            if (numbers.length==2) {
                                et_countryCode2.setText(numbers[0]);
                                et_phone2.setText(numbers[1]);
                            }
                            break;
                        case 2:
                            ll_phone3.setVisibility(View.VISIBLE);
                            numbers = user.getPhone().get(pos).split("-");
                            if (numbers.length==2) {
                                et_countryCode3.setText(numbers[0]);
                                et_phone3.setText(numbers[1]);
                            }
                            break;
                    }
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        ImageView backIcon = view.findViewById(R.id.backIcon);
        iv_profile = view.findViewById(R.id.iv_user);
        et_name = view.findViewById(R.id.et_name);
        ll_phone1 = view.findViewById(R.id.ll_phone_1);
        ll_phone2 = view.findViewById(R.id.ll_phone_2);
        ll_phone3 = view.findViewById(R.id.ll_phone_3);
        et_phone1 = view.findViewById(R.id.et_phone_1);
        et_phone2 = view.findViewById(R.id.et_phone_2);
        et_phone3 = view.findViewById(R.id.et_phone_3);
        et_countryCode1 = view.findViewById(R.id.et_country_code_2);
        et_countryCode2 = view.findViewById(R.id.et_country_code_2);
        et_countryCode3 = view.findViewById(R.id.et_country_code_3);
        iv_cancel2 = view.findViewById(R.id.iv_cancel_2);
        iv_cancel3 = view.findViewById(R.id.iv_cancel_3);

        et_dob = view.findViewById(R.id.et_dob);
        btn_update = view.findViewById(R.id.btn_update);

        enableOrDisableView(false);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        ImageView editUserIcon = view.findViewById(R.id.editUserIcon);
        editUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableOrDisableView(true);
            }
        });

        Button btn_update = view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();

                if (!et_name.getText().toString().equals("")) {
                    phoneList = new ArrayList<>();
                    countryCode = "+91";
                    if (!et_countryCode1.getText().toString().equals(""))
                        countryCode = et_countryCode1.getText().toString();
                    phoneList.add(countryCode + "-" + et_phone1.getText().toString());
                    if (ll_phone2.getVisibility() == View.VISIBLE && !et_phone2.getText().toString().equals("")) {
                        countryCode = "+91";
                        if (!et_countryCode2.getText().toString().equals(""))
                            countryCode = et_countryCode2.getText().toString();
                        phoneList.add(countryCode + "-" + et_phone2.getText().toString());
                    }
                    if (ll_phone3.getVisibility() == View.VISIBLE && !et_phone3.getText().toString().equals("")) {
                        countryCode = "+91";
                        if (!et_countryCode3.getText().toString().equals(""))
                            countryCode = et_countryCode3.getText().toString();
                        phoneList.add(countryCode + "-" + et_phone3.getText().toString());
                    }
                    UserModel userModel = new UserModel(picturePath, et_name.getText().toString(),
                            phoneList,
                            et_dob.getText().toString());
                    appViewModel.update(userModel);
                    enableOrDisableView(false);
                }
                else {
                    Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ll_phone2.getVisibility() == View.GONE)
                    ll_phone2.setVisibility(View.VISIBLE);
            }
        });
        et_phone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ll_phone3.getVisibility() == View.GONE)
                    ll_phone3.setVisibility(View.VISIBLE);
            }
        });
        iv_cancel2.setOnClickListener(this);
        iv_cancel3.setOnClickListener(this);
        et_dob.setOnClickListener(v -> showCalendar());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            user = (UserModel) getArguments().getSerializable(ARG_PARAM1);
            inflateUI(user);
            Log.d(TAG, "onCreate: "+user.toString());
        }
    }

    /**
     * This method is used for choosing the Date, Month Year from Calendar picker.
     */
    private void showCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1; // As Jan starts from 0
                String date = dayOfMonth + "/" + month + "/" + year;
                et_dob.setText(date);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void enableOrDisableView(boolean enable){
        iv_profile.setEnabled(enable);
        et_name.setEnabled(enable);
        et_countryCode1.setEnabled(enable);
        et_countryCode2.setEnabled(enable);
        et_countryCode3.setEnabled(enable);
        et_phone1.setEnabled(enable);
        et_phone2.setEnabled(enable);
        et_phone3.setEnabled(enable);
        et_dob.setEnabled(enable);
        btn_update.setEnabled(enable);
    }

    /**
     * This method is used for resetting the all inserted field.
     */
    private void reset() {
        picturePath = "";
        iv_profile.setImageResource(R.drawable.ic_user);
        et_name.setText("");
        et_countryCode1.setText("");
        et_phone1.setText("");
        et_countryCode2.setText("");
        et_phone2.setText("");
        et_countryCode3.setText("");
        et_phone3.setText("");
        ll_phone2.setVisibility(View.GONE);
        ll_phone3.setVisibility(View.GONE);
        et_dob.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel_2:
                et_countryCode2.setText("");
                et_phone2.setText("");
                ll_phone2.setVisibility(View.GONE);
                break;

            case R.id.iv_cancel_3:
                et_countryCode3.setText("");
                et_phone3.setText("");
                ll_phone3.setVisibility(View.GONE);
                break;
        }
    }
}