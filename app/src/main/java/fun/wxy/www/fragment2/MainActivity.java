package fun.wxy.www.fragment2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import fun.wxy.www.fragment2.navigation.BottomNavigationViewHelper;
import fun.wxy.www.fragment2.navigation.MyRecycleViewAdapter;
import fun.wxy.www.fragment2.navigation.NavigationItemSpace;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context mContext;

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
}
