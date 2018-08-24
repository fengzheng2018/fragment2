package fun.wxy.www.fragment2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

import fun.wxy.www.fragment2.map.ShowMap;
import fun.wxy.www.fragment2.navigation.BottomNavigationViewHelper;
import fun.wxy.www.fragment2.navigation.MyRecycleViewAdapter;
import fun.wxy.www.fragment2.navigation.NavigationItemSpace;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private DrawerLayout mDrawerLayout;
    private Context mContext;
    private MapView mMapView;
    private ShowMap showMap;

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

        showMap = new ShowMap(MainActivity.this,mContext,mMapView);
        showMap.initMap();
        showMap.drawMap();

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
                    showMap.drawMap();
                    Toast.makeText(mContext,"定位中...",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.bottom_tianjia:
                    Intent intent = new Intent(MainActivity.this,ShowRoute_Main.class);
                    startActivity(intent);
                    return true;
                case R.id.bottom_sousuo:
                    return true;
                case R.id.bottom_renwu:
                    return true;
            }
            return false;
        }
    };


    //重写onRequestPermissionResult方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //获得权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        showMap.drawMap();
    }

    //拒绝权限且点击了不再提示
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(MainActivity.this,perms)){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("定位权限提示");
            builder.setMessage("为了软件的正常使用，请同意此软件使用定位权限");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(mContext,"软件将无法正常使用，要正常使用软件，请在设置中同意此软件获取定位权限",Toast.LENGTH_LONG).show();
                }
            });
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+getPackageName()));
                    startActivityForResult(intent,103);
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        showMap.drawMap();
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
