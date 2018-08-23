package fun.wxy.www.fragment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import java.util.List;

public class ShowRoute_Main extends AppCompatActivity {

    private Context mContext;

    private MapView mMapView;
    private ArcGISMap arcGISMap;
    private LocationDisplay mLocationDisplay;

    private GraphicsOverlay graphicsOverlay;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_show_route__main);

        mContext = ShowRoute_Main.this;
    }

    @Override
    protected void onStart(){
        super.onStart();

        mMapView = findViewById(R.id.MapView_showRoute);
        arcGISMap = new ArcGISMap(Basemap.createImagery());
        mMapView.setMap(arcGISMap);

        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

        //要素图层
        final String layerURL = "http://202.203.134.147:6080/arcgis/rest/services/AilaoFeature/AilaoFeatureService/MapServer";
        final ArcGISMapImageLayer mapImageLayer = new ArcGISMapImageLayer(layerURL);
        arcGISMap.getOperationalLayers().add(mapImageLayer);

        mMapView.setMap(arcGISMap);

        mLocationDisplay.startAsync();

        graphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(graphicsOverlay);

        String pro = getProvider();
        addRouteLine(pro);
    }

    private void addRouteLine(String pro){
        try{
            final SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 3.0f);
            final PointCollection pointCollection = new PointCollection(SpatialReferences.getWgs84());

            locationManager.requestLocationUpdates(pro, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    pointCollection.add(location.getLongitude(),location.getLatitude());
                    Polyline mPolyline = new Polyline(pointCollection);

                    Graphic graphic = new Graphic(mPolyline,lineSymbol);

                    graphicsOverlay.getGraphics().add(graphic);
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
            });

        }catch(SecurityException e){
            e.printStackTrace();
        }

    }

    //获取位置提供器
    private String getProvider(){
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);

        String locationProvider = null;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(i);
            return null;
        }
        return locationProvider;
    }
}
