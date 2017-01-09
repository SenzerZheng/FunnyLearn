package net.frontdo.funnylearn.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import net.frontdo.funnylearn.R;

import static net.frontdo.funnylearn.app.AppConstants.IKEY_VIDEO_URL;


public class ActFlashPlay extends Activity {

    private FrameLayout mFullscreenContainer;
    private FrameLayout mContentView;
    private View mCustomView = null;
    private WebView mWebView;
    private String s = "<html><head><meta charset=\"utf-8\" /><title>swf</title></head><body>"
            + "<embed src=\"https://v.qq.com/iframe/player.html?vid=y03627t7g9u&tiny=0&auto=0\"  bgcolor=\"#000000\""
            + "  width=\"80%\" height=\"80%\" align=\"middle\" allowScriptAccess=\"always\""
            + " allowFullScreen=\"true\" wmode=\"transparent\" "
            + "type=\"application/x-shockwave-flash\"> </embed></body></html>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_flashplay);

        initViews();
        initWebView();

        if (getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
            getWindow().setFlags(0x1000000, 0x1000000);
        }

//        mWebView.loadData(s, "text/html; charset=UTF-8", null);
		mWebView.loadUrl("https://v.qq.com/iframe/player.html?vid=y03627t7g9u&tiny=0&auto=0");
    }

    private void initViews() {
        mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
        mContentView = (FrameLayout) findViewById(R.id.main_content);
        mWebView = (WebView) findViewById(R.id.webview_player);

    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(PluginState.ON);
        // settings.setPluginsEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);

        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    class MyWebChromeClient extends WebChromeClient {

        private CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation = 1;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // TODO Auto-generated method stub
            onShowCustomView(view, mOriginalOrientation, callback);
            super.onShowCustomView(view, callback);

        }

        public void onShowCustomView(View view, int requestedOrientation,
                                     WebChromeClient.CustomViewCallback callback) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            if (getPhoneAndroidSDK() >= 14) {
                mFullscreenContainer.addView(view);
                mCustomView = view;
                mCustomViewCallback = callback;
                mOriginalOrientation = getRequestedOrientation();
                mContentView.setVisibility(View.INVISIBLE);
                mFullscreenContainer.setVisibility(View.VISIBLE);
                mFullscreenContainer.bringToFront();

                setRequestedOrientation(mOriginalOrientation);
            }

        }

        public void onHideCustomView() {
            mContentView.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mFullscreenContainer.removeView(mCustomView);
            mCustomView = null;
            mFullscreenContainer.setVisibility(View.GONE);
            try {
                mCustomViewCallback.onCustomViewHidden();
            } catch (Exception e) {
            }
            // Show the content view.

            setRequestedOrientation(mOriginalOrientation);
        }

    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    public static int getPhoneAndroidSDK() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;

    }

    @Override
    public void onPause() {// 继承自Activity
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
        super.onResume();
        mWebView.onResume();
    }

    public static void boot(Context context, String videoUrl) {
        Intent intent = new Intent(context, ActFlashPlay.class);
        intent.putExtra(IKEY_VIDEO_URL, videoUrl);
        context.startActivity(intent);
    }
}