package fun.wxy.www.fragment2.permission;

import android.app.Activity;
import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

public class CheckPermissions {

    private Activity mActivity;
    private Context mContext;

    public CheckPermissions(Activity mActivity, Context mContext) {
        this.mActivity = mActivity;
        this.mContext = mContext;
    }

    public boolean checkPermission(String[] permissions){
        if(EasyPermissions.hasPermissions(mContext,permissions)){
            return true;
        }
        return false;
    }

    public void requestPermissions(String rational,int requestCode,String[] permissions){
        EasyPermissions.requestPermissions(mActivity,rational,requestCode,permissions);
    }
}
