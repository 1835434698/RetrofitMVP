package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.FuncItem;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.bean.MineMessageBean;
import com.cnepay.android.swiper.presenter.LocationPresenter;
import com.cnepay.android.swiper.presenter.MerchantQualifyPresenter;
import com.cnepay.android.swiper.presenter.MineMessagePresenter;
import com.cnepay.android.swiper.presenter.SignPresenter;
import com.cnepay.android.swiper.utils.MeasureUtils;
import com.cnepay.android.swiper.utils.SharedPre;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.LocationView;
import com.cnepay.android.swiper.view.MerchantQualifyView;
import com.cnepay.android.swiper.view.MineMessageQueryView;
import com.cnepay.android.swiper.view.SignView;
import com.cnepay.android.swiper.widget.CirView;
import com.cnepay.android.swiper.widget.NestGridView;
import com.cnepay.android.swiper.widget.ReboundScrollView;
import com.cnepay.android.swiper.widget.StickyNavLayout;

import java.util.ArrayList;
import java.util.List;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnRightPosCallback;
import zhy.com.highlight.shape.BaseLightShape;
import zhy.com.highlight.shape.CircleLightShape;

import static com.cnepay.android.swiper.R.id.home_page_rotate;
import static com.cnepay.android.swiper.utils.Utils.PREF_VERSION_NAME;

/**
 * created by millerJK on time : 2017/5/7
 * description : 首页
 */

public class HomePageActivity extends CheckPerssionsActivity
        implements View.OnClickListener
        , MerchantQualifyView
        , MineMessageQueryView
        , SignView
        , LocationView {

    private static final int SHOW_TIP = 0;

    private CirView cirView;
    private ImageView mPullImg;
    private NestGridView mGridView;
    private ReboundScrollView mReboundScrollView;
    private StickyNavLayout mStickyNavLayout;
    private HighLight mHighLight;
    private SharedPre sp;

    private MineMessagePresenter messagePresenter;
    private MerchantQualifyPresenter mQualifyPresenter;
    private SignPresenter mSignPresenter;
    private LocationPresenter mLocationPresenter;
    private int mClickResId;

    private List<FuncItem> items = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_TIP:
                    showTip();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_home_page);
        initViews();
        initData();
        initListener();
        if (checkIsFirstInstall()) {
            mHandler.sendEmptyMessageDelayed(SHOW_TIP, 100);
        }

        uc.setTitleIcon(R.drawable.home_page_logo);
        uc.getLeftText().setText("热门推荐");
        uc.getLeftTip().setImageResource(R.drawable.hot);
        uc.getRightText().setBackgroundResource(R.drawable.sys_msg);
        uc.getRightTip().setImageResource(R.drawable.sys_msg_point);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        uc.hideBackIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginBean loginBean = requestLoginBean();
        if (loginBean!=null){
            cirView.updateStatus(CirView.LEFT_BOTTOM, loginBean != null && loginBean.isDeviceKeysPrepared() ? CirView.ALREADY_OPEN : CirView.NOT_OPEN);
            cirView.updateStatus(CirView.RIGHT_BOTTOM, loginBean != null && loginBean.isDeviceBound() ? CirView.ALREADY_BIND : CirView.NOT_BIND);
        }
    }

    private boolean checkIsFirstInstall() {
        sp = SharedPre.getInstance(this);
        String lastVersion = sp.getStringValue(PREF_VERSION_NAME, null);
        String currVersion = Utils.getVersionName(this);
        if (lastVersion == null)
            return true;
        else if (!lastVersion.equals(currVersion)) {
            sp.saveStringValue(PREF_VERSION_NAME, currVersion);
        }
        return false;
    }


    private void initViews() {
        cirView = (CirView) findViewById(R.id.home_page_cirView);
        mReboundScrollView = (ReboundScrollView) findViewById(R.id.home_page_scroll);
        mReboundScrollView.forbidUpRebound(true);
        mReboundScrollView.forbidDownRebound(true);
        mStickyNavLayout = (StickyNavLayout) findViewById(R.id.home_page_sticky);
        mPullImg = (ImageView) findViewById(home_page_rotate);
        mGridView = (NestGridView) findViewById(R.id.home_page_gridView);
        messagePresenter = new MineMessagePresenter();
        messagePresenter.attachView(this);
        mQualifyPresenter = new MerchantQualifyPresenter();
        mQualifyPresenter.attachView(this);
        mSignPresenter = new SignPresenter();
        mSignPresenter.attachView(this);
        mLocationPresenter = new LocationPresenter();
        mLocationPresenter.attachView(this);
    }

    private void initData() {

        items.add(new FuncItem(R.drawable.bind_credit_card, R.string.bind_credit_card_tip));
        items.add(new FuncItem(R.drawable.bind_debit_card, R.string.bind_debit_card_tip));
        items.add(new FuncItem(R.drawable.account_query, R.string.account_query_tip));
        items.add(new FuncItem(R.drawable.transaction_details, R.string.transaction_query_tip));
        items.add(new FuncItem(R.drawable.balance_query, R.string.balance_query_tip));
        items.add(new FuncItem(R.drawable.change_device, R.string.change_device_tip));
        items.add(new FuncItem(R.drawable.normal_question, R.string.normal_question_tip));
        items.add(new FuncItem(R.drawable.home_mine, R.string.home_mine_tip));

        mGridView.setAdapter(new FuncAdapter());
        messagePresenter.queryMineMessage(true);
    }

    private void initListener() {
        mPullImg.setOnClickListener(this);
        uc.getLeftText().setOnClickListener(this);
        uc.getRightText().setOnClickListener(this);
        cirView.setOnItemClickListener(mItemClickListener);
        mStickyNavLayout.setOnScrollListener(distance ->
                mPullImg.setRotation(distance / cirView.getHeight() * 180));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messagePresenter.detachView(false);
        mQualifyPresenter.detachView(false);
        mLocationPresenter.detachView(false);
        mSignPresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_page_rotate:
                updateIndicate();
                break;
            case R.id.tvLeft:
//                ac.startActivity(new Intent(this, HotEventActivity.class));
                Intent intent = new Intent(this, HotEventActivity.class);
                startActivity(intent);
                break;
            case R.id.tvRight:
                if (uc.getRightTip().getVisibility() == View.VISIBLE) {
                    uc.getRightTip().setVisibility(View.INVISIBLE);
                }
                Intent messageIntent = new Intent(this, MineMessageActivity.class);
                ac.startActivity(messageIntent);
                break;
        }
    }


    private void updateIndicate() {
        if (mPullImg.getRotation() == 0) {
            mStickyNavLayout.pushUp();
        } else if (mPullImg.getRotation() == 180) {
            mStickyNavLayout.pullDown();
        }
    }

    /**
     * first install app will show tip
     */
    public void showTip() {

        sp.saveStringValue(PREF_VERSION_NAME, Utils.getVersionName(this));

        mHighLight = new HighLight(this)
                .addHighLight(R.id.tvRight, R.layout.tip_msg
                        , new OnBottomPosCallback(40) {
                            @Override
                            public void posOffset(float rightMargin, float bottomMargin
                                    , RectF rectF, HighLight.MarginInfo marginInfo) {
                                super.posOffset(rightMargin, bottomMargin, rectF, marginInfo);

                                marginInfo.leftMargin = rectF.left
                                        - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35
                                        , getResources().getDisplayMetrics());
                            }
                        }, new CircleLightShape())
                .addHighLight(R.id.home_page_cirView, R.layout.tip_circle
                        , new OnRightPosCallback() {
                            @Override
                            public void posOffset(float rightMargin, float bottomMargin
                                    , RectF rectF, HighLight.MarginInfo marginInfo) {
                                marginInfo.leftMargin = rectF.right / 3 * 2 + offset;
                                marginInfo.topMargin = rectF.top + rectF.right / 3 * 2;
                            }
                        }
                        , new BaseLightShape(MeasureUtils.getScreenWidth(HomePageActivity.this) / 7
                                , MeasureUtils.getScreenWidth(HomePageActivity.this) / 7) {
                            @Override
                            protected void resetRectF4Shape(RectF viewPosInfoRectF, float dx, float dy) {
                                viewPosInfoRectF.inset(dx, dy);
                            }

                            @Override
                            protected void drawShape(Bitmap bitmap, HighLight.ViewPosInfo viewPosInfo) {
                                Canvas canvas = new Canvas(bitmap);
                                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                paint.setDither(true);
                                paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
                                RectF rectF = viewPosInfo.rectF;
                                canvas.drawOval(rectF, paint);
                            }
                        })
                .addHighLight(R.id.home_page_gridView, R.layout.tip_grid, new OnRightPosCallback()
                        , new BaseLightShape(0, 0) {
                            @Override
                            protected void resetRectF4Shape(RectF viewPosInfoRectF, float dx, float dy) {
                                viewPosInfoRectF.offset(0,
                                        -TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10
                                                , getResources().getDisplayMetrics()));
                            }

                            @Override
                            protected void drawShape(Bitmap bitmap, HighLight.ViewPosInfo viewPosInfo) {
                                Canvas canvas = new Canvas(bitmap);
                                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                paint.setDither(true);
                                paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
                                RectF rectF = viewPosInfo.rectF;
                                canvas.drawRect(rectF, paint);
                            }
                        })
                .addHighLight(R.id.home_page_rotate, R.layout.tip_rotate, new OnRightPosCallback(5), new CircleLightShape())
                .autoRemove(false)
                .enableNext()
                .setClickCallback(() -> mHighLight.next());

        mHighLight.show();

    }

    @Override
    public void queryMineMessage(Object connect) {
        if (connect != null && connect instanceof MineMessageBean) {
            MineMessageBean bean = (MineMessageBean) connect;
            LoginBean loginBean = requestLoginBean();
            if (loginBean==null) return;
            loginBean.setMessageBean(bean);
            if (bean.getHead().getUnReadCount() > 0) {
                uc.getRightTip().setVisibility(View.VISIBLE);
            } else {
                uc.getRightTip().setVisibility(View.INVISIBLE);
            }
        }

    }

    class FuncAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(HomePageActivity.this, R.layout.func_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mView.setOnClickListener(v
                    -> intentActivity(items.get(position).tipId));
            viewHolder.mTextView.setText(items.get(position).tipId);
            viewHolder.mImageView.setImageResource(items.get(position).picId);
            return convertView;
        }

        class ViewHolder {

            View mView;
            ImageView mImageView;
            TextView mTextView;

            public ViewHolder(View convertView) {
                mView = convertView.findViewById(R.id.func_item_lay);
                mImageView = (ImageView) convertView.findViewById(R.id.func_item_img);
                mTextView = (TextView) convertView.findViewById(R.id.func_item_tv);
            }
        }

    }

    private void intentActivity(int tipId) {

        mClickResId = tipId;

        switch (tipId) {
            case R.string.bind_credit_card_tip:
            case R.string.home_mine_tip:
                mQualifyPresenter.merchantQualify();
                break;
            case R.string.bind_debit_card_tip:
                ac.startActivity(new Intent(this, CertificationReplaceSettlementCardActivity.class));
                break;
            case R.string.account_query_tip:
                ac.startActivity(new Intent(this, SettleQueryActivity.class));
                break;
            case R.string.transaction_query_tip:
                ac.startActivity(new Intent(this, TransactionQueryActivity.class));
                break;
            case R.string.balance_query_tip:
                ac.startActivity(new Intent(this, BankBalanceEnquiryActivity.class));
                break;
            case R.string.change_device_tip:
                ac.startCallbackActivity(new Intent(this, ReplaceDevTipsActivity.class));
                break;
            case R.string.normal_question_tip:
                ac.startWebActivity(this, WebViewActivity.DEFAULT_WEB_VIEW, "faq-zft.html", getString(R.string.mine_fqz));
                break;
        }
    }

    @Override
    public void updateUI(LoginBean.MerchantQualify entity) {

        refreshMerchantQualify(entity);
        switch (mClickResId) {
            case R.string.home_mine_tip:
                ac.startCallbackActivity(new Intent(this, MineActivity.class));
                break;
            case R.string.bind_credit_card_tip:
                if (!checkIfAllPass(entity)) {
                    //// FIXME: 2017/5/22 跳转到相应界面
                    uc.toast(R.string.verify_no_pass);
                    return;
                } else
                    ac.startActivity(new Intent(this, BindCardActivity.class));
                break;
            case R.id.home_page_cirView:
                if (!checkIfAllPass(entity)) {
                    //// FIXME: 2017/5/22 跳转到相应界面
                    uc.toast(R.string.verify_no_pass);
                    return;
                } else {
                    //定位
                    mLocationPresenter.location();
                }
                break;
        }
    }

    //resave merchant status data
    private void refreshMerchantQualify(LoginBean.MerchantQualify entity) {
        LoginBean loginBean = requestLoginBean();
        if (loginBean!=null)
            loginBean.setMerchantQualify(entity);
    }

    private boolean checkIfAllPass(LoginBean.MerchantQualify entity) {
        if (entity.getTerminalAuth() == 1
                && entity.getRealNameAuth() == 1
                && entity.getMerchantAuth() == 1
                && entity.getAccountAuth() == 1
                && entity.getSignatureAuth() == 1)
            return true;
        return false;
    }

    CirView.OnItemClickListener mItemClickListener = type -> {

        if (checkNeedPermissions(needPermissions))
            return;

        mClickResId = R.id.home_page_cirView;
        mQualifyPresenter.merchantQualify();
        LoginBean loginBean = requestLoginBean();
        if (loginBean==null) return;
        if (!loginBean.isDeviceBound()) {
            new AlertDialog.Builder(obtainContext()).setMessage("您还没有绑定设备!").setPositiveButton("去绑定", (dialog, which) -> {
                Intent intent = new Intent(HomePageActivity.this, BindDeviceActivity.class);
                ac.startCallbackActivity(intent);
            }).show();
        } else if (!loginBean.getAuthResult().equals(loginBean.getMerchantQualify().strAuthResult(LoginBean.MerchantQualify.AUTH_TYPE_ALL_PASS))) {//四审状态不是全通过
            if (loginBean.getAuthResult().equals(loginBean.getMerchantQualify().strAuthResult(LoginBean.MerchantQualify.AUTH_TYPE_UNDERWAY))){//正在审核中，且无可编辑项
                // TODO: 2017/5/22 提示用户
            }else {//某一条目待编辑
                // TODO: 2017/5/22 提示并合理跳转
                new AlertDialog.Builder(obtainContext()).setMessage("您还没有进行资质认证!").setPositiveButton("去认证", (dialog, which) -> {
                    Intent intent = new Intent(HomePageActivity.this, CertificationRealNameActivity.class);
                    ac.startCallbackActivity(intent);
                }).show();
            }
        } else if (loginBean.isDeviceKeysPrepared()) {//已签到
            Intent intent = new Intent(HomePageActivity.this, InstallMoneyActivity.class);
            ac.startCallbackActivity(intent);
        }else {
            // TODO: 2017/5/22 去签到
        }
    };

    @Override
    public void permissionDeny() {
    }

    @Override
    public void locationSuccess(String position) {
        mSignPresenter.signin(position);//签到
    }

    @Override
    public void locationFailed(int errorCode) {
        if (errorCode == 4) { //网络异常
            uc.toast(R.string.netting_time_out_check_net);
        } else if (errorCode == 12) { //缺少定位权限
            showMissingPermissionDialog();
        }
    }

    @Override
    public void signSuccess() {
        uc.toast("签到成功");
    }

    @Override
    public void signFailed() {
        uc.toast("签到失败");
    }
}

