package fun.wxy.www.fragment2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import fun.wxy.www.fragment2.utils.MyBaseApplication;

public class BroadCastServiceStop extends BroadcastReceiver {
    private Handler handler;

    public BroadCastServiceStop() {
        this.handler = MyBaseApplication.getInstance().getShowMapHandler();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(handler != null){
            Message msg = handler.obtainMessage();
            msg.what = 603;
            handler.sendMessage(msg);
        }else{
            Toast.makeText(context,"哀牢山：接收到广播，但发生异常",Toast.LENGTH_LONG).show();
        }
    }
}
