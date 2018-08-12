package fun.wxy.www.fragment2.navigation;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fun.wxy.www.fragment2.R;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {

    private List<NavigationItem> itemList;

    //单例
//    private static MyRecycleViewAdapter INSTANCE = new MyRecycleViewAdapter();
//    private MyRecycleViewAdapter(){}
//    public static MyRecycleViewAdapter getInstance(){
//        return INSTANCE;
//    }


    public void initItemList(){
        itemList = new ArrayList<>();
        NavigationItem item1 = new NavigationItem("详细信息",R.drawable.icon1);
        itemList.add(item1);
        NavigationItem item2 = new NavigationItem("详细信息",R.drawable.icon1);
        itemList.add(item2);
        NavigationItem item3 = new NavigationItem("详细信息",R.drawable.icon1);
        itemList.add(item3);
        NavigationItem item4 = new NavigationItem("详细信息",R.drawable.icon1);
        itemList.add(item4);
        NavigationItem item5 = new NavigationItem("日志信息",R.drawable.icon1);
        itemList.add(item5);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.navigation_left_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        NavigationItem navigationItem = itemList.get(position);
        holder.imageView.setImageResource(navigationItem.getImageView());
        holder.textView.setText(navigationItem.getTextView());

        //设置点击事件
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                NavigationItem recycleItem = itemList.get(position);
                Toast.makeText(v.getContext(),"click"+recycleItem.getTextView(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.navigation_left_item_txt);
            imageView = itemView.findViewById(R.id.navigation_left_item_img);
        }
    }
}
