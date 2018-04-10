package com.cnepay.android.swiper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.MessageContentBean;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.SingleTypeAdapter;

/**
 * created by millerJK on time : 2017/5/7
 * description : 结算查询ITEM
 */

public class MineMessageAdapter extends SingleTypeAdapter<MessageContentBean, MineMessageAdapter.MessageViewHolder> {
    private static final String TAG = "MineMessageAdapter";

    private Context mContext;

    public MineMessageAdapter(Context mContext, int resId) {
        super(resId);
        this.mContext = mContext;
    }

    @Override
    public MessageViewHolder createViewHolder(View itemView) {
        return new MessageViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(MessageViewHolder holder, MessageContentBean item, int position) {
        holder.mTimeTv.setText(Utils.getMessageCreateTime(item.getCreateTime()));
        holder.mMessageTv.setText(item.getContent());
        holder.mTitleTv.setText(item.getTitle());
        if(item.getIsRead()==0){
            holder.mHadReadIv.setVisibility(View.INVISIBLE);
        }else{
            holder.mHadReadIv.setVisibility(View.VISIBLE);
        }
    }

    public class MessageViewHolder extends CommonViewHolder {
        public LinearLayout mItemLL;
        public TextView mTimeTv, mMessageTv, mTitleTv, mReadTv;
        public ImageView mArrowIv;
        public ImageView mHadReadIv;
        public boolean flag = true;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mTimeTv = (TextView) itemView.findViewById(R.id.message_item_tv_time);
            mReadTv = (TextView) itemView.findViewById(R.id.message_item_tv_read);
            mMessageTv = (TextView) itemView.findViewById(R.id.message_item_tv_content);
            mTitleTv = (TextView) itemView.findViewById(R.id.message_item_tv_title);
            mItemLL = (LinearLayout) itemView.findViewById(R.id.message_item_ll);
            mArrowIv = (ImageView) itemView.findViewById(R.id.message_item_iv_arrow);
            mHadReadIv=(ImageView) itemView.findViewById(R.id.message_item_iv_hadread);
        }

    }
}
