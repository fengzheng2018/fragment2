package fun.wxy.www.fragment2.utils;

import android.location.LocationManager;

/**
 * 获取位置提供器
 */
public class LocationProvider {

    private LocationManager locationManager;

    /**
     * @param locationManager 传入LocationManager对象
     */
    public LocationProvider(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    /**
     * 获取位置提供器，若位置提供器都不能获取位置，返回null
     */
    public String getProvider(){
        // GPS定位
        String GPSProvider = LocationManager.GPS_PROVIDER;
        // 网络定位
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        // 被动定位
        String passiveProvider = LocationManager.PASSIVE_PROVIDER;

        if(judgeProvider(GPSProvider)){
            return GPSProvider;
        }else {
            if(judgeProvider(networkProvider)){
                return networkProvider;
            }else {
                if(judgeProvider(passiveProvider)){
                    return passiveProvider;
                }else {
                    return null;
                }
            }
        }
    }

    /**
     * 判断位置提供器是否可用
     * @param pro 传入位置提供器
     */
    private boolean judgeProvider(String pro){
        boolean isEnable = locationManager.isProviderEnabled(pro);

        // 判断位置提供器能否获取位置，不能获取位置返回false
        if(isEnable){
            try {
                return (locationManager.getLastKnownLocation(pro) != null);
            }catch (SecurityException e){
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }
}
