package genius.autoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private String installBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'price buy id-track-click id-track-impression\');btn[0].click();};";
    private String popupInstallBtnClick_Script = "javascript: {var btn = document.getElementsByClassName(\'play-button apps loonie-ok-button\');btn[0].click();};";
    private String googleLoginUrl = "https://accounts.google.com/AddSession?sacu=1&continue=https%3A%2F%2Fplay.google.com%2Fstore%2F&hl=ko&service=googleplay#identifier";
//    private String deviceSelectorButton_Script = "javascript: {var btn = document.getElementsByClassName('device-selector-button play-button');btn[0].click();};";
//    private String deviceInfoCheckUrl = "https://play.google.com/store/apps/details?id=cq.game.fivechess";

    private EditText edittext;
    private AutoInstallHelper autoInstallHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext = (EditText) findViewById(R.id.edittext);

        autoInstallHelper = new AutoInstallHelper();
        autoInstallHelper.init(this,(WebView)findViewById(R.id.webview));
        autoInstallHelper.openAlarm(this);
    }


    public void confirm(View v) {
        String s = edittext.getText().toString();

        //로그인 으로 가기
        if (s.equalsIgnoreCase("1")) {
            autoInstallHelper.getmWebView().loadUrl(googleLoginUrl);
        }

        //설치버튼 클릭
        if (s.equalsIgnoreCase("2")) {
            autoInstallHelper.getmWebView().loadUrl(installBtnClick_Script);
        }

        //설치확인 버튼 클릭
        if (s.equalsIgnoreCase("3")) {
            autoInstallHelper.getmWebView().loadUrl(popupInstallBtnClick_Script);

        }

        //서비스로 수동 미션 시작
        if (s.equalsIgnoreCase("4")) {
            Intent intent =new Intent(this,AutoService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(intent);
        }


        //현재 뷰에서 수동시작
        if (s.equalsIgnoreCase("5")) {
            Log.v("demo","view start auto");
            autoInstallHelper.addMission("com.hnsmall");
            autoInstallHelper.addMission("com.omnitel.android.lottewebview");
            autoInstallHelper.startMession(new MissionCallback() {
                @Override
                public void onInstalled(String pkg) {
                    //TODO 오토설치리스트에 해당하는 앱이  설치되였을때  서버로 설치완료 api호출
                    Log.v("demo","main 설치완료 :"+pkg);
                }

                @Override
                public void onPartIn(String pkg) {
                    //TODO 오토설치리스트에 해당하는 앱을 시작할때  서버로 미션시작을 알림.
                    Log.v("demo","main 진행시작 :"+pkg);
                }
            });
        }





    }

}
