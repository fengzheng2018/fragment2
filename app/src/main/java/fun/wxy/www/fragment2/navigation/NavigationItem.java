package fun.wxy.www.fragment2.navigation;

public class NavigationItem {

    private int imageView;
    private String textView;

    public NavigationItem(String textView,int imageView) {
        this.imageView = imageView;
        this.textView = textView;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getTextView() {
        return textView;
    }

    public void setTextView(String textView) {
        this.textView = textView;
    }
}
