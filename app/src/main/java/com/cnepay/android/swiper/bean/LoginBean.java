package com.cnepay.android.swiper.bean;

import android.text.TextUtils;
import android.util.SparseArray;

import com.cnepay.android.swiper.utils.Logger;

/**
 * Created by Administrator on 2017/4/25.
 */

public class LoginBean extends BaseBean {
    private final String sessionID;

    private String loginName;//手机号码
    private MerchantQualify merchantQualify;
    private String identityNum;//身份证号
    private String name;
    private DeviceBean deviceBean;
    private MineMessageBean messageBean;

    public MineMessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MineMessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public LoginBean(String sessionID) {
        if (TextUtils.isEmpty(sessionID)) {
            throw new RuntimeException("bean id created for this session is null");
        }
        this.sessionID = sessionID;
    }

    public void setIdentityNumber(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getIdentityNumber() {
        return identityNum;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        if (!TextUtils.isEmpty(loginName))
            this.loginName = loginName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public MerchantQualify getMerchantQualify() {
        return merchantQualify;
    }

    public void setMerchantQualify(MerchantQualify merchantQualify) {
        if (merchantQualify != null)
            this.merchantQualify = merchantQualify;
    }

    /**
     * 用户是否绑定了设备
     *
     * @return true 绑定了设备,可以进行资质认证;false没有绑定
     */
    public boolean isDeviceBound() {
        return 1 == getMerchantQualify().getTerminalAuth();
    }

    /**
     * 获取审核状态
     *
     * @return 四项审核中哪一条待编辑，或 全部通过的标示， 或 审核中的标示。
     */
    public String getAuthResult() {
        // TODO: 2017/5/22 看是否需要判断 isDeviceBound
        String result = null;
        boolean isAllSuccess = true;
        int[] authStatusArray = getMerchantQualify().getAuthStatusArray();
        for (int i = MerchantQualify.AUTH_TYPE_REAL_NAME; i <= MerchantQualify.AUTH_TYPE_SIGNATURE; i++) {
            int status = authStatusArray[i];
            if (status == MerchantQualify.AUTH_STATUS_NEVER || status == MerchantQualify.AUTH_STATUS_FAIL) {
                result = getMerchantQualify().strAuthResult(i);
                break;
            }
            isAllSuccess = isAllSuccess & (status == MerchantQualify.AUTH_STATUS_PASS);
        }
        if (TextUtils.isEmpty(result)) {//没有一个审核条目处于可编辑状态
            if (isAllSuccess)
                result = getMerchantQualify().strAuthResult(MerchantQualify.AUTH_TYPE_ALL_PASS);
            else
                result = getMerchantQualify().strAuthResult(MerchantQualify.AUTH_TYPE_UNDERWAY);
        }
        return result;
    }

    /**
     * 设备密钥等信息是否获取完成（是否成功签到）
     *
     * @return true 信息完整，otherwise false
     */
    public boolean isDeviceKeysPrepared() {
        // TODO: 2017/5/22 看是否需要判断 getAuthResult
        return 1 == getMerchantQualify().getTerminalAuth() * getMerchantQualify().getRealNameAuth() * getMerchantQualify().getMerchantAuth() * getMerchantQualify().getAccountAuth() * getMerchantQualify().getSignatureAuth();
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "sessionID='" + sessionID + '\'' +
                ", loginName='" + loginName + '\'' +
                ", merchantQualify=" + merchantQualify +
                ", identityNum='" + identityNum + '\'' +
                ", deviceBean=" + deviceBean +
                '}';
    }

    @Override
    public boolean dependsOnLoginBean() {
        return false;
    }

    public void refresh(LoginBean loginBean) {
        Logger.e(getObjectName(), "refresh newBean\n" + loginBean.toString());
        if (loginBean != null) {
            setLoginName(loginBean.getLoginName());
            setMerchantQualify(loginBean.getMerchantQualify());
            setSuccess(loginBean.isSuccess());
            setRespCode(loginBean.getRespCode());
            setRespMsg(loginBean.getRespMsg());
            setRespTime(loginBean.getRespTime());
        }
        Logger.e(getObjectName(), "refresh mergedBean\n" + toString());
    }

    public class MerchantQualify {
        public static final int AUTH_TYPE_REAL_NAME = 0;
        public static final int AUTH_TYPE_MERCHANT = 1;
        public static final int AUTH_TYPE_ACCOUNT = 2;
        public static final int AUTH_TYPE_SIGNATURE = 3;
        public static final int AUTH_TYPE_UNDERWAY = 4;//表示所有审核中某一项正在进行中
        public static final int AUTH_TYPE_ALL_PASS = 5;//表示所有审核都已通过
        public static final int AUTH_STATUS_NEVER = 0;//未认证
        public static final int AUTH_STATUS_PASS = 1;//认证成功
        public static final int AUTH_STATUS_FAIL = 2;//认证失败
        public static final int AUTH_STATUS_UNDERWAY = 3;//审核中
        private int[] statuses = new int[4];
        private final SparseArray<String> authResults = new SparseArray<>();
        {
            authResults.put(AUTH_TYPE_REAL_NAME,"AUTH_RESULT_REAL_NAME");
            authResults.put(AUTH_TYPE_MERCHANT,"AUTH_RESULT_MERCHANT");
            authResults.put(AUTH_TYPE_ACCOUNT,"AUTH_RESULT_ACCOUNT");
            authResults.put(AUTH_TYPE_SIGNATURE,"AUTH_RESULT_SIGNATURE");
            authResults.put(AUTH_TYPE_UNDERWAY,"AUTH_RESULT_UNDERWAY");
            authResults.put(AUTH_TYPE_ALL_PASS,"AUTH_RESULT_ALL_PASS");
        }

        public String strAuthResult(int authType) {
            String result = authResults.get(authType);
            Logger.e(getClass().getSimpleName(), "strAuthResult:"+result);
            return result;
        }

        private int terminalAuth;//设备绑定状态 0未绑定 ,1绑定激活成功
        private int realNameAuth;//实名认证
        private int merchantAuth;//商户认证
        private int accountAuth;//账户认证
        private int signatureAuth;//签名认证

        public int[] getAuthStatusArray() {
            Logger.e(getClass().getSimpleName(), "statuses==null:"+(statuses==null));
            statuses[0] = realNameAuth;
            statuses[1] = merchantAuth;
            statuses[2] = accountAuth;
            statuses[3] = signatureAuth;
            return statuses;
        }

        public int getTerminalAuth() {
            return terminalAuth;
        }

        public void setTerminalAuth(int terminalAuth) {
            this.terminalAuth = terminalAuth;
        }

        public int getRealNameAuth() {
            return realNameAuth;
        }

        public void setRealNameAuth(int realNameAuth) {
            this.realNameAuth = realNameAuth;
        }

        public int getMerchantAuth() {
            return merchantAuth;
        }

        public void setMerchantAuth(int merchantAuth) {
            this.merchantAuth = merchantAuth;
        }

        public int getAccountAuth() {
            return accountAuth;
        }

        public void setAccountAuth(int accountAuth) {
            this.accountAuth = accountAuth;
        }

        public int getSignatureAuth() {
            return signatureAuth;
        }

        public void setSignatureAuth(int signatureAuth) {
            this.signatureAuth = signatureAuth;
        }

        @Override
        public String toString() {
            return "MerchantQualify{" +
                    "terminalAuth=" + terminalAuth +
                    ", realNameAuth=" + realNameAuth +
                    ", merchantAuth=" + merchantAuth +
                    ", accountAuth=" + accountAuth +
                    ", signatureAuth=" + signatureAuth +
                    '}';
        }
    }

}
