package fun.wxy.www.fragment2.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import fun.wxy.www.fragment2.model.MyLocation;

public class LocationStore implements LocationListener{
    private long recordCode;

    public LocationStore(long recordCode) {
        this.recordCode = recordCode;
    }

    @Override
    public void onLocationChanged(Location location) {
        MyLocation myLocation = new MyLocation();

        myLocation.setTime(System.currentTimeMillis());
        myLocation.setLongitude(location.getLongitude());
        myLocation.setLatitude(location.getLatitude());
        myLocation.setIsLoad(1);
        myLocation.setRecordCode(recordCode);

        myLocation.save();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
