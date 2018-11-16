package fun.wxy.www.fragment2.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.ImageButton;
import android.widget.ZoomControls;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import fun.wxy.www.fragment2.listener.MyMapScaleListener;
import fun.wxy.www.fragment2.permission.CheckInternet;
import fun.wxy.www.fragment2.permission.CheckPermissions;
import fun.wxy.www.fragment2.service.HelperService;
import fun.wxy.www.fragment2.service.SaveLocation;
import fun.wxy.www.fragment2.utils.MyBaseApplication;
import fun.wxy.www.fragment2.utils.MyHandlerToShowMap;
import fun.wxy.www.fragment2.utils.ServiceIsRun;

public class ShowMap {

    private Activity mActivity;
    private Context mContext;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    private GraphicsOverlay graphicsOverlay;

    private MyBaseApplication baseApplication;

    public ZoomControls zoomControls;
    public ImageButton dingwei;

    public ShowMap(Activity mActivity,  ImageButton dingwei, ZoomControls zoomControls) {
        this.mActivity = mActivity;

        this.dingwei  = dingwei;
        this.zoomControls = zoomControls;

        this.baseApplication = MyBaseApplication.getInstance();

        this.mContext = baseApplication.getContext();
        this.mMapView = baseApplication.getMapView();
    }

    public void initMap(){
        final ArcGISMap arcGISMap = new ArcGISMap(Basemap.Type.IMAGERY,25.063463,102.758171,16);
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

        //初始化Handler
        ShowRouteLine showRouteLine = new ShowRouteLine();
        final MyHandlerToShowMap handler = new MyHandlerToShowMap(this,showRouteLine);
        baseApplication.setShowMapHandler(handler);


        // ArcGISMap设置监听
        arcGISMap.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if(arcGISMap.getLoadStatus().equals(LoadStatus.LOADED)){
                    //从Message缓存池中取出message对象
                    Message msg = handler.obtainMessage();
                    msg.what = 501;
                    handler.sendMessage(msg);

                    //设置比例缩放监听
                    mMapView.addMapScaleChangedListener(new MyMapScaleListener(zoomControls));

                    //检查服务是否在运行
                    boolean isServiceRun = ServiceIsRun.isServiceExisted(SaveLocation.class.getName(),mContext);
                    if(isServiceRun){
                        Message message = handler.obtainMessage();
                        message.what = 601;
                        handler.sendMessage(message);
                    }
                    //服务被杀死，但记录还没有完成的情况
                    //...
                }
            }
        });

        //要素图层
        final String layerURL = "http://202.203.134.147:6080/arcgis/rest/services/AilaoFeature/AilaoFeatureService/MapServer";
        final ArcGISMapImageLayer mapImageLayer = new ArcGISMapImageLayer(layerURL);
        arcGISMap.getOperationalLayers().add(mapImageLayer);

        mMapView.setMap(arcGISMap);

        //添加路径
        graphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(graphicsOverlay);
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
            //检查服务是否运行
            boolean isHelperServiceRun = ServiceIsRun.isServiceExisted(HelperService.class.getName(),mContext);
            if(!isHelperServiceRun){
                Intent startHelperService = new Intent(mContext, HelperService.class);
                mContext.startService(startHelperService);
            }
        }
    }


    public void drawMapLine(Graphic graphic){
        if(graphic != null){
            graphicsOverlay.getGraphics().add(graphic);
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
}
