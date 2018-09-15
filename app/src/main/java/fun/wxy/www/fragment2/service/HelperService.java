package fun.wxy.www.fragment2.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import fun.wxy.www.fragment2.R;
import fun.wxy.www.fragment2.notification.MyNotification;
import fun.wxy.www.fragment2.utils.PollingUtils;

public class HelperService extends Service {

    private SetCheckTime setCheckTime;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        //设置定时检查位置
        setCheckTime = new SetCheckTime();
        setCheckTime.outAreaAlarm(10,10);

        Log.i("fz","HelperService已启动...");

        final int NOTIFICATION_ID = 12;

        MyNotification myNotification = new MyNotification(this);
        myNotification.createNotificationChanel();
        Notification notification = myNotification.showNotification(R.string.notification_status1,R.string.notification_ticker1,false);

        startForeground(NOTIFICATION_ID,notification);

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return setCheckTime;
    }


    @Override
    public void onDestroy() {
        PollingUtils.stopPollingService(CheckLocation.class);

        stopForeground(true);

        Log.i("fz","HelperService被销毁...");
    }


    public class SetCheckTime extends Binder{
        /**
         * 尚未进入区域设置定时
         */
        public void outAreaAlarm(double r,double d){
            int time = setOutTime(r,d);

            Log.i("fz","提醒时间间隔为："+time);

            PollingUtils.startPollingService(time,CheckLocation.class);
        }

        /**
         * 进入区域设置定时
         */
        public void inAreaAlarm(double r,double d){
            int time = setInTime( r, d);

            Log.i("fz","提醒时间间隔为："+time);

            PollingUtils.startPollingService(time,CheckLocation.class);
        }
    }

    /**
     * 根据不同的距离设置不同的提醒时间
     * @param r 监测半径
     * @param d 实际距离
     * @return 时间，单位：秒
     */
    private int setOutTime(double r,double d){

        int time;

        double distance = d - r;

        if(distance >= 5000){
            //距离大于5公里，半小时提醒
            time = 30*60;
        }else if(distance > 2000 && distance < 5000){
            //距离大于2公里，十分钟提醒
            time = 10*60;
        }else if(distance > 1000 && distance <= 2000){
            //距离大于1公里，5分钟提醒
            time = 5*60;
        }else{
            //距离小于1公里，1分钟提醒
            time = 1;
        }
        return time;
    }

    private int setInTime(double r, double d){

        int time;

        double scale = d/r;

        if(r < 500){
            time = 1;
        }else if(r >= 500 && r < 1000){
            if(scale < 0.5){
                time = 5;
            }else {
                time = 1;
            }
        }else {
            time = 5;
        }
        return time;
    }
}
