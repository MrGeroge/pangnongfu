package com.pangnongfu.server.web.vo;

/**
 * Created by shuiyu on 2015/9/26.
 */
public class StatusCategoryVO {
    private long category_id;
    private String name;

    public StatusCategoryVO(){}
    public StatusCategoryVO(long category_id,String name){
        this.category_id = category_id;
        this.name=name;
    }
    public long getCategory_id() {
        return category_id;
    }

    public StatusCategoryVO setCategory_id(long category_id) {
        this.category_id = category_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StatusCategoryVO setName(String name) {
        this.name = name;
        return this;
    }

}
