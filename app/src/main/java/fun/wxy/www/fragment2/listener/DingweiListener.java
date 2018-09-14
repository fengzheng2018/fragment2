package fun.wxy.www.fragment2.listener;

import android.view.View;

import fun.wxy.www.fragment2.map.ShowMap;

public class DingweiListener implements View.OnClickListener {
    private ShowMap showMap;

    public DingweiListener(ShowMap showMap) {
        this.showMap = showMap;
    }

    @Override
    public void onClick(View v) {
        showMap.drawMap();
    }
}
