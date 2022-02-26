package com.scrappers.implicitintent;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Test Thred-up vulnerability.
 *
 * - Test the deep linking vulnerability function of intent-filters.
 *
 * - Test a suggested fix to the problem from a core android development principles.
 * Use this command to test deep linking vul :
 * ./adb shell am start -a android.intent.action.VIEW -d "scrappers://browser/?url=https://evil.com"
 *
 * - Resources :
 * - https://developer.android.com/training/app-links/deep-linking
 * - https://developer.android.com/training/app-links
 * - https://developer.android.com/guide/webapps/webview
 *
 * @author pavl_g.
 */
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // proceed if you find the right word
        webView = new WebView(getApplicationContext());
        webView.setId('B');
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);
        // set-up the activity with a browser web-view
        setContentView(webView);

        // process a suggested fix
        processIntentSuggestedFix(startDispatcher());

        final Intent launcherIntent = this.getIntent();
        if (launcherIntent.getAction().equals(Intent.ACTION_VIEW)) {
            processIntentSuggestedFix(launcherIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSettings = null;
        webView = null;
    }

    protected Intent startDispatcher() {
        final Intent startIntent = new Intent(Intent.ACTION_VIEW);
        startIntent.setData(Uri.parse("scrappers://browser/?url=https://thredup.com/"));
        return startIntent;
    }

    protected final void processIntent(final Intent intent) {
        final Uri intentData = intent.getData();
        final String formattedUri = intentData.toString();
        // scrappers://browser/?url=https://auth.thredup.com/"
        final String command = formattedUri.substring(formattedUri.indexOf(":"));
        if (command.contains("//browser/?url=")) {
            final String url = formattedUri.substring(formattedUri.indexOf("https"));
            webView.loadUrl(url);
        }
    }

    protected final void processIntentSuggestedFix(final Intent intent) {
        final Uri intentData = intent.getData();
        final String formattedUri = intentData.toString();
        // scrappers://browser/?url=https://auth.thredup.com/"
        final String command = formattedUri.substring(formattedUri.indexOf(":"));
        if (command.contains("//browser/?url=https://thredup.com/")) {
            final String url = formattedUri.substring(formattedUri.indexOf("https"));
            webView.loadUrl(url);
        }
    }
}