package fun.wxy.www.fragment2.listener;

import android.view.View;

import com.esri.arcgisruntime.mapping.view.MapView;

import fun.wxy.www.fragment2.R;
import fun.wxy.www.fragment2.map.ShowMap;

public class MapZoomingPositionListener implements View.OnClickListener {
    private ShowMap showMap;
    private MapView mapView;

    public MapZoomingPositionListener(ShowMap showMap, MapView mapView){
        this.showMap = showMap;
        this.mapView = mapView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_dingwei:{
                showMap.drawMap();
                break;
            }
            case R.id.button_zoom_in:{
                double mapScale = mapView.getMapScale();
                if(mapScale > 2000.0){
                    if( (mapScale * 0.5) <= 2000.0){
                        mapView.setViewpointScaleAsync(2000.0);
                    }else {
                        mapView.setViewpointScaleAsync(mapScale*0.5);
                    }
                }
                break;
            }
            case R.id.button_zoom_out:{
                double mapScale = mapView.getMapScale();
                if(mapScale < 10000000.0){
                    if( (mapScale * 2) >= 10000000.0){
                        mapView.setViewpointScaleAsync(10000000.0);
                    }else {
                        mapView.setViewpointScaleAsync(mapScale*2);
                    }
                }
                break;
            }
        }
    }
}
