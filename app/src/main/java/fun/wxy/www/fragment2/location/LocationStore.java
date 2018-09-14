package fun.wxy.www.fragment2.location;

import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;

import fun.wxy.www.fragment2.model.MyLocation;

public class LocationStore implements LocationDisplay.LocationChangedListener{

    private long recordCode;

    public LocationStore(long recordCode) {
        this.recordCode = recordCode;
    }

    @Override
    public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
        LocationDataSource.Location location = locationChangedEvent.getLocation();

        MyLocation myLocation = new MyLocation();

        myLocation.setTime(System.currentTimeMillis());
        myLocation.setLongitude(location.getPosition().getX());
        myLocation.setLatitude(location.getPosition().getY());
        myLocation.setIsLoad(1);
        myLocation.setRecordCode(recordCode);

        myLocation.save();
    }
}
