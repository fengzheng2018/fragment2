package fun.wxy.www.fragment2.location;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;

public class LocationProvider {
    private Context mContext;
    private LocationManager locationManager;

    public LocationProvider(Context mContext,LocationManager locationManager) {
        this.mContext = mContext;
        this.locationManager = locationManager;
    }

    public String initLocation(){
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);

        String locationProvider = null;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        return locationProvider;
    }
}
