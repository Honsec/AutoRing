package genius.autoring.auto;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by Hongsec on 2016-08-22.
 */
public class AutoLoginWebView extends RelativeLayout {
    public AutoLoginWebView(Context context) {
        super(context);
    }

/*

    private final String googleLoginUrl = "https://accounts.google.com/AddSession?sacu=1&continue=https%3A%2F%2Fplay.google.com%2Fstore%2F&hl=ko&service=googleplay#identifier";
    private Context mContext;
    private WebView mWebView;

    private String userGoogleId;
    private String userGooglePw;

    public AutoLoginWebView(Context context) {
        super(context);
        mContext = context;
    }

    public AutoLoginWebView(Context context, AttributeSet attrs, Context mContext) {
        super(context, attrs);
        this.mContext = mContext;
    }

    public AutoLoginWebView(Context context, AttributeSet attrs, int defStyleAttr, Context mContext) {
        super(context, attrs, defStyleAttr);
        this.mContext = mContext;
    }

    public void setLoadUrl(String _url) {

        onInit(_url);

    }
    public void goGoogleAccount(AutoLoginWebView.OnCallbackGoogleLoginListener _listner) {
        mLoginListner = _listner;
        onInit(googleLoginUrl);
    }


    private void onInit(String _url) {
        mWebView = new WebView(mContext);
        mWebView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.addJavascriptInterface(new AutoLoginWebView.JavaScriptInterface(this, 0x0), "android");
//        mWebView.addJavascriptInterface(new AutoLoginWebView.JavaScriptInterface(),"android");
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(_url);
    }


    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.v("autoWebView",url);
            if(url.contains("https://play.google.com/store")) {
                if(mLoginListner != null) {
                    Log.d("autoWebView", userGoogleId);
                    mLoginListner.onResponce(true, userGoogleId, userGooglePw);
                    mLoginListner = 0x0;
                }
            }

        }
    };


    Handler handler = new Handler();



    class JavaScriptInterface {

        public JavaScriptInterface(AutoLoginWebView autoLoginWebView, int i) {
        }

        public void getPassWord(String _id, String _pw) {
            handler.post(new CustomRunnable(this,_id,_pw));
        }
    }

    class CustomRunnable implements Runnable {

        private   String _pw;
        private   String _id;

        CustomRunnable(AutoLoginWebView.JavaScriptInterface p1, String p2, String p3) {
            _id = p2;
            _pw = p3;
        }


        @Override
        public void run() {
            userGoogleId = _id;
            userGooglePw = _pw;
        }
    }
*/


}
