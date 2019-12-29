package com.user.exoplayer.player.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.DefaultTrackNameProvider;
import com.google.android.exoplayer2.ui.PlayerView;
import com.user.exoplayer.R;
import com.user.exoplayer.player.data.VideoSource;
import com.user.exoplayer.player.data.database.AppDatabase;
import com.user.exoplayer.player.data.database.Subtitle;
import com.user.exoplayer.player.data.model.Audio;
import com.user.exoplayer.player.data.model.Quality;
import com.user.exoplayer.player.util.AudioDialog;
import com.user.exoplayer.player.util.QualityDialog;
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialog;
import com.user.exoplayer.player.util.PlayerController;
import com.user.exoplayer.player.util.SubtitleDialog;
import com.user.exoplayer.player.util.VideoPlayer;
import com.user.exoplayer.player.util.videoplayerdialog.VideoPlayerDialogAdapterListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerController {

    private static final String TAG = "PlayerActivity";
    private PlayerView playerView;
    private VideoPlayer player;
    private ImageButton mute, unMute, subtitle, audio, setting, lock, unLock, nextBtn, retry, back;
    private ProgressBar progressBar;
    private VideoPlayerDialog alertDialog;
    private VideoSource videoSource;
    private AudioManager mAudioManager;
    private boolean disableBackPress = false;
    List<Audio> audioList;
    List<Subtitle> subtitleList;
    List<Quality> qualityList;

    private static final int BITRATE_1080P = 2800000;
    private static final int BITRATE_720P = 1600000;
    private static final int BITRATE_480P = 700000;
    private static final int BITRATE_360P = 530000;
    private static final int BITRATE_240P = 400000;
    private static final int BITRATE_160P = 300000;

    /***********************************************************
     Handle audio on different events
     ***********************************************************/
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (player != null)
                                //  player.getPlayer().setPlayWhenReady(true);
                                break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Lost audio focus, but will gain it back (shortly), so note whether
                            // playback should resume
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost audio focus, probably "permanently"
                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                    }
                }
            };


    /***********************************************************
     Activity lifecycle
     ***********************************************************/
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getDataFromIntent();
        setupLayout();
        initSource();
    }

    private void getDataFromIntent() {
        videoSource = getIntent().getParcelableExtra("videoSource");
    }

    private void setupLayout() {
        playerView = findViewById(R.id.demo_player_view);
        progressBar = findViewById(R.id.progress_bar);

        mute = findViewById(R.id.btn_mute);
        unMute = findViewById(R.id.btn_unMute);
        subtitle = findViewById(R.id.btn_subtitle);
        audio = findViewById(R.id.btn_audio);
        setting = findViewById(R.id.btn_settings);
        lock = findViewById(R.id.btn_lock);
        unLock = findViewById(R.id.btn_unLock);
        nextBtn = findViewById(R.id.exo_next);
        retry = findViewById(R.id.retry_btn);
        back = findViewById(R.id.btn_back);

        //optional setting
        playerView.getSubtitleView().setVisibility(View.GONE);

        mute.setOnClickListener(this);
        unMute.setOnClickListener(this);
        subtitle.setOnClickListener(this);
        audio.setOnClickListener(this);
        setting.setOnClickListener(this);
        lock.setOnClickListener(this);
        unLock.setOnClickListener(this);
        retry.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initSource() {

        if (videoSource.getVideos() == null) {
            Toast.makeText(this, "can not play video", Toast.LENGTH_SHORT).show();
            return;
        }

        player = new VideoPlayer(playerView, getApplicationContext(), videoSource, this);

        checkSubtitleAvailability();

//        checkAudioAvailability();

        player.seekToOnDoubleTap();
        this.mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        player.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_ENDED:
                        nextBtn.performClick();
                        break;
                    case Player.STATE_READY:
                        Log.d(TAG, "onPlayerStateChanged: STATE_READY");
                        mAudioManager.requestAudioFocus(
                                mOnAudioFocusChangeListener,
                                AudioManager.STREAM_MUSIC,
                                AudioManager.AUDIOFOCUS_GAIN);
                        break;
                }
            }

        });

        //optional setting
        playerView.getSubtitleView().setVisibility(View.GONE);
        player.seekToOnDoubleTap();

        playerView.setControllerVisibilityListener(visibility ->
        {
            Log.i(TAG, "onVisibilityChange: " + visibility);
            if (player.isLock())
                playerView.hideController();

            back.setVisibility(visibility == View.VISIBLE && !player.isLock() ? View.VISIBLE : View.GONE);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (player != null)
            player.resumePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (player != null)
            player.resumePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null)
            player.releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            mAudioManager = null;
        }
        if (player != null) {
            player.releasePlayer();
            player = null;
        }
        PlayerApplication.getRefWatcher(this).watch(this);
    }

    @Override
    public void onBackPressed() {
        if (disableBackPress)
            return;

        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUi();
    }

    @Override
    public void onClick(View view) {
        int controllerId = view.getId();

        switch (controllerId) {
            case R.id.btn_mute:
                player.setMute(true);
                break;
            case R.id.btn_unMute:
                player.setMute(false);
                break;
            case R.id.btn_settings:
                showQualityDialog();
//                player.setSelectedQuality(this);
                break;
            case R.id.btn_subtitle:
                showSubtitleDialog();
                break;
            case R.id.btn_audio:
                showAudioDialog();
                break;
            case R.id.btn_lock:
                updateLockMode(true);
                break;
            case R.id.btn_unLock:
                updateLockMode(false);
            case R.id.exo_rew:
                player.seekToSelectedPosition(0, true);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.retry_btn:
                initSource();
                showProgressBar(true);
                showRetryBtn(false);
                break;
            case R.id.exo_next:
                player.seekToNext();
                break;
            case R.id.exo_prev:
                player.seekToPrevious();
                break;
            default:
                break;
        }


        if (controllerId == R.id.btn_lock) {
            updateLockMode(true);
        }

        if (controllerId == R.id.btn_unLock) {
            updateLockMode(false);
        }

    }

    public void checkSubtitleAvailability() {

        if (player.getCurrentVideo().getSubtitles() == null ||
                player.getCurrentVideo().getSubtitles().size() == 0) {
            subtitle.setImageResource(R.drawable.exo_no_subtitle_btn);
            subtitle.setClickable(false);
        }
    }

    public void checkAudioAvailability() {

        HlsManifest hlsManifest = (HlsManifest) player.getPlayer().getCurrentManifest();

        if (hlsManifest == null || hlsManifest.masterPlaylist.audios.size() == 0) {
            audio.setImageResource(R.drawable.exo_no_audio_btn);
            audio.setClickable(false);
        }
    }

    /***********************************************************
     UI config
     ***********************************************************/
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void showSubtitle(boolean show) {

        if (player == null || playerView.getSubtitleView() == null)
            return;

        if (!show) {
            playerView.getSubtitleView().setVisibility(View.GONE);
            return;
        }

        playerView.getSubtitleView().setVisibility(View.VISIBLE);
    }

    @Override
    public void changeSubtitleBackground() {
        CaptionStyleCompat captionStyleCompat = new CaptionStyleCompat(Color.YELLOW, Color.TRANSPARENT, Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW, Color.LTGRAY, null);
        playerView.getSubtitleView().setStyle(captionStyleCompat);
    }

    private void showSubtitleDialog() {
        //init subtitle dialog

        SubtitleDialog subtitleDialog = new SubtitleDialog();
        subtitleDialog.showNow(getSupportFragmentManager(), VideoPlayerDialog.TAG);
        subtitleDialog.showTitleScreen("Subtitle");
        subtitleDialog.showSubtitleList(getSubtitleList(), (subtitle, position) -> {

            if (subtitle.getId() == -1)
                PlayerActivity.this.showSubtitle(false);
            else
                player.setSelectedSubtitle(subtitle);
            updateSubtitleList(position);
            subtitleDialog.dismiss();

        });
    }

    private void showAudioDialog() {

        AudioDialog audioDialog = new AudioDialog();
        audioDialog.showNow(getSupportFragmentManager(), VideoPlayerDialog.TAG);
        audioDialog.showTitleScreen("Audio");
        audioDialog.showAudioList(getAudioList(), (audio, position) -> {
            player.setSelectedAudio(audio.getLanguage());
            updateAudioList(position);
            audioDialog.dismiss();
        });
    }

    private void showQualityDialog() {

        QualityDialog qualityDialog = new QualityDialog();
        qualityDialog.showNow(getSupportFragmentManager(), VideoPlayerDialog.TAG);
        qualityDialog.showTitleScreen("Quality");
        qualityDialog.showQualityList(getQualityList(), (quality, position) -> {

            DefaultTrackSelector.ParametersBuilder parametersBuilder = player.getTrackSelector().buildUponParameters();
            DefaultTrackSelector.SelectionOverride selectionOverride =
                    new DefaultTrackSelector.SelectionOverride(quality.getGroupIndex(), quality.getTrackIndex());
            parametersBuilder.setRendererDisabled(0,
                    player.getTrackSelector().getParameters().getRendererDisabled(0));
            if (player.getTrackSelector().getCurrentMappedTrackInfo() != null) {
                parametersBuilder.setSelectionOverride(0,
                        player.getTrackSelector().getCurrentMappedTrackInfo().getTrackGroups(0), selectionOverride);
            }
            player.getTrackSelector().setParameters(parametersBuilder);

            qualityDialog.dismiss();
        });
    }

    private void updateSubtitleList(int position) {
        for (Subtitle subtitle : subtitleList)
            subtitle.setSelected(false);

        subtitleList.get(position).setSelected(true);
    }

    private void updateAudioList(int position) {
        for (Audio audio : audioList)
            audio.setSelected(false);

        audioList.get(position).setSelected(true);
    }

    private List<Audio> getAudioList() {

        if (audioList != null)
            return audioList;

        audioList = new ArrayList<>();
        HlsManifest hlsManifest = (HlsManifest) player.getPlayer().getCurrentManifest();

        if (hlsManifest != null && hlsManifest.masterPlaylist != null
                && hlsManifest.masterPlaylist.audios != null)
            for (int i = 0; i < hlsManifest.masterPlaylist.audios.size(); i++) {

                if (hlsManifest.masterPlaylist.audios.get(i) != null &&
                        hlsManifest.masterPlaylist.audios.get(i).format != null &&
                        hlsManifest.masterPlaylist.audios.get(i).format.label != null &&
                        hlsManifest.masterPlaylist.audios.get(i).format.language != null)
                    audioList.add(new Audio(hlsManifest.masterPlaylist.audios.get(i).format.label,
                            hlsManifest.masterPlaylist.audios.get(i).format.language, false));
            }

        return audioList;
    }

    private List<Subtitle> getSubtitleList() {

        if (subtitleList != null)
            return subtitleList;

        subtitleList = new ArrayList<>();
        subtitleList.add(new Subtitle(-1, "No Subtitle", "", false));
        subtitleList.addAll(player.getCurrentVideo().getSubtitles());

        return subtitleList;
    }

    private List<Quality> getQualityList() {

        qualityList = new ArrayList<>();
        DefaultTrackSelector defaultTrackSelector = player.getTrackSelector();
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = defaultTrackSelector.getCurrentMappedTrackInfo();
        TrackGroupArray trackGroupArray = mappedTrackInfo.getTrackGroups(0);

        for (int i = 0; i < trackGroupArray.length; i++) {
            TrackGroup trackGroup = trackGroupArray.get(i);

            for (int j = 0; j < trackGroup.length; j++) {
                qualityList.add(
                        new Quality(buildBitrateString(trackGroup.getFormat(j)),
                        i,
                        j,
                        player.getPlayer().getVideoFormat().bitrate == trackGroup.getFormat(j).bitrate
                        ));
            }
        }

        return qualityList;
    }

    private String buildBitrateString(Format format) {
        int bitrate = format.bitrate;

        if (bitrate == Format.NO_VALUE) {
            return new DefaultTrackNameProvider(getResources()).getTrackName(format);
        }
        if (bitrate <= BITRATE_160P) {
            return " 160P";
        }
        if (bitrate <= BITRATE_240P) {
            return " 240P";
        }
        if (bitrate <= BITRATE_360P) {
            return " 360P";
        }
        if (bitrate <= BITRATE_480P) {
            return " 480P";
        }
        if (bitrate <= BITRATE_720P) {
            return " 720P";
        }
        if (bitrate <= BITRATE_1080P) {
            return " 1080P";
        }
        return new DefaultTrackNameProvider(getResources()).getTrackName(format);
    }

    public void setMuteMode(boolean mute) {
        if (player != null && playerView != null) {
            if (mute) {
                this.mute.setVisibility(View.GONE);
                unMute.setVisibility(View.VISIBLE);
            } else {
                unMute.setVisibility(View.GONE);
                this.mute.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateLockMode(boolean isLock) {
        if (player == null || playerView == null)
            return;

        player.lockScreen(isLock);

        if (isLock) {
            disableBackPress = true;
            playerView.hideController();
            unLock.setVisibility(View.VISIBLE);
            return;
        }

        disableBackPress = false;
        playerView.showController();
        unLock.setVisibility(View.GONE);

    }

    @Override
    public void showProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showRetryBtn(boolean visible) {
        retry.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void audioFocus() {
        mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void setVideoWatchedLength() {
        AppDatabase.Companion.getDatabase(getApplicationContext()).videoDao().
                updateWatchedLength(player.getCurrentVideo().getUrl(), player.getWatchedLength());
    }

    @Override
    public void videoEnded() {
        findViewById(R.id.exo_next).performClick();
    }

}
