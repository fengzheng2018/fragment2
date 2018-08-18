package fun.wxy.www.fragment2.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import fun.wxy.www.fragment2.permission.CheckInternet;
import fun.wxy.www.fragment2.permission.CheckPermissions;

public class ShowMap {

    private Activity mActivity;
    private Context mContext;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    private int requestLocateCode = 101;

    public ShowMap(Activity mActivity, Context mContext,MapView mapView) {
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.mMapView = mapView;
    }

    public void initMap(){
        ArcGISMap arcGISMap = new ArcGISMap(Basemap.createImagery());
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

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
}
