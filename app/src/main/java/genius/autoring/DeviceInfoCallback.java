package genius.autoring;

import java.util.ArrayList;

import genius.autoring.bean.DeviceInfo;

/**
 * Created by Hongsec on 2016-08-22.
 */
public interface  DeviceInfoCallback {

    /**
     *
     * @param status  로그인 안됐을때 return false
     * @param deviceInfos
     */
    public void  onResponse(boolean status,ArrayList<DeviceInfo> deviceInfos);
}
