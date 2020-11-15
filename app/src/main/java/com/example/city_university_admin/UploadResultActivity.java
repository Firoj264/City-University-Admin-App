package com.example.city_university_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UploadResultActivity extends AppCompatActivity {
    private WebView webview;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_result);
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());


        webview.loadUrl("https://cityuniversity.orbund.com/");
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);


    }

    public class mywebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}