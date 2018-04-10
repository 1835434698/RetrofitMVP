package com.cnepay.android.swiper.bean;


/**
 * Created by wjl on 2017/5/12.
 */

public class BaseBean {
    private String respTime;
    private boolean isSuccess;
    private String respCode;
    private String respMsg;
    private long expireTime;

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }


    public String getRespCode() {
        return respCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }


    @Override
    public String toString() {
        return "RegisterBean{" +
                "respTime='" + respTime + '\'' +
                ", isSuccess='" + isSuccess + '\'' +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                '}';
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getObjectName(){
        return getClass().getSimpleName();
    }

    /**
     * 子类实现，标注本Bean是否依赖于LoginBean
     * 如果返回true则本对象只能以参数形式存在于LoginBean中。并且不能交由Dictionary维护
     * @return 本Bean是否依赖于LoginBean
     */
    public boolean dependsOnLoginBean(){return true;}
}

