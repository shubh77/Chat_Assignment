package com.reino.assignment.view.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import com.reino.assignment.R;
import com.reino.assignment.model.UserModel;
import com.reino.assignment.viewmodel.AppViewModel;
import com.reino.assignment.utils.SaveBitmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEntryFragment extends Fragment implements View.OnClickListener {

    private CircleImageView iv_user;
    private EditText et_name, et_phone1, et_phone2, et_phone3, et_countryCode1, et_countryCode2, et_countryCode3;
    private TextView tvDob;
    private ImageView iv_cancel2, iv_cancel3;
    private LinearLayout ll_phone1, ll_phone2, ll_phone3;
    private Button btn_save;
    private AppViewModel appViewModel;
    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    String picturePath;
    private ArrayList<String> phoneList;
    private String countryCode = "+91";

    public CreateEntryFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEntryFragment newInstance() {
        CreateEntryFragment fragment = new CreateEntryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_entry, container, false);
        iv_user = view.findViewById(R.id.iv_user);
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
        tvDob = view.findViewById(R.id.tv_dob);
        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
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
                        tvDob.getText().toString());
                appViewModel.insert(userModel);
                Toast.makeText(getContext(), "Data has been inserted successfully.", Toast.LENGTH_SHORT).show();
                reset();
            }
            else {
                Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
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
        tvDob.setOnClickListener(v -> showCalendar());
        iv_user.setOnClickListener(v -> pictureOption());
        return view;
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
                tvDob.setText(date);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void pictureOption() {
        final CharSequence[] options = {getString(R.string.capture_pic),
                getString(R.string.choose_gallery), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.choose_method));

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals(getString(R.string.capture_pic))) {

                    checkPermissionAndStartCamera();

                } else if (options[item].equals(getString(R.string.choose_gallery))) {

                    checkPermissionAndOpenGallery();

                } else
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        else
            startOpenGalleryIntent();
    }

    private void startOpenGalleryIntent() {
        Intent intentPickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentPickPhoto, REQUEST_CODE_GALLERY);
    }

    private void startTakePictureIntent() {
        Intent intentTakePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePicture, REQUEST_CODE_CAMERA);
    }

    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(getContext(), "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    Bitmap bitmapCameraImage = (Bitmap) data.getExtras().get("data");
                    Uri cameraImageUri = null;
                    try {
                        Log.d("TAG", "Inside try of onActivity result of DataEntryFragment");
                        cameraImageUri = SaveBitmap.saveBitmapReturnUri(bitmapCameraImage);
                        Log.d("TAG", "cmeraUri: " + cameraImageUri.toString());
                        Log.d("TAG", "cameraUri: " + cameraImageUri.getPath());

                    } catch (IOException e) {
                        Log.d("TAG", "Inside catch: " + e.getMessage());
                        e.printStackTrace();
                    }
                    iv_user.setImageURI(cameraImageUri);

                    picturePath = cameraImageUri.toString();
                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    picturePath = selectedImageUri.toString();

                    iv_user.setImageURI(selectedImageUri);
                    break;
            }
        }
    }

    /**
     * This method is used for resetting the all inserted field.
     */
    private void reset() {
        picturePath = "";
        iv_user.setImageResource(R.drawable.ic_user);
        et_name.setText("");
        et_countryCode1.setText("");
        et_phone1.setText("");
        et_countryCode2.setText("");
        et_phone2.setText("");
        et_countryCode3.setText("");
        et_phone3.setText("");
        ll_phone2.setVisibility(View.GONE);
        ll_phone3.setVisibility(View.GONE);
        tvDob.setText("");
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