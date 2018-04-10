package com.cnepay.android.swiper.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * created by millerJK on time : 2017/5/19
 * description : 结算查询
 */

public class SettleListBean extends BaseBean{

    public int amount;
    public int count;
    public List<SettleListEntity> settleList;

    public static class SettleListEntity implements Parcelable {



        public int transAmount;
        public String accountNum;
        public int settleId;
        public int settleStatus;
        public String uniqueRecord;
        public int settleMoney;
        public String settleDate;
        public String settleType;
        public String merchantName;
        public String merchantNo;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.transAmount);
            dest.writeString(this.accountNum);
            dest.writeInt(this.settleId);
            dest.writeInt(this.settleStatus);
            dest.writeString(this.uniqueRecord);
            dest.writeInt(this.settleMoney);
            dest.writeString(this.settleDate);
            dest.writeString(this.settleType);
            dest.writeString(this.merchantName);
            dest.writeString(this.merchantNo);
        }

        public SettleListEntity() {
        }

        protected SettleListEntity(Parcel in) {
            this.transAmount = in.readInt();
            this.accountNum = in.readString();
            this.settleId = in.readInt();
            this.settleStatus = in.readInt();
            this.uniqueRecord = in.readString();
            this.settleMoney = in.readInt();
            this.settleDate = in.readString();
            this.settleType = in.readString();
            this.merchantName = in.readString();
            this.merchantNo = in.readString();
        }

        public static final Parcelable.Creator<SettleListEntity> CREATOR = new Parcelable.Creator<SettleListEntity>() {
            public SettleListEntity createFromParcel(Parcel source) {
                return new SettleListEntity(source);
            }

            public SettleListEntity[] newArray(int size) {
                return new SettleListEntity[size];
            }
        };
    }
}
