package genius.autoring;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import genius.autoring.bean.DeviceInfo;

/**
 * Created by Hongsec on 2016-08-22.
 */
public class AutoLoginHelper {


    public static final int PKG_ADD = 9977;
    private Context mContext;
    private WebView mWebView;

    private String installBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'price buy id-track-click id-track-impression\');btn[0].click();};";
    private String popupInstallBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'play-button apps loonie-ok-button\');btn[0].click();};";

    private final String devicegetUrl = "https://play.google.com/store/apps/details?id=cq.game.fivechess";
    private final String basicMissionUrl = "https://play.google.com/store/apps/details?id=";
    private String pkg="";


    enum STATUS{
        GETDEVICES,
        CHECKLOGIN,
        MISSION
    };

    STATUS status;



    public void init(Context context,WebView webView){
        mContext = context;
        initWebView(context,webView);
    }


    public void Go2Login(){
        mWebView.loadUrl("https://accounts.google.com/AddSession?sacu=1&continue=https%3A%2F%2Fplay.google.com%2Fstore%2F&hl=ko&service=googleplay#identifier");
    }

    public boolean isLogined(){
        return false;
    }

    public String getGoogleAccount(){
        return "";
    }

    public void fd(){

    };





    public   void onEvent(MyBus myBus){
        if(!myBus.target_name.contains(AutoLoginHelper.class.getSimpleName())  ||   myBus.data!= 1 ) return;


        switch (myBus.action){

            case PKG_ADD:

//                nextMission();
                break;

        }


    }








    /**
     * 미션 시작
     * @param pkg
     */

    MissionCallback missionCallback;

    public void startMession(String pkg,MissionCallback missionCallback){
        status = STATUS.MISSION;
        this.pkg = pkg;
        this.missionCallback = missionCallback;
        mWebView.loadUrl( basicMissionUrl+ pkg);
    }


    /**
     * 설비 정보 가져오기
     */


    DeviceInfoCallback deviceInfoCallback = null;
    public void getDevices(DeviceInfoCallback deviceInfoCallback){
        status = STATUS.GETDEVICES;
        this.deviceInfoCallback = deviceInfoCallback;
        mWebView.loadUrl(devicegetUrl);

    }









    private void initWebView(Context context,WebView mWebView) {
        if(mWebView!=null){
            this.mWebView =mWebView;
        }else{
            mWebView =new WebView(context);
        }
        WebSettings set = mWebView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);
        set.setJavaScriptCanOpenWindowsAutomatically(true);
        set.setSupportMultipleWindows(true);
        String path = context.getDir("database", Context.MODE_PRIVATE).getPath();
        set.setDatabaseEnabled(true);
        set.setDatabasePath(path);
        set.setDomStorageEnabled(true);
        set.setUseWideViewPort(true);
        set.setLoadWithOverviewMode(true);
        set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        set.setSavePassword(true);
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "android");
        set.setRenderPriority(WebSettings.RenderPriority.HIGH);
        set.setLoadsImagesAutomatically(false);
        set.setBlockNetworkImage(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if(Build.VERSION.SDK_INT >= 0x15) {
            set.setMixedContentMode(0x0);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        if(Build.VERSION.SDK_INT >= 0x10) {
            set.setAllowUniversalAccessFromFileURLs(true);
        }
        mWebView.setWebViewClient(webViewClient);
    }







    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);




        }
    };





    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.v("demo","PageStart:"+url);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.v("demo","PageFinished:"+url);

            if(status == STATUS.GETDEVICES){

                //step 0:
                if (url.contains("https://accounts.google.com")) {
                    //로그인 안됨

                    if(deviceInfoCallback!=null){
                        deviceInfoCallback.onResponse(false,null);
                    }
                    return;
                }

                //step 1:
                if(url.equalsIgnoreCase(devicegetUrl)){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mWebView.loadUrl(installBtnClick_Script);

                        }
                    },1000L);
                }

            }



            if(status == STATUS.MISSION){

                //step 0:
                if (url.contains("https://accounts.google.com")) {
                    //로그인 안됨

                    return;
                }

                //step 1:
                if(url.contains(basicMissionUrl)){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mWebView.loadUrl(installBtnClick_Script);//

                        }
                    },1000L);
                }

            }



        }


        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            mWebView.loadUrl("javascript:window.android.getHTML('" + url + "','<html>'+document.body.innerHTML+'</html>');");
        }
    };

      class JavaScriptInterface {

          @JavascriptInterface
          public void getHTML(String url, final String html) {
              if (!TextUtils.isEmpty(html)) {
                  onDataParse(url, html);
              }
          }

      }














    private void onDataParse(String url, String html) {


        if (url.contains("https://accounts.google.com")) {
            //로그인 안됨


            return;
        }

        //기기정보 가져옴무니다
        if (url.contains("/store/apps/details/dialog/app_dialog_permission_buckets")) {

            Document document = Jsoup.parse(html);
            ArrayList<DeviceInfo> devices = new ArrayList<>();//id-displayed-device
            Elements selected_device = document.getElementsByClass("id-displayed-device");
            DeviceInfo selected_deviceInfo = new DeviceInfo();
            if (selected_device != null && selected_device.size() > 0) {
                selected_deviceInfo.deviceName = selected_device.get(0).childNode(0).toString();
                selected_deviceInfo.isSelected = 1;
                devices.add(selected_deviceInfo);
            }

            Elements unselected_devices = document.getElementsByClass("device-selector-dropdown-child");

            boolean existActiveDevice= false; //실제 사용가능 혹은 사용가능 디바이스가 있을경우.

            if (unselected_devices != null) {
                for (Element unselected_device : unselected_devices) {
                    DeviceInfo deviceInfo = new DeviceInfo();
                    Elements device_title = unselected_device.getElementsByClass("device-title");
                    if (device_title != null) {
                        deviceInfo.deviceName = device_title.get(0).childNode(0).toString();
                    }
                    deviceInfo.isSelected = 0;
                    if (selected_deviceInfo.deviceName.equalsIgnoreCase(deviceInfo.deviceName)) {
                        existActiveDevice = true;
                    }
                    devices.add(deviceInfo);
                }
            }

            if(existActiveDevice){
                devices.remove(0);
            }

            if(status == STATUS.GETDEVICES){
                if(deviceInfoCallback!=null){
                    deviceInfoCallback.onResponse(true,devices);
                }
            }

        }
        
        
        
        
        
        
        
        //미션 시작
        if(url.equalsIgnoreCase("")){
            //// STOPSHIP: 2016-08-22
            if(status == STATUS.MISSION){
                if(existActiveDevice){

                    mWebView.loadUrl(popupInstallBtnClick_Script);//
                }else{
                    if(missionCallback!=null){
                        missionCallback.onResponse(false,pkg);
                    }
                }
            }
            
            
        }
        
        
        
        
        
        
        
        
        
    }















}
