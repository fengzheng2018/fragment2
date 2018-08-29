package fun.wxy.www.fragment2.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import fun.wxy.www.fragment2.R;

public class MyNotification {

    private Context mContext;

    //通道ID
    private final String CHANEL_ID = "MY_NOTIFICATION_ID";

    public MyNotification(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * API级别为26+时需要创建通道
     */
    public void createNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //通知管理面板显示的通知名称
            CharSequence name = mContext.getString(R.string.notification_name);

            //用户在系统设置中看到的描述
            String description = mContext.getString(R.string.notification_description);

            //通道重要性级别
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANEL_ID,name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 创建通知
     */
    public Notification showNotification(){
        return new NotificationCompat.Builder(mContext,CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher))
                .setContentTitle("哀牢山")
                .setContentText("正在记录你的位置...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }
}
