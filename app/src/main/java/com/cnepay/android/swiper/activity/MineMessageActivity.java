package com.cnepay.android.swiper.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.adapter.MineMessageAdapter;
import com.cnepay.android.swiper.bean.MessageContentBean;
import com.cnepay.android.swiper.bean.MineMessageBean;
import com.cnepay.android.swiper.bean.ModifyMessageBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.presenter.MineMessagePresenter;
import com.cnepay.android.swiper.presenter.ModifyMessagePresenter;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.bean.MessageManager;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.MineMessageModifyView;
import com.cnepay.android.swiper.view.MineMessageQueryView;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.refresh.NestedRefreshLayout;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wjl on 2017/5/3.
 * 我的消息页面
 */

public class MineMessageActivity extends MvpAppCompatActivity implements View.OnClickListener, MineMessageQueryView, MineMessageModifyView {
    private static final String TAG = "MineMessageActivity";

    private static final int TYPE_ALL = 0x001;//全部列表
    private static final int TYPE_NOTICE = 0x002;//通知列表
    private static final int TYPE_BULLETIN = 0x003;//公告列表

    private SwipeMenuRecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private NestedRefreshLayout mRefreshLayout;
    private MineMessageAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private LinearLayout mMessageNetError;

    private MineMessagePresenter messagePresenter;
    private ModifyMessagePresenter modifyMessagePresenter;
    private int firstItemPosition;//当前屏幕recycleview第一个item位置。
    private MineMessageAdapter.MessageViewHolder viewHolder;//recycleView的viewHolder
    private int position;
    private MessageManager messageManager;

    private int type;//点击条目的时候  区分点击了是全部的列表，还是通知列表，还是公告列表
    private List data = new ArrayList<MessageContentBean>();//消息数据
    private List noticeData = new ArrayList<MessageContentBean>();//公告消息数据
    private List bulletinData = new ArrayList<MessageContentBean>();//业务通知消息数据

    private View noDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }


    private void initView() {
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_mine_message);
        uc.setTitle(getResources().getString(R.string.mine_message_title));
        uc.getRightImage().setImageResource(R.drawable.mine_img_menu);
        uc.getRightImage().setOnClickListener(v -> popUpMyOverflow(v));
        mRefreshLayout = (NestedRefreshLayout) findViewById(R.id.refresh_lay);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.mine_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mMessageNetError = (LinearLayout) findViewById(R.id.message_net_error);
        noDataView = LayoutInflater.from(this).inflate(R.layout.item_mine_message_nodata, null);
        TextView nodataView = (TextView) noDataView.findViewById(R.id.mine_tv_message_no_data);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nodataView.getLayoutParams();
        params.width = Utils.screenWidth(this);
        nodataView.setLayoutParams(params);
        mMessageNetError.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);

        messageManager = MessageManager.getInstance();
        messagePresenter = new MineMessagePresenter();
        messagePresenter.attachView(MineMessageActivity.this);
        modifyMessagePresenter = new ModifyMessagePresenter();
        modifyMessagePresenter.attachView(MineMessageActivity.this);
        mAdapter = new MineMessageAdapter(this, R.layout.item_mine_message);
        mAdapter.addNoDataView(noDataView);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messagePresenter.detachView(false);
        modifyMessagePresenter.detachView(false);
    }

    private void initData() {
        if (Dictionary.getLoginBean() != null) {
            if (Dictionary.getLoginBean().getMessageBean() != null) {
                MineMessageBean bean = Dictionary.getLoginBean().getMessageBean();
                type = TYPE_ALL;
                data = bean.getBody();
                sortData(data);
                mAdapter.resetDatas(data);
            } else {
                //消息为空 重新请求
                messagePresenter.queryMineMessage(true);
            }

        }

    }

    /**
     * 对数据进行分类  通知  和公告
     */
    private void sortData(List data) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                MessageContentBean content = (MessageContentBean) data.get(i);
                if (content.getNewsType() == 0) {
                    //公告
                    bulletinData.add(content);
                } else if (content.getNewsType() == 1) {
                    //通知
                    noticeData.add(content);
                }
            }
            Logger.i("wjl", "公告有几条" + bulletinData.size());
            Logger.i("wjl", "通知有几条" + noticeData.size());
        }

    }

    private void initListener() {
        //下拉刷新监听  //*****
        mRefreshLayout.setOnRefreshListener(refreshListener);

        mAdapter.setItemClickListener(itemListener);
        mMessageNetError.setOnClickListener(this);
    }

    CommonViewHolder.OnItemClickListener itemListener = new CommonViewHolder.OnItemClickListener() {
        @Override
        public void OnItemClick(View v, int adapterPosition) {
            position = adapterPosition;
            firstItemPosition = layoutManager.findFirstVisibleItemPosition();
            MessageContentBean bean = null;
            if (type == TYPE_ALL) {
                bean = (MessageContentBean) data.get(adapterPosition);
            } else if (type == TYPE_BULLETIN) {
                bean = (MessageContentBean) bulletinData.get(adapterPosition);
            } else if (type == TYPE_NOTICE) {
                bean = (MessageContentBean) noticeData.get(adapterPosition);
            }

            Logger.i("wjl", "我点了那条消息？position:" + adapterPosition + "  ： id" + bean.getNewsId());
            if (adapterPosition - firstItemPosition >= 0) {
                //得到要更新的item的view
                View view = mRecyclerView.getChildAt(adapterPosition - firstItemPosition);
                if (null != mRecyclerView.getChildViewHolder(view)) {
                    viewHolder = (MineMessageAdapter.MessageViewHolder) mRecyclerView.getChildViewHolder(view);
                }
                if (bean.getIsRead() == 0) {
                    //未读
                    modifyMessagePresenter.modifyMessage(bean.getNewsId(), false);
                } else {
                    //已读
                    changeItemStatus(bean);
                }

            }
        }
    };

    NestedRefreshLayout.OnRefreshListener refreshListener = new NestedRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //todo  add netting here
            messagePresenter.queryMineMessage(true);
        }
    };

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow(View v) {

        //初始化PopupWindow的布局
        View popView = getLayoutInflater().inflate(R.layout.layout_pop_mine_message, null);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        int mWidth = metrics.widthPixels / 3;
        //popView即popupWindow的布局，ture设置focusAble.
        mPopupWindow = new PopupWindow(popView,
                mWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        //点击外部关闭。
        mPopupWindow.setOutsideTouchable(true);
        //设置一个动画。
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        //设置Gravity，让它显示在右上角。
        mPopupWindow.showAsDropDown(v, -(mWidth / 2 + 10), Utils.dip2px(this, 5));
        //设置item的点击监听
        popView.findViewById(R.id.pop_message_item_all).setOnClickListener(MineMessageActivity.this);
        popView.findViewById(R.id.pop_message_item_notify).setOnClickListener(MineMessageActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_message_item_all:
                //pop点击条目  公告
                type = TYPE_BULLETIN;
                mAdapter.resetDatas(bulletinData);
                Logger.i("wjl", "公告有几条" + bulletinData.size());
                mPopupWindow.dismiss();
                break;
            case R.id.pop_message_item_notify:
                //pop点击条目  业务通知
                type = TYPE_NOTICE;
                mAdapter.resetDatas(noticeData);
                Logger.i("wjl", "通知有几条" + noticeData.size());
                mPopupWindow.dismiss();
                break;
            case R.id.message_net_error:
                messagePresenter.queryMineMessage(true);
                break;
        }
    }


    @Override
    public void queryMineMessage(Object connect) {
        mMessageNetError.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        if (connect instanceof MineMessageBean) {
            MineMessageBean bean = (MineMessageBean) connect;
            if (bean != null) {
                if(Dictionary.getLoginBean()!=null){
                    messageManager.replaceMineMessageBean(bean);
                }

                Logger.i("wjl", bean.toString());
                data.clear();
                noticeData.clear();
                bulletinData.clear();
                data = bean.getBody();
                sortData(data);
                mAdapter.resetDatas(data);
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void modifyMessage(Object connect) {
        //修改某条消息为已读
        if (connect instanceof ModifyMessageBean) {
            ModifyMessageBean bean = (ModifyMessageBean) connect;
            MessageContentBean contentBean = null;
            switch (type) {
                case TYPE_ALL:
                    contentBean = (MessageContentBean) data.get(position);
                    ((MessageContentBean) data.get(position)).setIsRead(1);
                    break;
                case TYPE_BULLETIN:
                    contentBean = (MessageContentBean) bulletinData.get(position);
                    ((MessageContentBean) bulletinData.get(position)).setIsRead(1);
                    break;
                case TYPE_NOTICE:
                    contentBean = (MessageContentBean) noticeData.get(position);
                    ((MessageContentBean) noticeData.get(position)).setIsRead(1);
                    break;

            }
            int index = data.indexOf(contentBean);
            Logger.i("wjl", "那一条改变为已读--id:  " + bean.getMessageId());
            Logger.i("wjl", "那一条改变为已读--针对all： index--：  " + index);
            if(Dictionary.getLoginBean()!=null){
                messageManager.replaceMineMessageBean(index);
            }
            changeItemStatus(contentBean);
        }
    }

    /**
     * 修改条目的状态
     */
    private void changeItemStatus(MessageContentBean bean) {
        if (bean.getIsRead() == 0) {
            //未读
            viewHolder.mHadReadIv.setVisibility(View.INVISIBLE);
        } else {
            //已读
            viewHolder.mHadReadIv.setVisibility(View.VISIBLE);
        }
        if (viewHolder.flag) {
            if (viewHolder.mMessageTv.getLineCount() >= 2) {
                viewHolder.flag = false;
                viewHolder.mMessageTv.setSingleLine(viewHolder.flag);
                viewHolder.mMessageTv.setEllipsize(null); // 展开
                viewHolder.mReadTv.setText(R.string.pack_up_message);
                viewHolder.mArrowIv.setImageDrawable(getResources().getDrawable(R.drawable.mine_message_img_arrow));
            }

        } else {
            viewHolder.flag = true;
            viewHolder.mMessageTv.setMaxLines(2);
            viewHolder.mMessageTv.setEllipsize(TextUtils.TruncateAt.END); // 收缩
            viewHolder.mReadTv.setText(R.string.read_message);
            viewHolder.mArrowIv.setImageDrawable(getResources().getDrawable(R.drawable.all_arrows));
        }

    }

    @Override
    public void connectError(AppException error) {
        super.connectError(error);
        try {
            mRefreshLayout.setRefreshing(false);
            if (data.size() <= 0) {
                Logger.i("wjl", "网络错误---");
                mMessageNetError.setVisibility(View.VISIBLE);
                mRefreshLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
