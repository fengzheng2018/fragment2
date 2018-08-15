package fun.wxy.www.fragment2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.esri.arcgisruntime.arcgisservices.ArcGISMapServiceInfo;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import fun.wxy.www.fragment2.navigation.BottomNavigationViewHelper;
import fun.wxy.www.fragment2.navigation.MyRecycleViewAdapter;
import fun.wxy.www.fragment2.navigation.NavigationItemSpace;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context mContext;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;

    private boolean internetStatus = true;

    private final String layerURL = "http://202.203.134.147:6080/arcgis/rest/services/AilaoFeature/AilaoFeatureService/MapServer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        //初始化item
        MyRecycleViewAdapter myRecycleViewAdapter = new MyRecycleViewAdapter();
        myRecycleViewAdapter.initItemList();

        RecyclerView mRecyclerView = findViewById(R.id.navigation_left_recyclerView);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(myRecycleViewAdapter);
        mRecyclerView.addItemDecoration(new NavigationItemSpace());

        //顶部toolBar
        Toolbar mToolbar = findViewById(R.id.toolBar_top_container);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        //侧边滑动栏
        mDrawerLayout = findViewById(R.id.DrawerLayout_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //底部导航栏
        BottomNavigationView mBottomNavigationView = findViewById(R.id.BottomNavigation_bottom_container);
        BottomNavigationViewHelper.disableShiftModel(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //中间内容显示区域
        mMapView = findViewById(R.id.MapView_center_container);
        ArcGISMap arcGISMap = new ArcGISMap(Basemap.createImagery());
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);

        final ArcGISMapImageLayer mapImageLayer = new ArcGISMapImageLayer(layerURL);
        mapImageLayer.addDoneLoadingListener(new Runnable() {
            @Override
            public void run(){
                if(mapImageLayer.getLoadStatus()== LoadStatus.LOADED){
                    ArcGISMapServiceInfo mapServiceInfo=mapImageLayer.getMapServiceInfo();
                    Log.v("test2==>","图层加载成功");
                }
            }
        });
        arcGISMap.getOperationalLayers().add(mapImageLayer);

        mMapView.setMap(arcGISMap);

        MainActivityPermissionsDispatcher.checkInternetWithPermissionCheck(MainActivity.this);
        MainActivityPermissionsDispatcher.getLocationProviderWithPermissionCheck(MainActivity.this);
    }

    //满足权限，显示地图
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getLocationProvider(){
        if(internetStatus){
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            mLocationDisplay.startAsync();
        }else{
            Toast.makeText(mContext,"没有网络访问权限，请授予网络访问权限",Toast.LENGTH_LONG).show();
        }

    }

    //按返回键时隐藏侧边滑动栏
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //显示toolbar顶部菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_top_menu1, menu);
        return true;
    }
    //监听顶部菜单栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.tooBar_top_menu_item1) {
            Toast.makeText(mContext,"点击了顶部菜单栏第一项",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //侧滑栏底部登陆注册按钮监听
    public void btnLoginAndSign(View v){
        switch (v.getId()){
            case R.id.navigation_left_button1:{
                Toast.makeText(mContext,"点击了登陆",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.navigation_left_button2:{
                Toast.makeText(mContext,"点击了注册",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    //监听底部导航栏的点击事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_dingwei:
                    MainActivityPermissionsDispatcher.checkInternetWithPermissionCheck(MainActivity.this);
                    MainActivityPermissionsDispatcher.getLocationProviderWithPermissionCheck(MainActivity.this);
                    Toast.makeText(mContext,"定位中...",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.bottom_tianjia:
                    return true;
                case R.id.bottom_sousuo:
                    return true;
                case R.id.bottom_renwu:
                    return true;
            }
            return false;
        }
    };

    //权限管理
    //展示为什么需要权限
    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void showLocateRationalDialog(final PermissionRequest request){
        new AlertDialog.Builder(mContext)
                .setMessage("在地图上显示您的位置需要定位权限，是否授予定位权限？")
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false).show();
    }
    //获取到连接网络的权限时设置internetStatus的值为true
    @NeedsPermission(Manifest.permission.INTERNET)
    public void checkInternet(){
        internetStatus = true;
    }
    //拒绝授予定位权限时调用
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void localPermissionDenied(){
        Toast.makeText(mContext,"您拒绝授予定位权限，软件无法正常使用",Toast.LENGTH_SHORT).show();
    }
    //拒绝授予定位权限且点击了不再询问时调用
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void localPermissionNeverAsk(){
        Toast.makeText(mContext,"软件无法正常使用，请到设置里同意此软件获取定位权限",Toast.LENGTH_SHORT).show();
    }
    //重写onRequestPermissionResult方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(MainActivity.this,requestCode,grantResults);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMapView.dispose();
    }
}
