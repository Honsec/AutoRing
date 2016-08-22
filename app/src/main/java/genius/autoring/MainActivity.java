package genius.autoring;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import genius.autoring.bean.DeviceInfo;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;


    private String installBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'price buy id-track-click id-track-impression\');btn[0].click();};";
    private String popupInstallBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'play-button apps loonie-ok-button\');btn[0].click();};";
    private String googleLoginUrl = "https://accounts.google.com/AddSession?sacu=1&continue=https%3A%2F%2Fplay.google.com%2Fstore%2F&hl=ko&service=googleplay#identifier";
    private String deviceSelectorButton_Script = "javascript: {var btn = document.getElementsByClassName('device-selector-button play-button');btn[0].click();};";
    private String deviceInfoCheckUrl = "https://play.google.com/store/apps/details?id=cq.game.fivechess";

    private EditText edittext;
    private CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext = (EditText) findViewById(R.id.edittext);
//        initWebView();
        autoLoginHelper= new AutoLoginHelper();
        autoLoginHelper.init(this,(WebView)findViewById(R.id.webview));
//        mWebView.loadUrl(googleLoginUrl);
    }

    AutoLoginHelper autoLoginHelper = new AutoLoginHelper();

    public void confirm(View v) {
        String s = edittext.getText().toString();
        if (s.equalsIgnoreCase("1")) {
            mWebView.loadUrl(googleLoginUrl);
        }

        if (s.equalsIgnoreCase("2")) {
            mWebView.loadUrl(installBtnClick_Script);
        }

        if (s.equalsIgnoreCase("3")) {
            mWebView.loadUrl(popupInstallBtnClick_Script);

        }


        if (s.equalsIgnoreCase("4")) {
            mWebView.loadUrl("https://play.google.com/store");
        }
        if (s.equalsIgnoreCase("5")) {
            mWebView.loadUrl("https://play.google.com/settings");
        }
        if (s.equalsIgnoreCase("6")) {
            mWebView.loadUrl(deviceInfoCheckUrl);
        }

        if (s.equalsIgnoreCase("7")) {
            autoLoginHelper.startMession("air.com.hypah.io.slither", new MissionCallback() {
                @Override
                public void onResponse(boolean status, String pkg) {

                }
            });
        }


    }


    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings set = mWebView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);
        set.setJavaScriptCanOpenWindowsAutomatically(true);
        set.setSupportMultipleWindows(true);
        String path = getDir("database", Context.MODE_PRIVATE).getPath();
        set.setDatabaseEnabled(true);
        set.setDatabasePath(path);
        set.setDomStorageEnabled(true);
        set.setUseWideViewPort(true);
        set.setLoadWithOverviewMode(true);
        set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        set.setSavePassword(true);
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "android");
        set.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        set.setLoadsImagesAutomatically(false);
//        set.setBlockNetworkImage(true);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 0x15) {
            set.setMixedContentMode(0x0);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        if (Build.VERSION.SDK_INT >= 0x10) {
            set.setAllowUniversalAccessFromFileURLs(true);
        }
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(webChromeClient);
    }

    //자체로 자바스크립트호출함
    private void getHtml(String url) {
        mWebView.loadUrl("javascript:window.android.getHTML('" + url + "','<html>'+document.body.innerHTML+'</html>');");
    }

    public class JavaScriptInterface {
        String mPasswrod;
        String mUsername;

        @JavascriptInterface
        public void getHTML(String url, final String html) {
            if (!TextUtils.isEmpty(html)) {
                saveUserDataWebView(url, html);
            }
        }

        @JavascriptInterface
        public void save_password(final String password) {


//            if (!TextUtils.isEmpty(password)){
//                LogUtils.e("received from js. password = " + password);
//                this.mPasswrod = password;
//                checkData(mUsername, mPasswrod);
//            }


        }

        @JavascriptInterface
        public void save_username(final String username) {
//            if (!TextUtils.isEmpty(username)) {
//                LogUtils.e("received from js. username = " + username);
//                this.mUsername = username;
//                checkData(mUsername, mPasswrod);
//            }
        }

    }


    /**
     * 여기서 코드 파싱함.
     *
     * @param html
     */
    public void saveUserDataWebView(String url, String html) {


        if (url.contains("https://accounts.google.com")) {
            //로그인 안됨


            return;
        }


        //기기정보 가져옴무니다
        if (url.contains("/store/apps/details/dialog/app_dialog_permission_buckets")) {

            Document document = Jsoup.parse(html);
            List<DeviceInfo> devices = new ArrayList<>();//id-displayed-device
            Elements selected_device = document.getElementsByClass("id-displayed-device");
            DeviceInfo selected_deviceInfo = new DeviceInfo();
            if (selected_device != null && selected_device.size() > 0) {
                selected_deviceInfo.deviceName = selected_device.get(0).childNode(0).toString();
                selected_deviceInfo.isSelected = 1;
                devices.add(selected_deviceInfo);
            }

            Elements unselected_devices = document.getElementsByClass("device-selector-dropdown-child");
            if (unselected_devices != null) {
                for (Element unselected_device : unselected_devices) {
                    DeviceInfo deviceInfo = new DeviceInfo();
                    Elements device_title = unselected_device.getElementsByClass("device-title");
                    if (device_title != null) {
                        deviceInfo.deviceName = device_title.get(0).childNode(0).toString();
                    }
                    deviceInfo.isSelected = 0;
                    if (!selected_deviceInfo.deviceName.equalsIgnoreCase(deviceInfo.deviceName)) {
                        devices.add(deviceInfo);
                    }
                }
            }

        }
    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.v("demo", "PageStart:" + url);

            if (url.contains("https://play.google.com/store")) {
                //급방로그인되여 redirect되였을경우
                //아이디 비번 저장.

            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.v("demo", "PageFinished:" + url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
//            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            getHtml(url);
        }
    };


    private WebChromeClient webChromeClient = new WebChromeClient() {

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.v("demo", "onJsAlert:" + url + ",message:" + message);
            return super.onJsAlert(view, url, message, result);
        }

        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.v("demo", "onJsConfirm:" + url + ",message:" + message);
            return super.onJsConfirm(view, url, message, result);
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.v("demo", "onJsPrompt:" + url + ",message:" + message + ",defaultValue:" + defaultValue);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    };


}
