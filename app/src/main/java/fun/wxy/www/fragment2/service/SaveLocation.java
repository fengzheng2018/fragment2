package fun.wxy.www.fragment2.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;

import org.litepal.LitePal;

import java.util.List;

import fun.wxy.www.fragment2.location.LocationStore;
import fun.wxy.www.fragment2.model.MyRecord;
import fun.wxy.www.fragment2.notification.MyNotification;
import fun.wxy.www.fragment2.utils.MyBaseApplication;

public class SaveLocation extends Service {

    private LocationDisplay locationDisplay;
    private LocationStore locationStore;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        final int NOTIFICATION_ID = 11;
        String sms = "正在记录你的位置...";

        MyNotification myNotification = new MyNotification(this);
        myNotification.createNotificationChanel();
        Notification notification = myNotification.showNotification(sms);

        startForeground(NOTIFICATION_ID,notification);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        Log.i("fz","存储位置的服务被启动");

        MyBaseApplication baseApplication = MyBaseApplication.getInstance();
        locationDisplay = baseApplication.getMapView().getLocationDisplay();
        if(locationDisplay != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //记录标志位，标志属于哪一次记录
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

                    //监听位置变化
                    locationStore = new LocationStore(recordCode);
                    locationDisplay.addLocationChangedListener(locationStore);

                }
            }).start();
        }

        //由于异常终止服务后，服务会重新创建
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            //移除监听器
            locationDisplay.removeLocationChangedListener(locationStore);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        stopForeground(true);

        Log.i("fz","存储位置的服务被销毁");
    }
}
