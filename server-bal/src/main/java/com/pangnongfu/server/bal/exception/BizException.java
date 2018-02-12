package com.pangnongfu.server.bal.exception;

/**
 * Author:zhangyu
 * create on 15/9/20.
 */
public class BizException extends Exception{

    /**用户名重复*/
    public final static Integer ERROR_CODE_USERNAME_EXISTED=new Integer(2000);
    /**用户不存在*/
    public final static Integer ERROR_CODE_USER_NOT_FOUND=new Integer(2001);
    /**用户密码错误*/
    public final static Integer ERROR_CODE_PASSWORD_ERROR=new Integer(2002);
    /**状态不存在*/
    public final static Integer ERROR_CODE_STATUS_NOT_FOUND = new Integer(2003);
    /**状态发布失败*/
    public final static Integer ERROR_CODE_STATUS_ADD_ERROR = new Integer(2004);
    /**空指针异常*/
    public final static Integer ERROR_CODE_NULL_POINTER = new Integer(2005);
    /**数据库实例未找到*/
    public final static Integer ERROR_CODE_INSTANCE_NOT_FOUND = new Integer(2006);
    /**关注自身*/
    public final static Integer ERROR_CODE_FOLLOW_SELF = new Integer(2007);


    private Integer errorCode;

    public BizException(Integer errorCode,String msg){
        super(msg);
        this.errorCode=errorCode;
    }

    public BizException(Integer errorCode,String msg,Throwable e){
        super(msg,e);
        this.errorCode=errorCode;
    }

    public Integer getErrorCode(){
        return this.errorCode;
    }
}
