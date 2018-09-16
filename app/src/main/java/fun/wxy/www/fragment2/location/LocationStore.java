package fun.wxy.www.fragment2.location;

import android.os.Message;
import android.util.Log;

import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;

import fun.wxy.www.fragment2.model.MyLocation;
import fun.wxy.www.fragment2.utils.MyHandlerToShowMap;

public class LocationStore implements LocationDisplay.LocationChangedListener{

    private long recordCode;
    private PointCollection pointCollection;
    private MyHandlerToShowMap myHandler;

    public LocationStore(long recordCode) {
        this.recordCode = recordCode;

        pointCollection = new PointCollection(SpatialReferences.getWgs84());
    }

    @Override
    public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
        LocationDataSource.Location location = locationChangedEvent.getLocation();

        MyLocation myLocation = new MyLocation();

        double lat = location.getPosition().getY();
        double lng = location.getPosition().getX();

        myLocation.setTime(System.currentTimeMillis());
        myLocation.setLongitude(lng);
        myLocation.setLatitude(lat);
        myLocation.setIsLoad(1);
        myLocation.setRecordCode(recordCode);

        myLocation.save();

        pointCollection.add(lng,lat);

        if(myHandler != null){
            Message msg = myHandler.obtainMessage();
            msg.what = 602;
            msg.obj = pointCollection;
            myHandler.sendMessage(msg);
        }else{
            Log.i("fz","存储位置的服务handler为空");
        }
    }

    public void setMyHandler(MyHandlerToShowMap myHandler){
        this.myHandler = myHandler;
        Log.i("fz","handler已经在存储位置的服务类中设置");
    }
}
