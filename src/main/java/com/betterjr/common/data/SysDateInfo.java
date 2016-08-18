package com.betterjr.common.data;

 
import com.betterjr.common.utils.BetterDateUtils;

/**
 * 系统日期参数表，主要包括自然日，上一工作日，工作日，下一工作日
 * 
 * @author zhoucy
 *
 */
public class SysDateInfo implements java.io.Serializable {
    private static final long serialVersionUID = 6533500504081647703L;
    
    // 自然日期，如果没有使用系统当日期
    private String naturalDate;
    // 上一交易日
    private String lastDate;
    // 当前交易日
    private String currentDate;
    // 下一交易日
    private String nextDate;

    public SysDateInfo() {

    }

    public String findTradeDate(String anTradeTime) { 
        String myTime = naturalDate.concat(" ").concat(BetterDateUtils.getNumTime());
        String otherTime = currentDate.concat(" ").concat(anTradeTime);
        int result = myTime.compareTo(otherTime);
        if (result <= 0) {
            return this.currentDate;
        }
        else {
            return this.nextDate;
        }
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String anNextDate) {
        nextDate = anNextDate;
    }

    public SysDateInfo(String anA1, String anA2, String anA3, String anA4) {
        this.naturalDate = anA1;
        this.lastDate = anA2;
        this.currentDate = anA3;
        this.nextDate = anA4;
    }

    public String getNaturalDate() {
        return naturalDate;
    }

    public void switchNext(String anNextDate) {
        this.naturalDate = BetterDateUtils.getNumDate();
        this.lastDate = this.currentDate;
        this.currentDate = nextDate;
        this.nextDate = anNextDate;
    }

    public String getTradeDate(boolean anNext) {
        if (anNext) {
            return this.nextDate;
        }
        else {
            return this.currentDate;
        }
    }

    public void setNaturalDate(String anNaturalDate) {
        naturalDate = anNaturalDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String anLastDate) {
        lastDate = anLastDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String anCurrentDate) {
        currentDate = anCurrentDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(" naturalDate=").append(naturalDate);
        sb.append(", lastDate=").append(lastDate);
        sb.append(", currentDate=").append(currentDate);
        sb.append(", nextDate=").append(nextDate);
        sb.append("]");
        return sb.toString();
    }

}
