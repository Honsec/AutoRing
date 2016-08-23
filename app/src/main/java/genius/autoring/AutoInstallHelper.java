package genius.autoring;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Hongsec on 2016-08-22.
 */
public class AutoInstallHelper {


    private static AutoInstallHelper instance;

    public static AutoInstallHelper getInstance(){
        if(instance == null){
            instance = new AutoInstallHelper();
        }
        return instance;
    }
    private WebView mWebView;

    private String installBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'price buy id-track-click id-track-impression\');btn[0].click();};";
    private String popupInstallBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'play-button apps loonie-ok-button\');btn[0].click();};";

    private final String basicMissionUrl = "https://play.google.com/store/apps/details?id=";
    private String pkg = "";


    private ArrayList<String> missionDatas = new ArrayList<>();


    enum STATUS {
        MISSION,
        NONE
    }

    STATUS status=STATUS.NONE;

    public void cancleAll(){
        status = STATUS.NONE;
//        missionDatas.clear();
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
    }


    public void addMission(String missionData){
        if(!missionDatas.contains(missionData)){
            missionDatas.add(missionData);
        }
    }

    public void closeAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pIntent);
    }

    public void openAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour>3){
            calendar.setTimeInMillis(System.currentTimeMillis()+1000*3600*24);
            calendar.set(Calendar.HOUR_OF_DAY,3);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
        }else{
            calendar.set(Calendar.HOUR_OF_DAY,3);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        String format = simpleDateFormat.format(calendar.getTime());
        Log.v("demo","알람 시작시간 :"+format);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*3600*24, pIntent);
    }


    public void init(Context context, WebView webView) {
        initWebView(context, webView);
    }


    public void Go2Login() {
        mWebView.loadUrl("https://accounts.google.com/AddSession?sacu=1&continue=https%3A%2F%2Fplay.google.com%2Fstore%2F&hl=ko&service=googleplay#identifier");
    }

    public boolean isLogined() {
        return false;
    }

    public String getGoogleAccount() {
        return "";
    }



    /**
     * 미션 시작
     *
     * @param pkg
     */

    public    MissionCallback missionCallback;

    public void startMession( MissionCallback missionCallback) {
        if(status!=STATUS.NONE)return;
        this.missionCallback = missionCallback;
        if(missionDatas.size()>0){
            status = STATUS.MISSION;
            mWebView.loadUrl(basicMissionUrl + missionDatas.get(0));
            Log.v("demo","총 " + missionDatas.size()+"개 미션 대기중_____________________________________________________________________________");
            Log.v("demo","Mission Start!!!!"+", pkg:"+ missionDatas.get(0));
            if(this.missionCallback!=null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AutoInstallHelper.getInstance().missionCallback.onPartIn( missionDatas.get(0));
                    }
                }).start();
            }
        }else{
            status = STATUS.NONE;
            Log.v("demo","Mission Clear!");
        }


    }


    private void initWebView(Context context, WebView myview) {
        if (myview != null) {
            this.mWebView = myview;
        } else {
            this.mWebView = new WebView(context);
        }
        WebSettings set =  this.mWebView.getSettings();
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
//        mWebView.addJavascriptInterface(new JavaScriptInterface(), "android");
        set.setRenderPriority(WebSettings.RenderPriority.HIGH);
        set.setLoadsImagesAutomatically(false);
        set.setBlockNetworkImage(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 0x15) {
            set.setMixedContentMode(0x0);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        if (Build.VERSION.SDK_INT >= 0x10) {
            set.setAllowUniversalAccessFromFileURLs(true);
        }
        mWebView.setWebViewClient(webViewClient);
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 0:
                    if(status == STATUS.MISSION){
                        Log.v("demo", "installButton");
                        mWebView.loadUrl(installBtnClick_Script);//
                    }
                    break;
                case 1:
                    if(status == STATUS.MISSION){
                        Log.v("demo", "popupInstallButton");
                        mWebView.loadUrl(popupInstallBtnClick_Script);//
                    }
                break;
                case 2:
                    if(status == STATUS.MISSION){
                        status = STATUS.NONE;
                        Log.v("demo", "nextMission");
                        if(missionDatas.size()>0){
                            oldMissionData.add(missionDatas.get(0));
                            missionDatas.remove(0);
                            startMession(missionCallback);
                        }
                    }
                    break;
            }

        }
    };

    public WebView getmWebView() {
        return mWebView;
    }

    public   ArrayList<String> oldMissionData = new ArrayList<>();

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.v("demo", "PageStart:" + url);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.v("demo", "PageFinished:" + url);


            if (status == STATUS.MISSION) {

                //step 0:
                if (url.contains("https://accounts.google.com")) {
                    //로그인 안됨
                    cancleAll();
                    return;
                }

                //step 1:
                if (url.contains(basicMissionUrl)) {
                    handler.sendEmptyMessageDelayed(0,15000L);
                    handler.sendEmptyMessageDelayed(1,30000L);
                    handler.sendEmptyMessageDelayed(2,120000L);
                }


            }


        }


        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    };


}
