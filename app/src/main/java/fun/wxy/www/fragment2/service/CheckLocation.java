package fun.wxy.www.fragment2.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;

import java.util.List;

import fun.wxy.www.fragment2.model.MyRecord;
import fun.wxy.www.fragment2.utils.MyBaseApplication;

public class CheckLocation extends IntentService {

    private double distance;

    //测试半径
    private final double testRadius = 100.0;
    //服务是否在运行
    private boolean isServiceRun = false;

    public CheckLocation() {
        super("CheckLocation");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        MyBaseApplication baseApplication = MyBaseApplication.getInstance();

        LocationDisplay locationDisplay = baseApplication.getMapView().getLocationDisplay();
        if(locationDisplay != null){

            locationDisplay.startAsync();
            locationDisplay.addLocationChangedListener(new MyLocationChangeListener(locationDisplay));

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("fz","检查位置的服务类被销毁");
    }

    /**
     * 位置改变监听内部类
     */
    private class MyLocationChangeListener implements LocationDisplay.LocationChangedListener{

        private LocationDisplay locationDisplay;

        private MyLocationChangeListener(LocationDisplay locationDisplay) {
            this.locationDisplay = locationDisplay;
        }

        // 测试位置
        final double lat2 = 25.063463;
        final double lng2 = 102.758171;

        @Override
        public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
            LocationDataSource.Location location = locationChangedEvent.getLocation();

            if(location != null){
                double lat1 = location.getPosition().getY();
                double lng1 = location.getPosition().getX();

                Log.i("fz","获取的经度："+lat1+"，获取的纬度："+lng1);

                distance = distanceOfTwoPoints(lat1,lng1,lat2,lng2);

                //判断服务是否在运行
                isServiceRun = isServiceExisted(SaveLocation.class.getName());

                Log.i("fz","计算后的距离为："+distance);
                Log.i("fz","判断服务是否运行："+isServiceRun);

                //进入指定区域，开始记录位置
                if(distance <= testRadius){
                    //服务没有运行，开启服务
                    if(!isServiceRun){
                        Log.i("fz","准备开启保存位置的服务");

                        Intent saveLocationService = new Intent(CheckLocation.this,SaveLocation.class);
                        startService(saveLocationService);
                    }
                }else {
                    //记录位置结束，将数据表中结束标志位置2
                    if(isServiceRun){
                        MyRecord myRecord = new MyRecord();
                        myRecord.setIsOver(2);
                        myRecord.updateAll("isOver=?","1");

                        //关闭服务
                        Log.i("fz","准备关闭保存位置的服务");
                        Intent stopLocationService = new Intent(CheckLocation.this,SaveLocation.class);
                        stopService(stopLocationService);
                    }
                }

                //绑定服务
                Intent bindHelper = new Intent(CheckLocation.this,HelperService.class);
                bindService(bindHelper,connService,BIND_AUTO_CREATE);

                locationDisplay.stop();
                //结束监听
                locationDisplay.removeLocationChangedListener(MyLocationChangeListener.this);
            }

        }
    }



    /**
     * 计算两点之间的距离
     * @param lat1 第一个位置纬度
     * @param lng1 第一个位置经度
     * @param lat2 第二个位置纬度
     * @param lng2 第二个位置经度
     * @return 返回距离，单位：米
     */
    private double distanceOfTwoPoints(double lat1,double lng1,double lat2,double lng2){
        final double RAD = Math.PI/180.0;
        final double EARTH_RADIUS = 6378137;

        double radLat1 = lat1 * RAD;
        double radLat2 = lat2 * RAD;

        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * RAD;

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;

        return s;
    }


    /**
     * 判断服务是否运行
     * @param className 要检查的服务类名
     * @return 服务在运行，返回true
     */
    private boolean isServiceExisted(String className){

        boolean isWork = false;

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceInfoList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if(serviceInfoList.size() <= 0){
            return false;
        }else {
            for(int i=0; i<serviceInfoList.size(); i++){
                String name = serviceInfoList.get(i).service.getClassName();
                if(name.equals(className)){
                    isWork = true;
                    break;
                }
            }
        }
        return isWork;
    }


    private ServiceConnection connService = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            HelperService.SetCheckTime setCheckTime = (HelperService.SetCheckTime) service;
            if(isServiceRun){
                setCheckTime.inAreaAlarm(testRadius,distance);
            }else {
                setCheckTime.outAreaAlarm(testRadius,distance);
            }

            //解绑服务
            unbindService(connService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //服务已被系统杀死，提示用户
        }
    };
}
