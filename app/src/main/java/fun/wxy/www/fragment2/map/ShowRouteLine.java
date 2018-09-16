package fun.wxy.www.fragment2.map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import fun.wxy.www.fragment2.service.SaveLocation;
import fun.wxy.www.fragment2.utils.MyBaseApplication;

public class ShowRouteLine {

    private Context context;
    private SimpleLineSymbol lineSymbol;

    ShowRouteLine() {

        MyBaseApplication baseApplication = MyBaseApplication.getInstance();

        this.context = baseApplication.getContext();

        lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 3.0f);
    }

    public void bindTheService(){

        Log.i("fzh","ShowRouteLine准备绑定SaveLocation服务");
        Intent intent = new Intent(this.context,SaveLocation.class);
        context.bindService(intent,saveLocationServiceConn,Context.BIND_AUTO_CREATE);
    }

    public void unbindTheService(){
        context.unbindService(saveLocationServiceConn);
    }

    public Graphic drawLine(PointCollection pointCollection){
        Log.i("fz","开始绘制线条");
        return new Graphic(new Polyline(pointCollection),lineSymbol);
    }

    private ServiceConnection saveLocationServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SaveLocation.SetHandlerBinder handlerBinder = (SaveLocation.SetHandlerBinder) service;
            Log.i("fzh","开始设置存储位置服务类的handler");
            handlerBinder.setHandler();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //存储位置的服务被销毁
        }
    };
}
