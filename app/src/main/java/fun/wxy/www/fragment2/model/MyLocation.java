package fun.wxy.www.fragment2.model;

import org.litepal.crud.LitePalSupport;

public class MyLocation extends LitePalSupport {
    private long time;
    private double longitude;
    private double latitude;
    private long recordCode;
    private int isLoad;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(long recordCode) {
        this.recordCode = recordCode;
    }

    public int getIsLoad() {
        return isLoad;
    }

    public void setIsLoad(int isLoad) {
        this.isLoad = isLoad;
    }
}
