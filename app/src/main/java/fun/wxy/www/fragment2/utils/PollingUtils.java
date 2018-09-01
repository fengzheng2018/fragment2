package fun.wxy.www.fragment2.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class PollingUtils {
    //请求码
    private final static int REQUEST_CODE = 301;
    //让新的PendingIntent替换旧的PendingIntent
    private final static int PENDING_TYPE = PendingIntent.FLAG_UPDATE_CURRENT;

    /**
     * 设置提醒
     * @param seconds 间隔执行的时间，单位：秒
     * @param cls 要启动的类
     */
    public static void startPollingService(Context context,int seconds,Class<?> cls){
        //在睡眠状态下会唤醒系统执行提示功能
        int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,cls);
        PendingIntent pendingIntent = PendingIntent.getService(context,REQUEST_CODE,intent,PENDING_TYPE);

        //自系统启动以来的时间，包括睡眠时间
        long triggerAtTime = SystemClock.elapsedRealtime();

        //重复提醒
        alarmManager.setRepeating( alarmType, triggerAtTime,seconds * 1000, pendingIntent);
    }

    /**
     * 停止提醒
     * @param cls 要停止的类
     */
    public static void stopPollingService(Context context,Class<?> cls){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,cls);
        PendingIntent pendingIntent = PendingIntent.getService(context,REQUEST_CODE,intent,PENDING_TYPE);

        alarmManager.cancel(pendingIntent);
    }
}
