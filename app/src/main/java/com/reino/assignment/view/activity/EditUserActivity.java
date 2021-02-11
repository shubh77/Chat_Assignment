package com.reino.assignment.view.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.reino.assignment.R;
import com.reino.assignment.model.UserModel;
import com.reino.assignment.viewmodel.AppViewModel;
import com.reino.assignment.utils.Constant;
import com.reino.assignment.utils.SaveBitmap;

import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;

public class EditUserActivity extends AppCompatActivity {

    private CircleImageView ivUser;
    private EditText edtName;
    private EditText edtDob;
    private EditText edtPhone;
    private int id = -1;
    private AppViewModel appViewModel;
    private Disposable disposable = null;
    private final int REQUEST_CODE_CAMERA = 0;
    private final int REQUEST_CODE_GALLERY = 1;
    String picturePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        ivUser = findViewById(R.id.iv_user);
        edtName = findViewById(R.id.et_name);
        edtDob = findViewById(R.id.et_dob);
        edtPhone = findViewById(R.id.et_phone);

        Intent intent = getIntent();

        if (intent.hasExtra(Constant.EXTRA_ID)) {
            setTitle("Edit Note");
            id = getIntent().getIntExtra(Constant.EXTRA_ID, -1);
            if (!intent.getStringExtra(Constant.EXTRA_PIC).equals("")) {
                picturePath = intent.getStringExtra(Constant.EXTRA_PIC);
                Uri uri = Uri.parse(picturePath);
                ivUser.setImageURI(uri);
            }
            edtName.setText(intent.getStringExtra(Constant.EXTRA_NAME));
            edtPhone.setText(intent.getStringExtra(Constant.EXTRA_PHONE));
            edtDob.setText(intent.getStringExtra(Constant.EXTRA_DOB));
        } else {
            setTitle("Add User");
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user:
                pictureOption();
                break;
            case R.id.et_dob:
                showCalendar();
                break;
            case R.id.btn_update:
                saveUser();
                break;
        }
    }

    /**
     * This method is used for choosing the Date, Month Year from Calendar picker.
     */
    private void showCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1; // As Jan starts from 0
                String date = dayOfMonth + "/" + month + "/" + year;
                edtDob.setText(date);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void saveUser() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String dob = edtDob.getText().toString();

        if (name.trim().isEmpty()) {
            edtName.setError("Empty Name");
            return;
        } else {
            UserModel user = new UserModel();
            user.setId(id);
            user.setPic(picturePath);
            user.setName(edtName.getText().toString());
//            user.setPhone(edtPhone.getText().toString());
            user.setDob(edtDob.getText().toString());

            appViewModel.update(user);
            finish();
            /*pageViewModel.update(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            Log.d("EditUser", "onSubscribe");
                        }

                        @Override
                        public void onComplete() {
                            Log.d("EditUser", "onComplete");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("EditUser", "onSubsconErrorribe");
                        }
                    });*/
                    /*.subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            finish();
                        }
                    });*/
//            finish();
        }

        /*Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_NAME, name);
        intent.putExtra(Constant.EXTRA_PHONE, phone);
        if (id != -1) {
            intent.putExtra(Constant.EXTRA_ID, id);
        }

        setResult(Activity.RESULT_OK, intent);
        finish();*/
    }

    private void pictureOption() {
        final CharSequence[] options = {getString(R.string.capture_pic),
                getString(R.string.choose_gallery), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_method));

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionAndStartCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
        else
            startTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startTakePictureIntent();
            else
                Toast.makeText(this, "Failed. Please grant camera permission", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                startOpenGalleryIntent();
            else
                Toast.makeText(this, "Failed. Please grant gallery permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    ivUser.setImageURI(cameraImageUri);

                    picturePath = cameraImageUri.toString();
                    break;

                case REQUEST_CODE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    Log.d("TAG", "URi: " + selectedImageUri.getPath());
                    picturePath = selectedImageUri.toString();

                    ivUser.setImageURI(selectedImageUri);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}
