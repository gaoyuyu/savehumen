package com.moudle.savehumen.savehumen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.DownLoadResultListener;
import com.just.library.IWebLayout;
import com.just.library.PermissionInterceptor;
import com.just.library.WebDefaultSettingsManager;
import com.moudle.savehumen.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWebViewActivity extends Activity {
    @BindView(R.id.webView)
    WebView webView;
    private AgentWeb mAgentWeb;
    private WebView mWebView;
    private String url;
    private boolean isExit;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);

        url=getIntent().getStringExtra("data_one");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setIndicatorColorWithHeight(R.color.white, 1)
                .setWebChromeClient(getWebChromeClient())
                .setWebViewClient(getWebViewClient())
                .setWebView(getWebView())
                .setPermissionInterceptor(getPermissionInterceptor())
                .setWebLayout(getWebLayout())
                .addDownLoadResultListener(mDownLoadResultListener)
                .setAgentWebSettings(getAgentWebSettings())
                .setSecutityType(AgentWeb.SecurityType.strict)
                .openParallelDownload()//打开并行下载 , 默认串行下载。
                .setNotifyIcon(R.mipmap.download) //下载通知图标。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(url);
        mWebView = mAgentWeb.getWebCreator().get();

        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
    }


    @OnClick({R.id.rbt_home, R.id.rbt_back, R.id.rbt_go, R.id.rbt_refresh, R.id.rbt_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rbt_home://首页
                mWebView.clearCache(true);
                mWebView.loadUrl(url);
                mWebView.postDelayed(() -> mWebView.clearHistory(), 1000);
                break;
            case R.id.rbt_back://后退
                mWebView.goBack();
                break;
            case R.id.rbt_go://前进
                mWebView.goForward();
                break;
            case R.id.rbt_refresh://刷新
                mWebView.reload();
                break;
            case R.id.rbt_clear://清除缓存
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(MyWebViewActivity.this);
                normalDialog.setTitle("清除缓存");
                normalDialog.setMessage("是否清除");
                normalDialog.setPositiveButton("清除"
                        , (dialog, which) -> {
                            mAgentWeb.getWebCreator().get().clearHistory();
                            mAgentWeb.getWebCreator().get().clearCache(true);
                            dialog.dismiss();
                            Toast.makeText(this,"清除成功",Toast.LENGTH_LONG).show();
                        });
                normalDialog.setNegativeButton("取消"
                        , (dialog, which) -> dialog.dismiss());
                normalDialog.create().show();
                break;
        }
    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Nullable
    WebChromeClient getWebChromeClient() {
        return null;
    }

    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
            //do you work

        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
            //do you work
        }
    };

    protected
    @Nullable
    WebViewClient getWebViewClient() {
        return mWebViewClient;
    }

    protected
    @Nullable
    WebView getWebView() {
        return null;
    }

    protected
    @Nullable
    IWebLayout getWebLayout() {
        return null;
    }

    protected PermissionInterceptor getPermissionInterceptor() {
        return null;
    }

    public
    @Nullable
    AgentWebSettings getAgentWebSettings() {
        return WebDefaultSettingsManager.getInstance();
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            if (url.startsWith("http") || url.startsWith("https")) {
                super.onPageStarted(view, url, favicon);
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MyWebViewActivity.this, "您没有安装相应的程序", Toast.LENGTH_LONG).show();
                    //当手机上没有安装对应应用时报出异常
                }
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("http") || url.startsWith("https")) {
                view.loadUrl(url);
                return false;
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MyWebViewActivity.this, "您没有安装相应的程序", Toast.LENGTH_LONG).show();
                    //当手机上没有安装对应应用时报出异常
                }
                return true;

            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.d("SslError:", error.toString());
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            if (!mAgentWeb.getWebCreator().get().canGoBack()) {
                return false;
            }
        }
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再点击一次退出程序", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> isExit = false, 2000);

        } else {
            Intent it = new Intent(Intent.ACTION_MAIN);
            it.addCategory(Intent.CATEGORY_HOME);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }
    }

}
