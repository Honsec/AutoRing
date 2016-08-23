package genius.autoring;

/**
 * Created by Hongsec on 2016-08-22.
 */
public interface MissionCallback {
    /**
     * 미션 다운로드 완료
     * @param pkg
     */
    public void onInstalled(String pkg);

    /**
     * 미션 참여 신청
     * @param pkg
     */
    public void onPartIn(String pkg);
}
