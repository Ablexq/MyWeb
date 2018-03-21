package com.example.lenovo.mywebtest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyWebAct extends FragmentActivity {

    private ObserveWebView mWebView;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private TextView mTv;
    private ImageView mIv;

    private String url = "http://test.daicash.net/app/lend/detail/view_10998.html";
    //private String url = "http://fu.daicash.cn/active/201709.html";
    //private String url="http://old.daicash.net/app/active/201709.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);

        mProgressBar = ((ProgressBar) this.findViewById(R.id.myProgressBar));
        mWebView = ((ObserveWebView) this.findViewById(R.id.wv));
        mProgressDialog = new ProgressDialog(this);
        mTv = ((TextView) this.findViewById(R.id.tv));
        mIv = ((ImageView) this.findViewById(R.id.iv));

        setScrollBar();//滚动条的设置

        initWebSetting();

        setScrollListener();


        mWebView.loadUrl(url);


        /**
         * 外交使节
         */
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

                if (icon != null) {
                    mIv.setImageBitmap(icon);
                    Log.e("MyWebAct", "icon===" + icon);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                if (!TextUtils.isEmpty(title)) {
                    mTv.setText(title);
                    Log.e("MyWebAct", "title===" + title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                //顶部添加进度条
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);//设置进度值
                }

                super.onProgressChanged(view, newProgress);
            }
        });

        /**
         * 内政大臣
         */
        mWebView.setWebViewClient(new WebViewClient() {

            //控制webView是否处理按键时间,如果返回true,则WebView不处理,返回false则处理,默认false
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.trim().startsWith("tel")) {//特殊情况tel，调用系统的拨号
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    mWebView.loadUrl(url);
                }

                return true;
            }

            //ViewView的缩放发生改变时调用
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

            //通知主程序WebView即将加载指定url的资源
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            //通知应用程序内核即将加载url制定的资源，应用程序可以返回本地的资源提供给内核，若本地处理返回数据，内核不从网络上获取数据。
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressDialog.show();//显示进度圈
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.dismiss();//取消进度圈

                //获取webview的cookie
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                Log.e("MyWebAct", "Cookies = " + CookieStr);

            }

            //更新历史记录
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            //当浏览器访问制定的网址发生错误时会通知我们应用程序，比如网络错误。
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode) {
//                    case HttpStatus.SC_NOT_FOUND:
//                        view.loadUrl("file:///android_assets/error_handle.html");
//                        break;
                }
            }

        });


        //页面的下载设置
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        });

        //页面的回退设置
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                    //return true表示这个事件已经被消费，不会再向上传播
                    return true;
                }
                return false;
            }
        });

    }

    private void setScrollListener() {
        mTv.setAlpha(0);
        ViewTreeObserver viewTreeObserver = mTv.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mTv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final int mTitleHeight = mTv.getHeight() + mIv.getHeight();
                Log.e("MyWebAct", "mTv.getHeight()===" + mTv.getHeight());
                Log.e("MyWebAct", "mIv.getHeight()===" + mIv.getHeight());

                mWebView.setWebViewListener(new ObserveWebView.OnWebViewListener() {
                    @Override
                    public void onScrollChanged(ObserveWebView observeWebView, int x, int y, int oldx, int oldy) {
                        //标题随着滚动：显示与隐藏
                        if (y <= 0) {
                            mTv.setAlpha(0);
                        } else if (y > 0 && y <= mTitleHeight) {
                            float scale = (float) y / mTitleHeight;
//                            Log.e("MyWebAct", "===== 透明度scale ===" + scale);

                            mTv.setAlpha(scale);
                        } else {
                            mTv.setAlpha(1);
                        }

                        Log.e("MyWebAct", "===== mWebView.getContentHeight() ===" + mWebView.getContentHeight());
                        Log.e("MyWebAct", "===== mWebView.getScale()===" + mWebView.getScale());
                        Log.e("MyWebAct", "=====  mWebView.getHeight() ===" + mWebView.getHeight());
                        Log.e("MyWebAct", "=====  mWebView.getScrollY() ===" + mWebView.getScrollY());
                        Log.e("MyWebAct", "=====  mWebView.getContentHeight() * mWebView.getScale() ===" + mWebView.getContentHeight() * mWebView.getScale());
                        Log.e("MyWebAct", "=====  转int的  mWebView.getContentHeight() * mWebView.getScale() ===" + (int) (mWebView.getContentHeight() * mWebView.getScale()));
                        Log.e("MyWebAct", "=====  (mWebView.getHeight() + mWebView.getScrollY()===" + (mWebView.getHeight() + mWebView.getScrollY()));
                        Log.e("MyWebAct", "=====  到了底部吗？===" + (mWebView.getContentHeight() * mWebView.getScale() == (mWebView.getHeight() + mWebView.getScrollY())));
                        Log.e("MyWebAct", "=====  转int 到了底部吗？===" + ((int) (mWebView.getContentHeight() * mWebView.getScale()) == (mWebView.getHeight() + mWebView.getScrollY())));
                        //判断到了底部,注意float转int（去掉小数）
                        if ((int) (mWebView.getContentHeight() * mWebView.getScale()) == (mWebView.getHeight() + mWebView.getScrollY())) {
                            Log.e("MyWebAct", "=====  已经到了底部 ===");
                        }

                    }
                });
            }
        });
    }

    private void setScrollBar() {
        // 去掉垂直滚动条
        mWebView.setVerticalScrollBarEnabled(false);//默认false
        // 去掉横向滚动条
        mWebView.setHorizontalScrollBarEnabled(false);//默认false
        // 设置WebView的一些缩放功能点
//        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
//        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting() {

        WebSettings webSettings = mWebView.getSettings();

         /* ------------------------------JS执行------------------------------ */
         /* ------------------------------JS执行----------------------------- */
        // 允许JS执行
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

         /* ------------------------------//其他细节操作---------------------------- */
         /* ------------------------------//其他细节操作----------------------------- */
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
        //http/https混合加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

         /* ------------------------------加快网页加载速度------------------------------ */
         /* ------------------------------加快网页加载速度------------------------------ */
        //noinspection deprecation
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级:默认normal
        //webSettings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染


        /* ------------------------------缩放------------------------------ */
        /* ------------------------------缩放------------------------------ */
        //自适应屏幕方式一：不推荐
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        //自适应屏幕方式二：
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放,默认false
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件，默认true


        /* ------------------------------缓存------------------------------ */
        /* ------------------------------缓存------------------------------ */

        //Dom缓存
        webSettings.setDomStorageEnabled(true);//默认false

        //database缓存
        webSettings.setDatabaseEnabled(true);  //默认false

        //APP缓存
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);//APP缓存路径
        //noinspection deprecation
        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);//APP缓存大小：10M
        webSettings.setAppCacheEnabled(true);//默认false

        //缓存模式
        if (isNetworkAvailable(this)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        /* ------------------------------访问文件------------------------------ */
        /* ------------------------------访问文件------------------------------ */

        //WebView访问文件数据
        webSettings.setAllowFileAccess(true);//默认true
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
