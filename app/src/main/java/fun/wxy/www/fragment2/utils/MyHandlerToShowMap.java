package fun.wxy.www.fragment2.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.esri.arcgisruntime.geometry.PointCollection;

import java.lang.ref.WeakReference;

import fun.wxy.www.fragment2.map.ShowMap;
import fun.wxy.www.fragment2.map.ShowRouteLine;

public class MyHandlerToShowMap extends Handler {
    private final WeakReference<ShowMap> showMapWeakReference;
    private final WeakReference<ShowRouteLine> showRouteLineWeakReference;

    public MyHandlerToShowMap(ShowMap showMap,ShowRouteLine routeLine){
        showMapWeakReference = new WeakReference<>(showMap);
        showRouteLineWeakReference = new WeakReference<>(routeLine);
    }

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);

        ShowMap mShowMap = showMapWeakReference.get();
        ShowRouteLine showRouteLine = showRouteLineWeakReference.get();

        if(mShowMap != null){
            switch (msg.what){
                //ArcGISMap加载完成
                case 501:{
                    mShowMap.dingwei.setVisibility(View.VISIBLE);
                    mShowMap.zoomControls.setVisibility(View.VISIBLE);

                    break;
                }
                //LocationStore服务启动完成
                case 601:{
                    showRouteLine.bindTheService();
//                    if(showRouteLine == null){
//                        Log.i("fz","ShowRouteLine为空");
//                    }

                    break;
                }
                //开始有记录
                case 602:{
                    Log.i("fz","message的ID值为602");
                    PointCollection pointCollection = (PointCollection) msg.obj;
                    mShowMap.drawMapLine(showRouteLine.drawLine(pointCollection));

                    break;
                }
                //记录结束
                case 603:{
                    try{
                        showRouteLine.unbindTheService();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }
}
