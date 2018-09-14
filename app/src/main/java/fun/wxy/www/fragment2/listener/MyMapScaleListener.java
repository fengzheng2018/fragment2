package fun.wxy.www.fragment2.listener;

import android.util.Log;
import android.widget.ZoomControls;

import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MyMapScaleListener implements MapScaleChangedListener {
    private ZoomControls zoomControls;

    public MyMapScaleListener(ZoomControls zoomControls){
        this.zoomControls = zoomControls;
    }

    @Override
    public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {

        MapView mMapView = mapScaleChangedEvent.getSource();
        double mapScale = mMapView.getMapScale();

        Log.i("fz","地图比例："+mapScale);

        if(mapScale < 2000.0){
            mMapView.setViewpointScaleAsync(2000.0);
            zoomControls.setIsZoomInEnabled(false);
        }else if(mapScale > 10000000.0){
            mMapView.setViewpointScaleAsync(10000000.0);
            zoomControls.setIsZoomOutEnabled(false);
        }else {
            zoomControls.setIsZoomInEnabled(true);
            zoomControls.setIsZoomOutEnabled(true);
        }
    }
}
