package fun.wxy.www.fragment2.utils;

import android.app.Application;
import android.content.Context;

import com.esri.arcgisruntime.mapping.view.MapView;

import org.litepal.LitePal;

public class MyBaseApplication extends Application {

    private static MyBaseApplication baseApplication;

    private Context context;
    private MapView mapView;

    @Override
    public void onCreate(){
        super.onCreate();
        LitePal.initialize(MyBaseApplication.this);

        baseApplication = MyBaseApplication.this;
    }

    public static MyBaseApplication getInstance(){
        return  baseApplication;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public Context aContext(){
        return MyBaseApplication.this;
    }
}
