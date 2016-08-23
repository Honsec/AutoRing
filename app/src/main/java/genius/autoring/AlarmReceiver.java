package genius.autoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Hongsec on 2016-08-23.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AutoInstallHelper autoInstallHelper =AutoInstallHelper.getInstance();
        autoInstallHelper.init(context,null);
        autoInstallHelper.openAlarm(context);

        //TODO   api 호출하여 할 미션 시작시킴
        autoInstallHelper.addMission("com.himart.main");
        autoInstallHelper.addMission("gsshop.mobile.v2");
        autoInstallHelper.startMession(new MissionCallback() {
            @Override
            public void onInstalled(String pkg) {
                //TODO 오토설치리스트에 해당하는 앱이  설치되였을때  서버로 설치완료 api호출
                Log.v("demo","alarm 설치완료 :"+pkg);
            }

            @Override
            public void onPartIn(String pkg) {
                //TODO 오토설치리스트에 해당하는 앱을 시작할때  서버로 미션시작을 알림.
                Log.v("demo","alarm 진행시작 :"+pkg);
            }
        });

    }


}
