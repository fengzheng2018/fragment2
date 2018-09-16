package fun.wxy.www.fragment2.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;

import org.litepal.LitePal;

import java.util.List;

import fun.wxy.www.fragment2.R;
import fun.wxy.www.fragment2.broadcast.BroadCastServiceStart;
import fun.wxy.www.fragment2.location.LocationStore;
import fun.wxy.www.fragment2.model.MyRecord;
import fun.wxy.www.fragment2.notification.MyNotification;
import fun.wxy.www.fragment2.utils.MyBaseApplication;
import fun.wxy.www.fragment2.utils.MyHandlerToShowMap;

public class SaveLocation extends Service {

    private LocationDisplay locationDisplay;
    private LocationStore locationStore;

    private MyHandlerToShowMap myHandler;
    private BroadCastServiceStart broadCastReceiver;
    private LocalBroadcastManager broadcastManager;

    @Override
    public IBinder onBind(Intent intent) {
        return new SetHandlerBinder();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        final int NOTIFICATION_ID = 11;

        MyNotification myNotification = new MyNotification(this);
        myNotification.createNotificationChanel();
        Notification notification = myNotification.showNotification(R.string.notification_status2,R.string.notification_ticker2,true);

        startForeground(NOTIFICATION_ID,notification);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){

        Log.i("fz","存储位置的服务被启动");

        MyBaseApplication baseApplication = MyBaseApplication.getInstance();
        locationDisplay = baseApplication.getMapView().getLocationDisplay();

        myHandler = baseApplication.getShowMapHandler();

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

                    //获取LocalBroadCastManager实例
                    broadcastManager = LocalBroadcastManager.getInstance(SaveLocation.this);
                    //注册广播监听器
                    broadCastReceiver = new BroadCastServiceStart();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("fun.wxy.www.saveLocationStart");
                    broadcastManager.registerReceiver(broadCastReceiver,intentFilter);
                    //发送服务开始的广播
                    Intent serviceStartFlag = new Intent("fun.wxy.www.saveLocationStart");
                    broadcastManager.sendBroadcast(serviceStartFlag);

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

        //移除广播接收器
        broadcastManager.unregisterReceiver(broadCastReceiver);

        Log.i("fz","存储位置的服务被销毁");
    }


    /**
     * 内部类，设置Handler对象
     */
    public class SetHandlerBinder extends Binder{
        public void setHandler(){
            locationStore.setMyHandler(myHandler);
        }
    }
}
