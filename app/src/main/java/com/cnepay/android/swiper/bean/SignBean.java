package com.cnepay.android.swiper.bean;

/**
 * created by millerJK on time : 2017/5/22
 * description : 签到
 */

public class SignBean extends BaseBean {

    public int traceNo;
    public String model;
    public boolean needUpdateIC;
    public DeviceEntity device;

    public static class DeviceEntity {
        public String macAddress;
        public String keyValue;
        public String checkValue;
        public String bluetoothName;
        public String keyType;
        public boolean isBluetooth;
        public String ksnNo;
    }
}
