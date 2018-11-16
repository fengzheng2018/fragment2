package fun.wxy.www.fragment2.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.esri.arcgisruntime.mapping.view.MapView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MyBaseApplication extends Application {

    private static MyBaseApplication baseApplication;

    private Context context;
    private MapView mapView;

    private MyHandlerToShowMap showMapHandler;

    private List<Activity> activityList;

    @Override
    public void onCreate(){
        super.onCreate();
        LitePal.initialize(MyBaseApplication.this);

        baseApplication = MyBaseApplication.this;

        activityList = new ArrayList<>();
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

    public MyHandlerToShowMap getShowMapHandler() {
        return showMapHandler;
    }

    public void setShowMapHandler(MyHandlerToShowMap showMapHandler) {
        this.showMapHandler = showMapHandler;
    }

    /**
     * 添加activity
     */
    public void addActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(activity);
        }
    }

    /**
     * 销毁单个activity
     */
    public void removeActivity(Activity activity){
        if(activityList.contains(activity)){
            activityList.remove(activity);
            activity.finish();
        }
    }

    /**
     * 销毁所有activity
     */
    public void removeAllActivity(){
        for(Activity activity : activityList){
            activity.finish();
        }
    }
}
