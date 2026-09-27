package com.mutabazisabitifamily;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

public class MainActivity extends Activity {
private android.webkit.ValueCallback<android.net.Uri[]> filePathCallback;
private static final int FILE_CHOOSER_REQUEST_CODE = 1001;
    private WebView webView;

    private static final String PORTAL_URL =
        "https://mutabazisabitifamily.github.io/MutabaziSabitiFamily/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            webView = new WebView(this);
            setContentView(webView);

            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);

            webView.setWebChromeClient(new WebChromeClient() {
    @Override
    public boolean onShowFileChooser(
            WebView webView,
            android.webkit.ValueCallback<android.net.Uri[]> filePathCallback,
            WebChromeClient.FileChooserParams fileChooserParams) {

        if (MainActivity.this.filePathCallback != null) {
            MainActivity.this.filePathCallback.onReceiveValue(null);
        }

        MainActivity.this.filePathCallback = filePathCallback;

        android.content.Intent intent =
                new android.content.Intent(
                        android.content.Intent.ACTION_GET_CONTENT);

        intent.addCategory(
                android.content.Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");

        try {
            startActivityForResult(
                    android.content.Intent.createChooser(
                            intent, "Choose a file"),
                    FILE_CHOOSER_REQUEST_CODE);
        } catch (Exception e) {
            MainActivity.this.filePathCallback = null;
            return false;
        }

        return true;
    }
});

            webView.loadUrl(PORTAL_URL);
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(
        int requestCode, int resultCode,
        android.content.Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
        if (filePathCallback != null) {
            android.net.Uri[] results = null;

            if (resultCode == RESULT_OK && data != null) {
                android.net.Uri uri = data.getData();

                if (uri != null) {
                    results = new android.net.Uri[]{uri};
                }
            }

            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }
            }
    }
}
