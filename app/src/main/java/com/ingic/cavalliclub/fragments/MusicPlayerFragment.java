package com.ingic.cavalliclub.fragments;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.fragments.abstracts.BaseFragment;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.ingic.cavalliclub.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MusicPlayerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_photo_song)
    ImageView ivPhotoSong;
    @BindView(R.id.iv_pasue)
    ImageView ivPasue;
    @BindView(R.id.tv_track_title)
    AnyTextView tvTrackTitle;
    @BindView(R.id.tv_performing_now)
    AnyTextView tvPerformingNow;
    @BindView(R.id.seekBarSound)
    SeekBar volumeSeekbar = null;
    ImageLoader imageLoader;

    static String AUDIO_PATH = "http://edge.mixlr.com/channel/dqgpt";
    //static final String AUDIO_PATH = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    private MediaPlayer mediaPlayer;
    private int playbackPosition = 0;
    private AudioManager audioManager = null;

    public static MusicPlayerFragment newInstance() {
        return new MusicPlayerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDockActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        ivPasue.setVisibility(View.VISIBLE);
        imageLoader = ImageLoader.getInstance();
        if (prefHelper != null && prefHelper.getLiveMusic() != null && prefHelper.getLiveMusic().getImageUrl() != null) {
            imageLoader.displayImage(prefHelper.getLiveMusic().getImageUrl(), ivPhotoSong);
            tvTrackTitle.setText(prefHelper.getLiveMusic().getTitle());
            AUDIO_PATH = prefHelper.getLiveMusic().getMusic();
        }
        whenStart();
        initControls();
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

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.music_small));
        titleBar.showBackButton();
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        killMediaPlayer();
    }

    @OnClick({R.id.iv_pasue, R.id.iv_play})
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
            case R.id.seekBarSound:
                break;
            case R.id.iv_play:
                whenStart();
                ivPasue.setVisibility(View.VISIBLE);
                ivPlay.setVisibility(View.GONE);
                break;
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

    private void playLocalAudio() throws Exception {
        mediaPlayer = MediaPlayer.create(getDockActivity(), R.raw.music_file);
        mediaPlayer.start();
    }

    private void playLocalAudio_UsingDescriptor() throws Exception {

        AssetFileDescriptor fileDesc = getResources().openRawResourceFd(
                R.raw.music_file);
        if (fileDesc != null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileDesc.getFileDescriptor(), fileDesc
                    .getStartOffset(), fileDesc.getLength());

            fileDesc.close();

            mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
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
}