package genius.autoring.auto;

/**
 * Created by Hongsec on 2016-08-22.
 */
public class AutoInstallSDK {
  /*  private static AutoInstallSDK instance;
    private   WebView mWebView;
    private Context mContext;
    private String mGoogleID;
    private String mGooglePW;


    private String googleLoginUrl = "https://accounts.google.com/AddSession?sacu=1&continue=https%3A%2F%2Fplay.google.com%2Fstore%2F&hl=ko&service=googleplay#identifier";
    private String marketUrl = "https://play.google.com/store/apps/details?id=";
    private String installBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'price buy id-track-click id-track-impression\');btn[0].click();};";
    private String popupInstallBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'play-button apps loonie-ok-button\');btn[0].click();};";
    private String mDevice_modelName = "";


    public AutoInstallSDK(Context context) {
        mContext = context;
        instance = this;
        onCreate();
    }


    public AutoInstallSDK(Context context, WebView wv) {
        mContext = context;
        instance = this;
        mWebView = wv;
        onCreate();
    }

    public static AutoInstallSDK getInstance() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit");
        }
        return instance;
    }

    private void onCreate() {
        if(mWebView == null) {
            mWebView = new WebView(mContext);
        }
        WebSettings set = mWebView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);
        set.setJavaScriptCanOpenWindowsAutomatically(true);
        set.setSupportMultipleWindows(true);
        String path = mContext.getDir("database", Context.MODE_PRIVATE).getPath();
        set.setDatabaseEnabled(true);
        set.setDatabasePath(path);
        set.setDomStorageEnabled(true);
        set.setUseWideViewPort(true);
        set.setLoadWithOverviewMode(true);
        set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        set.setSavePassword(false);
        set.setRenderPriority(WebSettings.RenderPriority.HIGH);
        set.setLoadsImagesAutomatically(false);
        set.setBlockNetworkImage(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        mWebView.addJavascriptInterface(new AutoInstallSDK.JavaScriptInterface(this, 0x0), "android");
        if(Build.VERSION.SDK_INT >= 0x15) {
            set.setMixedContentMode(0x0);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        if(Build.VERSION.SDK_INT >= 0x10) {
            set.setAllowUniversalAccessFromFileURLs(true);
        }
        mWebView.setWebViewClient(mWebViewClient);
    }



*/

}
