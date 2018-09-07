package fun.wxy.www.fragment2.listener;

import android.view.View;
import android.widget.ImageButton;

import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MyMapScaleListener implements MapScaleChangedListener {
    private ImageButton zoomIn;
    private ImageButton zoomOut;

    public MyMapScaleListener(ImageButton in,ImageButton out){
        this.zoomIn = in;
        this.zoomOut = out;
    }

    @Override
    public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {

        MapView mMapView = mapScaleChangedEvent.getSource();
        double mapScale = mMapView.getMapScale();

        if(mapScale < 2000.0){
            mMapView.setViewpointScaleAsync(2000.0);
            zoomIn.setVisibility(View.INVISIBLE);
        }else if(mapScale > 10000000.0){
            mMapView.setViewpointScaleAsync(10000000.0);
            zoomOut.setVisibility(View.INVISIBLE);
        }else if(mapScale > 2000.0){
            zoomIn.setVisibility(View.VISIBLE);
        }else if(mapScale < 10000000.0){
            zoomOut.setVisibility(View.VISIBLE);
        }
    }
}
