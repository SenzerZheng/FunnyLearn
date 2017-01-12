package net.frontdo.funnylearn.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.frontdo.funnylearn.R;
import net.frontdo.funnylearn.base.BaseHoldBackActivity;
import net.frontdo.funnylearn.common.StringUtil;
import net.frontdo.funnylearn.logger.FrontdoLogger;

import butterknife.Bind;

import static net.frontdo.funnylearn.app.AppConstants.IKEY_VIDEO_URL;

/**
 * ProjectName: SWFPlayerActivity
 * Description: 嵌套WebView视频播放页面
 * 一个重要的陷阱
 * <p>
 * 就算按照上的方式进行了编写，仍旧会有播放不出来的情况，
 * 这里就需要修改Android工程支持的目标版本了，就是targetSdkVersion部分。填写完这个就可以正常播放了。
 * author: JeyZheng
 * version: 4.0
 * created at: 1/9/2017 22:51
 */
public class SWFPlayerActivity extends BaseHoldBackActivity {
    private static final String TAG = SWFPlayerActivity.class.getSimpleName();

    @Bind(R.id.webview)
    WebView webview;

    private String videoUrl;

    @Override
    protected int onBindLayout() {
        return R.layout.activity_swf_player;
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
//        showProgressDlg(null, true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);

        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new InsideWebViewClient());
        webview.loadUrl(videoUrl);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 当窗口关闭的时候也将浏览器关闭，否则视频的声音不停下来
        webview.destroy();
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // must add all chars
            view.loadUrl("javascript:(" +
                    "       function() { " +
                    "               var videos = document.getElementsByTagName('video'); " +
                    "               for(var i=0;i<videos.length;i++) {" +
                    "                   videos[i].play();" +
                    "               }" +
                    "      })()");
//            super.onPageFinished(view, url);
        }
    }

    public static void boot(Context context, String videoUrl) {
        Intent intent = new Intent(context, SWFPlayerActivity.class);
        intent.putExtra(IKEY_VIDEO_URL, videoUrl);
        context.startActivity(intent);
    }
}
