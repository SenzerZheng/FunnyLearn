package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import butterknife.Bind;

import static net.frontdo.funnylearn.app.AppConstants.IKEY_VIDEO_URL;

/**
 * ProjectName: VideoActivity
 * Description: 视频播放页面
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/20/2016 18:25
 */
public class VideoActivity extends BaseHoldBackActivity {
    private static final String TAG = VideoActivity.class.getSimpleName();

    @Bind(R.id.videoView)
    VideoView videoView;
    @Bind(R.id.activity_video)
    RelativeLayout activityVideo;

    private String videoUrl;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(IKEY_VIDEO_URL)) {
            videoUrl = intent.getStringExtra(IKEY_VIDEO_URL);
        }

        if (StringUtil.checkEmpty(videoUrl)) {
            FrontdoLogger.getLogger().e(TAG, "[ " + TAG + " - onCreate] videoUrl is empty!");
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        showProgressDlg(null, true);
        // 设置视频控制器
        videoView.setMediaController(new MediaController(this));
        // 播放预加载监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                dismissProgressDlg();
            }
        });
        // 播放状态监听
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {           // 缓冲中

                    showProgressDlg(null, true);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {      // 缓冲结束

                    dismissProgressDlg();
                }
                return false;
            }
        });
        // 播放完成回调
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                toast("播放结束！");
                finish();
            }
        });

        // 设置视频路径
        videoView.setVideoURI(Uri.parse(videoUrl));
        // 开始播放视频
        videoView.start();
//        videoView.requestFocus();
    }

    public static void boot(Context context, String videoUrl) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(IKEY_VIDEO_URL, videoUrl);
        context.startActivity(intent);
    }
}
