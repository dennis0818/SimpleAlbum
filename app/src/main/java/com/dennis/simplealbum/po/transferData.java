package com.dennis.simplealbum.po;

import java.util.List;

public class transferData {

    public int count;

    public List<entryVo> dataList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<entryVo> getDataList() {
        return dataList;
    }

    public void setDataList(List<entryVo> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "transferData : {" +
                "count = " + count +
                ", dataList=" + dataList +
                '}';
    }
}
