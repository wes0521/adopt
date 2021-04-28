package com.wes.adopt.utils;

public class PageModel {
    //当前页码
    private int pageIndex;
    //页面的大小
    private int pageSize=10;
    //总页数
    private int totalSize;
    //总记录数
    private int recordCount;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        if(recordCount%pageSize == 0){
            totalSize = recordCount/pageSize;
        }
        else{
            totalSize = recordCount/pageSize + 1;
        }
        return totalSize;
    }

    /*public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }*/

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getFirstLimitParam(){

        return (this.getPageIndex()-1)*this.pageSize;

    }
}
