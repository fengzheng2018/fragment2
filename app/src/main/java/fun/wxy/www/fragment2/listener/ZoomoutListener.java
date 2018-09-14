package fun.wxy.www.fragment2.listener;

import android.view.View;
import android.widget.ZoomControls;

import com.esri.arcgisruntime.mapping.view.MapView;

public class ZoomoutListener implements View.OnClickListener {

    private MapView mapView;
    private ZoomControls zoomControls;

    public ZoomoutListener(MapView mapView,ZoomControls zoomControls){
        this.mapView = mapView;
        this.zoomControls = zoomControls;
    }

    @Override
    public void onClick(View v) {
        double mapScale = mapView.getMapScale();
        if(mapScale < 10000000.0){
            if( (mapScale * 2) >= 10000000.0){
                mapView.setViewpointScaleAsync(10000000.0);
            }else {
                mapView.setViewpointScaleAsync(mapScale*2);
            }
        }else {
            zoomControls.setIsZoomOutEnabled(false);
        }
    }
}
