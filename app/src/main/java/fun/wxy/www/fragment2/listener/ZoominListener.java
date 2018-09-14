package fun.wxy.www.fragment2.listener;

import android.view.View;
import android.widget.ZoomControls;

import com.esri.arcgisruntime.mapping.view.MapView;

public class ZoominListener implements View.OnClickListener {

    private MapView mapView;
    private ZoomControls zoomControls;

    public ZoominListener(MapView mapView,ZoomControls zoomControls){
        this.mapView = mapView;
        this.zoomControls = zoomControls;
    }

    @Override
    public void onClick(View v) {
        double mapScale = mapView.getMapScale();
        if(mapScale > 2000.0){
            if( (mapScale * 0.5) <= 2000.0){
                mapView.setViewpointScaleAsync(2000.0);
            }else {
                mapView.setViewpointScaleAsync(mapScale*0.5);
            }
        }else {
            zoomControls.setIsZoomInEnabled(false);
        }
    }
}
