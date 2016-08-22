package genius.autoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.greenrobot.event.EventBus;

/**
 * Created by Hongsec on 2016-08-22.
 */
public class AppInstalledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {

            if ("android.intent.action.PACKAGE_ADDED".equalsIgnoreCase(intent.getAction())) {

                    MyBus myBus =new  MyBus();
                    myBus.action = AutoLoginHelper.PKG_ADD;
                myBus.target_name.add(AutoLoginHelper.class.getSimpleName());
                myBus.data = intent.getDataString().substring(8);
                myBus.type = 1;
                EventBus.getDefault().post(myBus);

            }

        }


    }


}
