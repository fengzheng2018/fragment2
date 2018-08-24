## Fragement  
### 布局文件  
* activity_main.xml  MainActivity加载的布局文件
* content_main.xml  主界面布局文件  
* navigation_left_foot.xml  左侧滑动栏底部登陆这侧按钮  
* navigation_left_head.xml  左侧滑动栏顶部图标和人员信息  
* navigation_left_item.xml  左侧滑动栏中间选项菜单  
<br/>

### 菜单文件  
* navigation_bottom_item1.xml  主界面底部导航栏菜单  
* toolbar_top_menu1.xml  主界面顶部菜单  
<br/>

### 左侧滑动栏  
* id为navigation_left_container  
* 滑动栏顶部人员信息，id为navigation_left_top_head，引入布局文件navigation_left_head
* 滑动栏中间菜单项，id为navigation_left_recyclerView，使用RecycleView动态生成
* 滑动栏底部登陆注册，id为navigation_left_bottom_button，引入布局文件navigation_left_foot  
<strong>顶部人员信息：</strong><br/>
  1.图片：id为navigation_left_image1  
  2.人员信息1：id为navigation_left_text1  
  3.人员信息2：id为navigation_left_text2  
<strong>中间选项菜单：</strong><br/>
  1.详细信息：  
  2.详细信息：  
  3.日志信息  
<strong>底部登陆注册按钮：</strong>    
  1.登陆：id为navigation_left_button1  
  2.注册：id为navigation_left_button2
<br/>

### 主界面
引入布局文件content_main.xml  
* 顶部toolBar：id为toolBar_top_container
* 中间内容显示区域：
* 底部导航栏：id为BottomNavigation_bottom_container，引入菜单文件navigation_bottom_item1.xml  
  1.定位：id为bottom_dingwei  
  2.添加事件，id为bottom_tianjia  
  3.搜索，id为bottom_sousuo  
  4.任务查看，id为bottom_renwu<br/>

### 图标文件
* 定位按钮：dingwei.xml  
* 添加按钮：tianjia.xml  
* 搜索按钮：sousuo.xml  
* 任务查看：renwu.xml