package fun.wxy.www.fragment2.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ZoomControls;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.lang.ref.WeakReference;

import fun.wxy.www.fragment2.listener.MyMapScaleListener;
import fun.wxy.www.fragment2.permission.CheckInternet;
import fun.wxy.www.fragment2.permission.CheckPermissions;
import fun.wxy.www.fragment2.service.HelperService;
import fun.wxy.www.fragment2.utils.MyBaseApplication;

public class ShowMap {

    private Activity mActivity;
    private Context mContext;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    private ZoomControls zoomControls;
    private ImageButton dingwei;

    public ShowMap(Activity mActivity,  ImageButton dingwei, ZoomControls zoomControls) {
        this.mActivity = mActivity;

        this.dingwei  = dingwei;
        this.zoomControls = zoomControls;

        MyBaseApplication baseApplication = MyBaseApplication.getInstance();

        this.mContext = baseApplication.getContext();
        this.mMapView = baseApplication.getMapView();
    }

    public void initMap(){
        ArcGISMap arcGISMap = new ArcGISMap(Basemap.Type.IMAGERY,25.063463,102.758171,16);
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

        //初始化Handler
        final MyHandler handler = new MyHandler(this);

        // ArcGISMap设置监听
        arcGISMap.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 501;
                handler.sendMessage(msg);

                //设置比例缩放监听
                mMapView.addMapScaleChangedListener(new MyMapScaleListener(zoomControls));
            }
        });

        //要素图层
        final String layerURL = "http://202.203.134.147:6080/arcgis/rest/services/AilaoFeature/AilaoFeatureService/MapServer";
        final ArcGISMapImageLayer mapImageLayer = new ArcGISMapImageLayer(layerURL);
        arcGISMap.getOperationalLayers().add(mapImageLayer);

        mMapView.setMap(arcGISMap);
    }

    public void drawMap(){
        //已经有了定位权限
        if(permissions()){
            //检查是否具有联网的权限
            if(CheckInternet.isNetworkAvailable(mContext)){
                mLocationDisplay.startAsync();
            }else{
                CheckInternet.settingNetwork(mActivity);
            }
        }
    }

    /**
     * 开启HelperService服务
     */
    public void startAlarm(){
        if(permissions()){
            // 开启服务
            Intent startHelperService = new Intent(mContext, HelperService.class);
            mContext.startService(startHelperService);
        }
    }

    /**
     * 检查定位权限
     */
    private boolean permissions(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        CheckPermissions checkPermissions = new CheckPermissions(mActivity,mContext);

        //请求码
        int requestLocateCode = 101;

        if(checkPermissions.checkPermission(permissions)){
            return true;
        }else {
            String rational = "无法获取到定位权限软件将无法使用，现在去设置界面打开定位权限？";
            checkPermissions.requestPermissions(rational,requestLocateCode,permissions);
            return false;
        }
    }


    /**
     * 内部类，显示定位、缩放按钮
     * 弱引用
     */
    private class MyHandler extends Handler{
        private final WeakReference<ShowMap> showMapWeakReference;

        private MyHandler(ShowMap showMap){
            showMapWeakReference = new WeakReference<>(showMap);
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            ShowMap mShowMap = showMapWeakReference.get();

            if(mShowMap != null){
                switch (msg.what){
                    case 501:{
                        dingwei.setVisibility(View.VISIBLE);
                        zoomControls.setVisibility(View.VISIBLE);

                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }
    }
}
