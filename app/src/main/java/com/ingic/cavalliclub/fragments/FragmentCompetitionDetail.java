package com.ingic.cavalliclub.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.MainActivity;
import com.ingic.cavalliclub.entities.GetCompetitionEntity;
import com.ingic.cavalliclub.entities.ResponseWrapper;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.CameraHelper;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.ui.views.AnyEditTextView;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
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

public class FragmentCompetitionDetail extends BaseFragment implements View.OnClickListener, MainActivity.ImageSetter {


    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.txt_heading)
    AnyTextView txtHeading;
    @BindView(R.id.txt_description)
    AnyTextView txtDescription;
    @BindView(R.id.edt_name)
    AnyEditTextView edtName;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.titleBackBtn)
    ImageView titleBackBtn;
    private static String competitionDetailkey = "competitionDetailkey";
    @BindView(R.id.llTextBox)
    LinearLayout llTextBox;
    @BindView(R.id.llImageBox)
    LinearLayout llImageBox;
    private GetCompetitionEntity CompetitionEntities;
    public File profilePic;
    boolean checkPhoto;
    Unbinder unbinder;

    public static FragmentCompetitionDetail newInstance(GetCompetitionEntity getCompetitionEntity) {
        Bundle args = new Bundle();
        args.putString(competitionDetailkey, new Gson().toJson(getCompetitionEntity));
        FragmentCompetitionDetail fragment = new FragmentCompetitionDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        competitionDetailkey = getArguments().getString(competitionDetailkey);
        if (competitionDetailkey != null) {
            CompetitionEntities = new Gson().fromJson(competitionDetailkey, GetCompetitionEntity.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_competition_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivity().hideBottomTab();
        checkPhoto = false;
        setData();
        getMainActivity().setImageSetter(this);
    }

    private void setData() {
        if (CompetitionEntities.getTitle() != null)
            txtHeading.setText(CompetitionEntities.getTitle() + "");

        if (CompetitionEntities.getDescription() != null)
            txtDescription.setText(CompetitionEntities.getDescription() + "");

        if (CompetitionEntities.getCompetitionImages() != null && CompetitionEntities.getCompetitionImages().size() > 0) {
            if (CompetitionEntities.getCompetitionImages().get(0).getImageUrl() != null)
                ImageLoader.getInstance().displayImage(CompetitionEntities.getCompetitionImages().get(0).getImageUrl(), ivHeader);
        }
/*
        if (prefHelper.getUpdatedUser() != null && prefHelper.getUpdatedUser().getUserName() != null && prefHelper.getUpdatedUser().getUserName() != "") {
            edtName.setText(prefHelper.getUpdatedUser().getUserName() + "");
        } else if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getUserName() != null && prefHelper.getSignUpUser().getUserName() != "")
            edtName.setText(prefHelper.getSignUpUser().getUserName() + "");
*/

        if(CompetitionEntities.getType() != null){
            if (CompetitionEntities.getType().equals(AppConstants.CompetitionTypes.TEXT)) {

                if (prefHelper.getUpdatedUser() != null && prefHelper.getUpdatedUser().getUserName() != null && prefHelper.getUpdatedUser().getUserName() != "") {
                    edtName.setText(prefHelper.getUpdatedUser().getUserName() + "");
                } else if (prefHelper.getSignUpUser() != null && prefHelper.getSignUpUser().getUserName() != null && prefHelper.getSignUpUser().getUserName() != "")
                    edtName.setText(prefHelper.getSignUpUser().getUserName() + "");

                llTextBox.setVisibility(View.VISIBLE);
                llImageBox.setVisibility(View.GONE);
            }

            if (CompetitionEntities.getType().equals(AppConstants.CompetitionTypes.IMAGE)) {
                llImageBox.setVisibility(View.VISIBLE);
                llTextBox.setVisibility(View.GONE);
            }
        }

    }

    private boolean validate() {
        return edtName.testValidity();
    }

    @OnClick({R.id.iv_camera, R.id.btn_submit, R.id.titleBackBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera:
                requestCameraPermission();
                break;
            case R.id.btn_submit:
                if(CompetitionEntities.getType() != null){
                    if (CompetitionEntities.getType().equals(AppConstants.CompetitionTypes.TEXT)) {
                        if (validate()) {
                            competitionParticipation();
                        }
                    }

                    if (CompetitionEntities.getType().equals(AppConstants.CompetitionTypes.IMAGE)) {
                        if (checkPhoto) {
                            competitionParticipation();
                        } else {
                            UIHelper.showShortToastInCenter(getDockActivity(), "Please take photo to participate.");
                        }
                    }
                }
                break;
            case R.id.titleBackBtn:
                getDockActivity().popFragment();
                break;
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setImage(String imagePath) {
        if (imagePath != null) {
            checkPhoto = true;
            profilePic = new File(imagePath);
            // ImageLoader.getInstance().displayImage("file:///" + imagePath, ivPhoto);
            Picasso.with(getDockActivity()).load("file:///" + imagePath).placeholder(R.drawable.placeholder_banner).into(ivPhoto);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.hideTwoTabsLayout();
        titleBar.showBackButtonAsPerRequirement(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().popFragment();
            }
        }, getDockActivity());
        //  titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.details));
    }

    @Override
    public void setFilePath(String filePath) {
    }

    @Override
    public void setVideo(String videoPath) {
    }

    private void competitionParticipation() {
        Call<ResponseWrapper> call = null;

        MultipartBody.Part filePart = null;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("images", profilePic.getName(), RequestBody.create(MediaType.parse("image"), profilePic));
        }

        loadingStarted();

        if(CompetitionEntities.getType() != null){
            if (CompetitionEntities.getType().equals(AppConstants.CompetitionTypes.TEXT)) {
                call = webService.participateCompetitionText(String.valueOf(CompetitionEntities.getId()), edtName.getText().toString().trim());
            }

            if (CompetitionEntities.getType().equals(AppConstants.CompetitionTypes.IMAGE)) {
                call = webService.participateCompetition(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(CompetitionEntities.getId())), filePart != null ? filePart : null);
            }
        }

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();

                if (response.body().getCode().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    UIHelper.showLongToastInCenter(getDockActivity(), "Participated in competition successfully!");
                    getDockActivity().popFragment();
                } else {
                    UIHelper.showLongToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                t.printStackTrace();
            }
        });
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
