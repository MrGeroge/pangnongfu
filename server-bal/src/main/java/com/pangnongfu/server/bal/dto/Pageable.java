package com.pangnongfu.server.bal.dto;

import java.util.List;

/**
 * Author:zhangyu
 * create on 15/9/21.
 */
public class Pageable<T> {
    private boolean hasNextPage;
    private boolean hasPrePage;
    private int currentPage;
    private List<T> content;

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean getHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
