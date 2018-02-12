package com.pangnongfu.server.web.vo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shuiyu on 2015/9/26.
 */
public class SocialResult<T> {
    public static final String SUCCESS_STATUS="success";
    public static final String FAILED_STATUS="failed";
    private boolean hasNextPage;
    private boolean hasPrePage;
    private int currentPage;
    private int count;
    private List<T> content=new LinkedList<>();


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCount(int count){this.count = count;}

    public void setHasNextPage(boolean hasNextPage){
        this.hasNextPage=hasNextPage;
    }
    public void setHasPrePage(boolean hasPrePage){
        this.hasNextPage=hasPrePage;
    }
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
