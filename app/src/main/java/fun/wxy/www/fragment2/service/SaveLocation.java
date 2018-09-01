package fun.wxy.www.fragment2.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.litepal.LitePal;

import java.util.List;

import fun.wxy.www.fragment2.location.LocationStore;
import fun.wxy.www.fragment2.model.MyRecord;
import fun.wxy.www.fragment2.notification.MyNotification;
import fun.wxy.www.fragment2.utils.LocationProvider;

public class SaveLocation extends Service {

    private LocationManager locationManager;
    private LocationStore locationStore;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        final int NOTIFICATION_ID = 11;
        MyNotification myNotification = new MyNotification(this);
        myNotification.createNotificationChanel();
        Notification notification = myNotification.showNotification();

        startForeground(NOTIFICATION_ID,notification);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.i("fz","存储位置的服务被启动");

        LocationProvider locationProvider = new LocationProvider(locationManager);
        final String pro = locationProvider.getProvider();

        if(pro != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();

                    long recordCode;

                    //检查是否存在没有完成的记录
                    List<MyRecord> myRecords =  LitePal.where("isOver = ?","1").find(MyRecord.class);
                    //存在没结束的记录
                    if(myRecords.size() > 0){
                        MyRecord myRecord = myRecords.get(0);
                        recordCode = myRecord.getRecordCode();
                    }else{
                        recordCode = System.currentTimeMillis();

                        MyRecord newRecord = new MyRecord();
                        newRecord.setIsOver(1);
                        newRecord.setRecordCode(recordCode);
                        newRecord.setRecordTime(recordCode);

                        newRecord.save();
                    }

                    try{
                        locationStore = new LocationStore(recordCode);
                        locationManager.requestLocationUpdates(pro,5000,1,locationStore);
                    }catch(SecurityException e){
                        e.printStackTrace();
                        //出现异常，停止服务
                        onDestroy();
                    }

                    Looper.loop();
                }
            }).start();
        }
        //处理没有获取到位置提供器的情况
        //.......

        //由于异常终止服务后，服务会重新创建
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            //移除监听器
            locationManager.removeUpdates(locationStore);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        stopForeground(true);

        Log.i("fz","存储位置的服务被销毁");
    }
}
