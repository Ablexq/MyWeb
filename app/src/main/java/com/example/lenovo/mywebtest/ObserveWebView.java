package com.example.lenovo.mywebtest;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 监听webview的滚动
 * <p>
 * Created by lenovo on 2017/8/16.
 */

public class ObserveWebView extends WebView {

    private OnWebViewListener webViewListener = null;

    public ObserveWebView(Context context) {
        super(context);
    }

    public ObserveWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserveWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (webViewListener != null) {
            webViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setWebViewListener(OnWebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    public interface OnWebViewListener {
        void onScrollChanged(ObserveWebView observeWebView, int x, int y, int oldx, int oldy);
    }

}
