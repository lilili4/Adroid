package edu.fjnu.mywebview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String targetUrl = getIntent().getData().toString();
        if(targetUrl==null)
            //若网址为空就直接去百度
            webView.loadUrl("http://www.baidu.com");
        else
            webView.loadUrl(targetUrl);
    }
}
