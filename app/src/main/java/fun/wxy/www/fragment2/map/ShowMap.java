package fun.wxy.www.fragment2.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.lang.ref.WeakReference;

import fun.wxy.www.fragment2.listener.MyMapScaleListener;
import fun.wxy.www.fragment2.permission.CheckInternet;
import fun.wxy.www.fragment2.permission.CheckPermissions;

public class ShowMap {

    private Activity mActivity;
    private Context mContext;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    private ImageButton dingwei,zoomIn,zoomOut;

    private int requestLocateCode = 101;

    public ShowMap(Activity mActivity, Context mContext,MapView mapView,ImageButton dingwei,ImageButton zoomIn,ImageButton zoomOut) {
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.mMapView = mapView;

        this.dingwei  = dingwei;
        this.zoomIn = zoomIn;
        this.zoomOut = zoomOut;
    }

    public void initMap(){
        ArcGISMap arcGISMap = new ArcGISMap(Basemap.createImagery());
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
                mMapView.addMapScaleChangedListener(new MyMapScaleListener(zoomIn,zoomOut));
            }
        });

        //要素图层
        final String layerURL = "http://202.203.134.147:6080/arcgis/rest/services/AilaoFeature/AilaoFeatureService/MapServer";
        final ArcGISMapImageLayer mapImageLayer = new ArcGISMapImageLayer(layerURL);
        arcGISMap.getOperationalLayers().add(mapImageLayer);

        mMapView.setMap(arcGISMap);
    }

    public void drawMap(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        CheckPermissions checkPermissions = new CheckPermissions(mActivity,mContext);
        if(checkPermissions.checkPermission(permissions)){
            //具有定位权限，开启服务
            //Intent intent = new Intent(mActivity, SaveLocation.class);
            //mContext.startService(intent);

            // 开启提醒
            //PollingUtils.startPollingService(mContext,1, CheckLocation.class);

            //检查是否具有联网的权限
            if(CheckInternet.isNetworkAvailable(mContext)){
                mLocationDisplay.startAsync();
            }else{
                CheckInternet.settingNetwork(mActivity);
            }
        }else {
            String rational = "无法获取到定位权限软件将无法使用，现在去设置界面打开定位权限？";
            checkPermissions.requestPermissions(rational,requestLocateCode,permissions);
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
                        zoomIn.setVisibility(View.VISIBLE);
                        zoomOut.setVisibility(View.VISIBLE);

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
