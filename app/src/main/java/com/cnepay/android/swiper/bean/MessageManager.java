package com.cnepay.android.swiper.bean;

import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;

/**
 * Created by wjl on 2017/5/18.
 */

public final class MessageManager {
    private static MessageManager messageManager;


    private MessageManager() {
    }

    public static MessageManager getInstance() {
        if (null == messageManager) {
            messageManager = new MessageManager();
        }
        return messageManager;
    }


    /**
     * 点击一条未读消息， 把他修改成已读
     * 我的消息的信息 替换
     *@param index  点击了那个未读消息
     * @param
     * @return
     */
    public static void replaceMineMessageBean(int index) {
        MineMessageBean message = Dictionary.getLoginBean().getMessageBean();
        if(message!=null){
            message.getBody().get(index).setIsRead(1);
            message.getHead().setUnReadCount(message.getHead().getUnReadCount()-1);
            message.getHead().setReadCount(message.getHead().getReadCount()+1);
            if(message.getHead().getUnReadCount()==0){
                message.getHead().setHasUnRead(false);
            }
             Logger.i("wjl",message.toString());
        }

    }
    /**
     *
     * 我的消息的信息 替换--重新请求的网络数据
     * @param bean  未读消息
     * @return
     */
    public static void replaceMineMessageBean(MineMessageBean bean) {
        MineMessageBean message = Dictionary.getLoginBean().getMessageBean();
        if (bean != null) {
            if(message==null){
                Dictionary.getLoginBean().setMessageBean(bean);
                message= Dictionary.getLoginBean().getMessageBean();
            }
            message.setBody(bean.getBody());
            message.setHead(bean.getHead());
            message.setRespCode(bean.getRespCode());
            message.setRespMsg(bean.getRespMsg());
            message.setRespTime(bean.getRespTime());
            message.setSuccess(bean.isSuccess());
        }
        Logger.i("wjl",message.toString());
    }
}
