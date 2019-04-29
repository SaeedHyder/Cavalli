package com.application.cavalliclub.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.application.cavalliclub.R;
import com.application.cavalliclub.activities.MainActivity;
import com.application.cavalliclub.entities.ResponseWrapper;
import com.application.cavalliclub.entities.SignUpEntity;
import com.application.cavalliclub.entities.UpdateProfileEntity;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.CameraHelper;
import com.application.cavalliclub.helpers.DatePickerHelper;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilefragment extends BaseFragment implements View.OnClickListener, MainActivity.ImageSetter {

    @BindView(R.id.iv_profile_photo)
    ImageView ivProfilePhoto;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.edt_email)
    AnyEditTextView edtEmail;
    @BindView(R.id.edt_phone_number)
    AnyEditTextView edtPhoneNumber;
    @BindView(R.id.btn_save_changes)
    Button btnSaveChanges;
    Unbinder unbinder;
    @BindView(R.id.imgCamera)
    ImageView imgCamera;
    @BindView(R.id.imgLayout)
    RelativeLayout imgLayout;
    public File profilePic;
    @BindView(R.id.tv_dob)
    AnyTextView tvDob;
    @BindView(R.id.sp_gender)
    Spinner spGender;
    private ArrayList<String> genderList = new ArrayList<>();
    private Date DateSelected;
    String NewDate;
    String splittedDate;
    int Day;
    int Month;
    int Year;

    public static EditProfilefragment newInstance() {
        Bundle args = new Bundle();

        EditProfilefragment fragment = new EditProfilefragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        getMainActivity().setImageSetter(this);
        setGenderData();
        setProfileData();
        edtEmail.setEnabled(false);
        edtPhoneNumber.setEnabled(false);
        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFromPickerValidated(tvDob);
            }
        });

        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper.uploadPhotoDialog(getMainActivity());
            }
        });
    }

    private void setProfileData() {

        if (prefHelper.getUpdatedUser() != null) {
            if (prefHelper.getUpdatedUser().getImageUrl() != null)
                ImageLoader.getInstance().displayImage(prefHelper.getUpdatedUser().getImageUrl(), ivProfilePhoto, getMainActivity().getImageLoaderRoundCornerTransformation(10));
            if (prefHelper.getUpdatedUser().getUserName() != null)
                edtName.setText(prefHelper.getUpdatedUser().getUserName() + "");
            if (prefHelper.getUpdatedUser().getEmail() != null)
                edtEmail.setText(prefHelper.getUpdatedUser().getEmail() + "");
            if (prefHelper.getUpdatedUser().getPhone() != null)
                edtPhoneNumber.setText(prefHelper.getSignUpUser().getPhone() + "");
            if (prefHelper.getUpdatedUser().getDob() != null)
                tvDob.setText(prefHelper.getUpdatedUser().getDob() + "");
            if (prefHelper.getUpdatedUser().getGender() != null) {
                if (prefHelper.getUpdatedUser().getGender().equalsIgnoreCase("Male")) {
                    spGender.setSelection(1);
                } else
                    spGender.setSelection(2);
            }

        } else if (prefHelper.getSignUpUser() != null) {
            if (prefHelper.getSignUpUser().getImageUrl() != null)
                ImageLoader.getInstance().displayImage(prefHelper.getSignUpUser().getImageUrl(), ivProfilePhoto);
            if (prefHelper.getSignUpUser().getUserName() != null)
                edtName.setText(prefHelper.getSignUpUser().getUserName() + "");
            if (prefHelper.getSignUpUser().getEmail() != null)
                edtEmail.setText(prefHelper.getSignUpUser().getEmail() + "");
            if (prefHelper.getSignUpUser().getPhone() != null)
                edtPhoneNumber.setText(prefHelper.getSignUpUser().getPhone() + "");
            if (prefHelper.getSignUpUser().getDob() != null)
                tvDob.setText(prefHelper.getSignUpUser().getDob() + "");
            if (prefHelper.getSignUpUser().getGender() != null) {
                if (prefHelper.getSignUpUser().getGender().equalsIgnoreCase("Male")) {
                    spGender.setSelection(1);
                } else if (prefHelper.getSignUpUser().getGender().equalsIgnoreCase("Female"))
                    spGender.setSelection(2);
                else
                    spGender.setSelection(0);
            }
        }
    }

    private void setGenderData() {

        genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, genderList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
        spGender.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initFromPickerValidated(final AnyTextView textView) {

        NewDate = tvDob.getText().toString();

        String str = NewDate;
        String[] splitedNewDate = str.split("-");
        Year = Integer.parseInt(splitedNewDate[0]);
        Month = (Integer.parseInt(splitedNewDate[1]) - 1);
        Day = Integer.parseInt(splitedNewDate[2]);

        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialogMin(
                getDockActivity(),
                Year,
                Month,
                Day
                , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

/*                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.set(Year, year);
                        c.set(Month, month);
                        c.set(Day, dayOfMonth);*/

                        // and get that as a Date
                        Date dateSpecified = c.getTime();
                        /*if (dateSpecified.after(date)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "Please enter valid date.");
                        } else {*/
                        DateSelected = dateSpecified;
                        String predate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

                        String[] splited = predate.split("\\s");
                        splittedDate = splited[0];

                        textView.setText(splittedDate);
                        textView.setPaintFlags(Typeface.BOLD);
                        //}
                    }
                }, "PreferredDate", new Date());
        datePickerHelper.showDate();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
        //titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.edit_profile));
    }

    @OnClick({R.id.imgCamera, R.id.btn_save_changes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCamera:
                requestCameraPermission();

                break;
            case R.id.iv_profile_photo:
                //CameraHelper.uploadPhotoDialog(getMainActivity());
                break;
            case R.id.btn_save_changes:
                if (validate())
                    if (edtName.getText().toString().length() < 3) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.enter_correct_name));
                    } else if (edtPhoneNumber.getText().toString().length() < 6) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.correct_phone_number));
                    } else if (tvDob.getText().toString().equalsIgnoreCase(getString(R.string.date_of_birth))) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.please_select_date_birth));
                    } else if (spGender.getSelectedItemPosition() == 0) {
                        UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_gender));
                    } else {
                        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                            updateProfile();
                    }
                break;
        }
    }

    private boolean validate() {
        return edtName.testValidity() && edtEmail.testValidity() && edtPhoneNumber.testValidity();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setImage(String imagePath) {
        if (imagePath != null) {
            profilePic = new File(imagePath);
            Picasso.with(getDockActivity()).load("file:///" + imagePath).placeholder(R.drawable.placeholder_banner).into(ivProfilePhoto);
         //   ImageLoader.getInstance().displayImage("file:///" + imagePath, ivProfilePhoto);
        }
    }

    @Override
    public void setFilePath(String filePath) {
    }

    @Override
    public void setVideo(String videoPath) {
    }

    private void updateProfile() {

        MultipartBody.Part filePart = null;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_image", profilePic.getName(), RequestBody.create(MediaType.parse("image"), profilePic));
        }

        loadingStarted();
        Call<ResponseWrapper<UpdateProfileEntity>> call = webService.updateProfile(
                RequestBody.create(MediaType.parse("text/plain"), edtName.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), edtEmail.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), edtPhoneNumber.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), tvDob.getText().toString() + ""),
                RequestBody.create(MediaType.parse("text/plain"), spGender.getSelectedItem().toString() + ""),
                filePart != null ? filePart : null);

        call.enqueue(new Callback<ResponseWrapper<UpdateProfileEntity>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UpdateProfileEntity>> call, Response<ResponseWrapper<UpdateProfileEntity>> response) {
                loadingFinished();

                if (response.body().getCode().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUpdatedUser(response.body().getResult());
                    UIHelper.showLongToastInCenter(getDockActivity(), "Profile updated successfully!");

                   // android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
//
                 //  if(currentFragment.isVisible()){
                       if (getMainActivity() != null)
                           getMainActivity().popFragment();
                 //  }



/*
                    ((Animatable) check.getDrawable()).start();

                    check.postDelayed(new Runnable() {
                        public void run() {
                            check.setVisibility(View.GONE);
                        }
                    }, 3000);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            ((Animatable) check.getDrawable()).stop();
                        }
                    }, 3);
*/


                } else {
                    UIHelper.showLongToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UpdateProfileEntity>> call, Throwable t) {
                loadingFinished();
                t.printStackTrace();
                //Log.e(ServiceHelper.class.getSimpleName()+" by tag: " + tag, t.toString());
            }
        });
    }

    private RequestBody getBody() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        MultipartBody.Part filePart = null;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_image", profilePic.getName(),
                    RequestBody.create(MediaType.parse("image"), profilePic));
        }
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("full_name", name)
                .addFormDataPart("email", email)
                .addFormDataPart("phone_no", phoneNumber);
        if (filePart != null) {
            body.addPart(filePart);
        }
        return body.build();
    }

    private void updateProfile(RequestBody body) {
        loadingStarted();
        Call<ResponseWrapper<SignUpEntity>> call = webService.updateProfileWithBody(prefHelper.getSignUpUser().getToken(), body);
        call.enqueue(new Callback<ResponseWrapper<SignUpEntity>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<SignUpEntity>> call, Response<ResponseWrapper<SignUpEntity>> response) {
                loadingFinished();

                if (response.body().getCode().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    //prefHelper.putSignupUser(response.body().getResult());
                    UIHelper.showLongToastInCenter(getDockActivity(), "Profile updated successfully!");
                } else {
                    UIHelper.showLongToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<SignUpEntity>> call, Throwable t) {
                loadingFinished();
                t.printStackTrace();
                //Log.e(ServiceHelper.class.getSimpleName()+" by tag: " + tag, t.toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void requestCameraPermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            CameraHelper.uploadPhotoDialog(getMainActivity());
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestCameraPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestCameraPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Camera And Storage Permission to processed");
                        openSettings();
                    }
                })

                .onSameThread()
                .check();


    }

    private void openSettings() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Uri uri = Uri.fromParts("package", getDockActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
