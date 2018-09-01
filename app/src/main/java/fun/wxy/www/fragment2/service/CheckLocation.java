package fun.wxy.www.fragment2.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import fun.wxy.www.fragment2.model.MyRecord;
import fun.wxy.www.fragment2.utils.LocationProvider;

public class CheckLocation extends IntentService {

    public CheckLocation() {
        super("CheckLocation");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 测试位置
        final double lat2 = 25.063463;
        final double lng2 = 102.758171;
        //测试半径
        final double testRadius = 20.0;

        LocationProvider locationProvider = new LocationProvider(locationManager);
        String pro = locationProvider.getProvider();

        if(pro != null){

            try{
                Location location = locationManager.getLastKnownLocation(pro);
                double lat1 = location.getLatitude();
                double lng1 = location.getLongitude();

                double distance = distanceOfTwoPoints(lat1,lng1,lat2,lng2);

                //判断服务是否在运行
                boolean isServiceRun = isServiceExisted(SaveLocation.class.getName());

                Log.i("fz","计算后的距离为："+distance);
                Log.i("fz","判断服务是否运行："+isServiceRun);

                //进入指定区域，开始记录位置
                if(distance <= testRadius){
                    //服务没有运行，开启服务
                    if(!isServiceRun){
                        Intent saveLocationService = new Intent(this,SaveLocation.class);
                        startService(saveLocationService);
                    }
                }else {
                    //记录位置结束，将数据表中结束标志位置2
                    if(isServiceRun){
                        MyRecord myRecord = new MyRecord();
                        myRecord.setIsOver(2);
                        myRecord.updateAll("isOver=?","1");

                        //关闭服务
                        Intent stopLocationService = new Intent(this,SaveLocation.class);
                        stopService(stopLocationService);
                    }
                }
            }catch (SecurityException e){
                e.printStackTrace();
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
}
