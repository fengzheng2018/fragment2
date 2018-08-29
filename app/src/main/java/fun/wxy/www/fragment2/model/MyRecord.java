package fun.wxy.www.fragment2.model;

import org.litepal.crud.LitePalSupport;

public class MyRecord extends LitePalSupport {
    private long recordTime;
    private long recordCode;
    private int isOver;

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public long getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(long recordCode) {
        this.recordCode = recordCode;
    }

    public int getIsOver() {
        return isOver;
    }

    public void setIsOver(int isOver) {
        this.isOver = isOver;
    }
}
