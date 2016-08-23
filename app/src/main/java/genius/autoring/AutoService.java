package genius.autoring;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 수동으로 자동설치 서비스를 시작
 * Created by Hongsec on 2016-08-23.
 */
public class AutoService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v("demo","service start");

        AutoInstallHelper autoInstallHelper = AutoInstallHelper.getInstance();
            autoInstallHelper.init(this,null);
            autoInstallHelper.openAlarm(this);

        //TODO   api 호출하여 할 미션 시작시킴
        autoInstallHelper.addMission("com.sec.android.app.shealth");
        autoInstallHelper.addMission("com.tayu.tau.pedometer");
        autoInstallHelper.startMession(new MissionCallback() {
            @Override
            public void onInstalled(String pkg) {
                //TODO 오토설치리스트에 해당하는 앱이  설치되였을때  서버로 설치완료 api호출
                Log.v("demo","service 설치완료 :"+pkg);
            }

            @Override
            public void onPartIn(String pkg) {
                //TODO 오토설치리스트에 해당하는 앱을 시작할때  서버로 미션시작을 알림.
                Log.v("demo","service 진행시작 :"+pkg);
            }
        });


        return super.onStartCommand(intent, flags, startId);
    }
}
