package fun.wxy.www.fragment2.navigation;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class NavigationItemSpace extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent,RecyclerView.State state){
        outRect.bottom = 35;
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = 20;
        }
    }
}
