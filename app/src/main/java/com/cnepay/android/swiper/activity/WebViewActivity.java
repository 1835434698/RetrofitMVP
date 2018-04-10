package com.cnepay.android.swiper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangzy on 2017/5/12.
 */

public class WebViewActivity  extends MvpAppCompatActivity {
    private static final String TAG = "WebViewActivity";

    public static final int DEFAULT_WEB_VIEW = -1;
    public static final int DEMONSTRATIVE_WEB_VIEW = 1;
    public static final int INTERACTIVE_WEB_VIEW = 2;
    public static final int PREFERENTIAL_WEB_VIEW = 3;

    public static final int WEB_VIEW_TITLE = 4;//

    public static final int NOTITLE_WHITE_WEB_VIEW = 5;

    public static final String TITLE_SELECT = "titleSelect";
    public static final String TITLE_TEXT = "titleText";
    public static final String ISNEDDSESSION = "isNeedSession";
    public static final String CURR_URL = "CURR_URL";
    private boolean isNeedSession;
    private String title;
    private String url;
    private  Bundle bundle;
    private WebView webView;
    private View progressBar;
//    int title_select = WEB_VIEW_NOTITLE;
    private int webViewType = DEFAULT_WEB_VIEW;



    public static final String H5Cache = "H5Cache";
    private String js;
    /**
     * 返回app主页
     */
    public static final int backIndex = 1;
    /**
     * 返回上一页
     */
    public static final int backRequestSource = 2;
    /**
     * 实名认证成功并返回
     */
    public static final int backAuthSuccess = 4;

    /**
     * 横竖屏切换时数据保存,用户按Home键,移入下一个Activity所触发的onPause时也会执行(finish不会)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURR_URL, url);
        outState.putString(TITLE_TEXT, title);
        outState.putInt(TITLE_SELECT, webViewType);
        outState.putBoolean(ISNEDDSESSION, isNeedSession);
        Logger.d(TAG, "onSaveInstanceState ---- url = "+url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            bundle = savedInstanceState;
        }else {
            bundle = getIntent().getExtras();
        }
        initView();
        initWebViewSettings();

        //请求的header部分 要求有Date
        Map<String, String> header = new HashMap<String, String>();
        header.put("Date", Utils.getHeaderDate());

        //处理sessionId
        if(isNeedSession){
            LoginBean loginBean = requestLoginBean();
            if (loginBean == null) return;
            String sessionId =  loginBean.getSessionID();
            if (TextUtils.isEmpty(sessionId)){
//                    signOff();
                // TODO: 2017/5/20 下线逻辑不能直接在界面调用，必须交由SessionPresenter统一处理。
                uc.toast(R.string.login_timeout);
                return;
            }
            header.put(MainApp.SESSION_HEADER, sessionId);
        }
        if (url!=null){
            Logger.d(TAG, "url = "+url);
            webView.loadUrl(url ,header);
        }

    }
    private void initView() {
        if (bundle != null){
            webViewType = bundle.getInt(TITLE_SELECT);
            switch (webViewType){
                case DEFAULT_WEB_VIEW:
                case DEMONSTRATIVE_WEB_VIEW:
                case INTERACTIVE_WEB_VIEW:
                case PREFERENTIAL_WEB_VIEW:
                    uc.setContentViewFrameWithBottomBtn(R.layout.activity_webview);
                    title = bundle.getString(TITLE_TEXT);
                    uc.setTitle(title);
                    break;
                case WEB_VIEW_TITLE:
                    uc.setContentViewPure(R.layout.activity_webview);
                    uc.initStatusBar(R.color.white);
                    break;
                case NOTITLE_WHITE_WEB_VIEW:
                    uc.setContentViewFrameWithBottomBtn(R.layout.activity_webview);
                    setWindowStatusBarColor(this, R.color.white);
                    uc.getRelTitle().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                    uc.setBackIcon(R.drawable.all_img_drak_back);
                    uc.initStatusBar(R.color.white);

                    break;
            }
            isNeedSession = bundle.getBoolean(ISNEDDSESSION);
            progressBar = findViewById(R.id.help_progressBar1);
            webView = (WebView) findViewById(R.id.help_webview);
            url = bundle.getString(CURR_URL);
        }
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initWebViewSettings() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDatabaseEnabled(true);
        String path = this.getDir(H5Cache, Context.MODE_PRIVATE).getPath();
        setting.setDatabasePath(path);
        setting.setDomStorageEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(true);
        //版本判断
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setting.setDisplayZoomControls(false);
        } else {
            // 3.0以下可用反射解决
        }
        setting.setAppCacheEnabled(true);
        webView.requestFocus();

        setting.supportMultipleWindows();//支持多窗口
        setting.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过js打开新窗口
        setting.setLoadsImagesAutomatically(true);//支持自动化加载图片
        setting.setAllowFileAccess(true);//设置可以访问文件
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);//是让该WebView插件去缓存浏览的数据
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.setDownloadListener(dl);
        webView.addJavascriptInterface(WebViewActivity.this, "native");
    }

    private void changeTitle(String title) {
        if (TextUtils.isEmpty(title))
            return;
//        if (webViewType != DEMONSTRATIVE_WEB_VIEW && webViewType != PREFERENTIAL_WEB_VIEW) {
//            if (title.length() > 6) {
//                title = title.substring(0, 6) + "…";
//            }
//            uc.setTitle(title);
//            this.title =title;
//        }
    }

    private boolean pageError;
    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {//重定向
            Logger.i(TAG, "shouldOverrideUrlLoading   url:" + url);
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        public void onPageStarted(WebView wb, String url, android.graphics.Bitmap favicon) {
            Logger.i(TAG, "onPageStarted   url:" + url);
            pageError = false;
            WebViewActivity.this.url = url;
            if (url.contains("/appErrorAction.htm") || url.contains("NEED_LOGIN")) {
                //只要服务端返回session失效信息，本地就自动下线。因为WebView访问网络不通过本地封装的HttpManagerLib,故不会触发本地监听中的自动下线机制，因此需要在此单独监听WebView
                Logger.e(TAG, "服务器通知下线，本地登出");
                // TODO: 2017/5/12 下线
//                uc.signOff();
                signOff();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Logger.i(TAG, "onReceivedError  errCode:" + errorCode + "  " + "description:" + description + "   failingUrl:" + failingUrl);
            pageError = true;
            progressBar.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            view.loadUrl("about:blank");
            uc.toast(getString(R.string.web_timeout_hint, title));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Logger.i(TAG, "onReceivedSslError   error:" + error);
            handler.proceed(); // 接受所有网站的证书
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Logger.i(TAG, "onPageFinished   url:" + url + "   view.getTitle():" + view.getTitle());
            if (!pageError) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                changeTitle(view.getTitle());
                if (!TextUtils.isEmpty(js)) {
                    view.loadUrl("javascript:" + js);
                }
            }
        }

    };

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Logger.i(TAG, "onReceivedTitle title:" + title);
            changeTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Logger.i(TAG, "onProgressChanged   newProgress:" + newProgress);
            if (newProgress < 100) {
//                progressBar.setVisibility(View.VISIBLE);
            } else {
//                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle(R.string.web_alert).setMessage(R.string.web_isLogout).setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.button_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).show();
            return true;
        }
    };


    private DownloadListener dl = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            // 调用系统 下载
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    };

    @JavascriptInterface
    public void callback(final int action, String msg) {//该方法暴露给服务器调用，在子线程运行
        Logger.e("xsw", "native callback   action:" + action + "    msg:" + msg);
        Utils.runOnUI(new Runnable() {
            @Override
            public void run() {
                switch (action) {
                    case backIndex:
                        if (webViewType == INTERACTIVE_WEB_VIEW) {
                            Intent intent = new Intent();
                            intent.putExtra(TAG, action);
                            ac.startResponseActivity(intent);
                        } else
                            finish();
                        break;
                    case backRequestSource:
                        Logger.e("xsw", "2:backRequestSource");
                        WebViewActivity.super.onBackPressed();
                        break;
                    case backAuthSuccess://POS 实名认证成功
                        startOpenReceipt();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void startOpenReceipt() {
        Logger.e("xsw", "startOpenReceipt");
        // TODO: 2017/5/12 数据保存
//        Session session = ui.getLoginBean();
//        if (session == null) {
//            ui.toast(R.string.login_timeout);
//            ui.signOff();
//            return;
//        }
//        ui.toast("POS实名认证成功...");
//        session.put(Http.SESSION_IS_POS_MERCHANT, true);
//        session.put(Http.SESSION_POS_STATUS, Http.SESSION_POS_STATUS_VERIFIED);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && !pageError) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

}
