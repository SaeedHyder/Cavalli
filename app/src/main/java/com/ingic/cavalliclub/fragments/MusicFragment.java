package com.ingic.cavalliclub.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.entities.EntityCavalliNights;
import com.ingic.cavalliclub.entities.EntityLiveMusic;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.global.AppConstants;
import com.ingic.cavalliclub.global.WebServiceConstants;
import com.ingic.cavalliclub.helpers.InternetHelper;
import com.ingic.cavalliclub.interfaces.DeviceVolumeControlListener;
import com.ingic.cavalliclub.ui.adapters.AdapterRecyclerViewMusic;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MusicFragment extends BaseFragment implements DeviceVolumeControlListener{


    @BindView(R.id.iv_photo_song)
    ImageView ivPhotoSong;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_pasue)
    ImageView ivPasue;
    @BindView(R.id.tv_track_title)
    AnyTextView tvTrackTitle;
    @BindView(R.id.tv_performing_now)
    AnyTextView tvPerformingNow;
    @BindView(R.id.seekBarSound)
    SeekBar volumeSeekbar;
    @BindView(R.id.txt_updates)
    AnyTextView txtUpdates;
    @BindView(R.id.ll_see_all)
    LinearLayout llSeeAll;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.recycler_view_music)
    RecyclerView recyclerViewMusic;
    Unbinder unbinder;
    private ArrayList<EntityCavalliNights> musicList = new ArrayList<>();
    private ArrayList<EntityCavalliNights> userCollection;
    private AdapterRecyclerViewMusic mAdapter;
    public String ArrayStringContainer;
    EntityLiveMusic entityLiveMusic;
    ImageLoader imageLoader;

    static String AUDIO_PATH = "http://edge.mixlr.com/channel/dqgpt";
    private MediaPlayer mediaPlayer;
    private int playbackPosition = 0;
    private AudioManager audioManager = null;

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDockActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getDockActivity().setDeviceVolumeControlListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageLoader = ImageLoader.getInstance();
        getMainActivity().hideBottomTab();
        ivPasue.setVisibility(View.VISIBLE);

        getMusicUpdates();
        getLiveMusicData();
        getMainActivity().showBottomTab(AppConstants.music);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getDockActivity().setDeviceVolumeControlListener(null);
    }

    @Override
    public void onVolumeUp() {
        if(volumeSeekbar.getProgress() < 100){
            int progress = volumeSeekbar.getProgress();
            progress += 1;
            volumeSeekbar.setProgress(progress);
        }
    }

    @Override
    public void onVolumeDown() {
        if(volumeSeekbar.getProgress() <= 100 && volumeSeekbar.getProgress() >= 0){
            int progress = volumeSeekbar.getProgress();
            progress -= 1;
            volumeSeekbar.setProgress(progress);
        }
    }

    private void whenStart() {
        try {
            playAudio(AUDIO_PATH);
            //playLocalAudio();
            //playLocalAudio_UsingDescriptor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initControls() {
        try {

            audioManager = (AudioManager) getDockActivity().getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMusicUpdates() {
        serviceHelper.enqueueCall(webService.getMusicEvents(AppConstants.MUSIC_EVENTS), WebServiceConstants.MUSIC_EVENTS);
    }

    private void getLiveMusicData() {
        serviceHelper.enqueueCall(webService.liveMusicData(), WebServiceConstants.LIVE_MUSIC);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.MUSIC_EVENTS:
                musicList = (ArrayList<EntityCavalliNights>) result;
                setMusicData(musicList);
                Gson gson = new Gson();
                ArrayStringContainer = gson.toJson(musicList);
                prefHelper.setMusicUpdates(ArrayStringContainer);
                break;

            case WebServiceConstants.LIVE_MUSIC:
                entityLiveMusic = (EntityLiveMusic) result;
                prefHelper.setLiveMusic(entityLiveMusic);
                setLiveMusicData(entityLiveMusic);
                break;
        }
    }

    private void setLiveMusicData(EntityLiveMusic entityLiveMusic) {

        tvTrackTitle.setText(entityLiveMusic.getTitle() + "");
        imageLoader.displayImage(entityLiveMusic.getImageUrl(), ivPhotoSong);
        AUDIO_PATH = entityLiveMusic.getMusic();

        whenStart();
        initControls();
    }

    private void setMusicData(ArrayList<EntityCavalliNights> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
/*
        if (userCollection.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            recyclerViewMusic.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            recyclerViewMusic.setVisibility(View.VISIBLE);
        }
*/
    }

    private void bindData(ArrayList<EntityCavalliNights> userCollection) {

        mAdapter = new AdapterRecyclerViewMusic(this.musicList, getDockActivity());
        recyclerViewMusic.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMusic.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMusic.setAdapter(mAdapter);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);

        titleBar.hideButtons();
        titleBar.hideTwoTabsLayout();
        titleBar.showBackButton();
    //    titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.music_small));
    }

    @OnClick({R.id.iv_pasue, R.id.iv_play, R.id.ll_see_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pasue:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    playbackPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                }
                ivPlay.setVisibility(View.VISIBLE);
                ivPasue.setVisibility(View.GONE);
                break;

            case R.id.iv_play:
                whenStart();
                ivPasue.setVisibility(View.VISIBLE);
                ivPlay.setVisibility(View.GONE);
                break;

            case R.id.ll_see_all:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    getDockActivity().replaceDockableFragment(SeeAllMusicFragment.newInstance(), "SeeAllMusicFragment");
                }
                break;
        }
    }

}
