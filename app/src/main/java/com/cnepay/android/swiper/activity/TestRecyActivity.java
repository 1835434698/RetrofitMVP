package com.cnepay.android.swiper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.adapter.AccountQueryAdapter;
import com.cnepay.android.swiper.bean.AccountQueryItem;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.RecycleScrollListener;
import com.igeek.hfrecyleviewlib.divider.LineVerComDecoration;
import com.igeek.hfrecyleviewlib.refresh.NestedRefreshLayout;
import com.igeek.hfrecyleviewlib.swipe.Closeable;
import com.igeek.hfrecyleviewlib.swipe.OnSwipeMenuItemClickListener;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenu;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuCreator;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuItem;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by millerJK on time : 2017/5/10
 * <ul>
 * <li>{@link SwipeMenuRecyclerView}  下拉刷新控件</li>
 * <li>{@link NestedRefreshLayout}  RecyclerView</li>
 * </ul>
 */
public class TestRecyActivity extends MvpAppCompatActivity {

    private static final int PULL_REFRESH = 0;
    private static final int LOAD_MORE = 1;

    private SwipeMenuRecyclerView mRecyclerView;
    private NestedRefreshLayout mRefreshLayout;
    private AccountQueryAdapter mAdapter;

    //联网请求使用Handler 进行模拟
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_MORE:
                    ArrayList lists = (ArrayList) createData();
                    mAdapter.appendDatas(lists);
                    break;

                case PULL_REFRESH:
                    ArrayList lists1 = (ArrayList) createData();
                    mRefreshLayout.setRefreshing(false);
                    mAdapter.resetDatas(lists1);
                    break;
            }

            //调用完毕
            mAdapter.onLoadComplete();

            //是否还有数据 ******
            if (mAdapter.getRealDataCount() > 15) {
                mRefreshLayout.setForbidRefresh(true); /** 禁止下拉刷新,默认为false**/
                mAdapter.isNeedLoadMore(false);
            } else {
                mAdapter.isNeedLoadMore(true);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_test_recy);
        uc.setTitle("SwipeMenuRecyclerView");
        initView();
        initData();
        initListener();
    }

    private void initView() {

        TextView mHeadView = new TextView(this);
        mHeadView.setText("header1");
        TextView mHeadView1 = new TextView(this);
        mHeadView1.setText("header2");
        TextView mFootView = new TextView(this);
        mFootView.setText("footer1");
        TextView mFootView1 = new TextView(this);
        mFootView1.setText("foot2");
        TextView nodataView = new TextView(this);
        nodataView.setText("no data");


        mRefreshLayout = (NestedRefreshLayout) findViewById(R.id.test_refresh_lay);
        mRefreshLayout.setForbidRefresh(false); /** 禁止下拉刷新,默认为false**/
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.test_recyclerView);
        mRecyclerView.addItemDecoration(new LineVerComDecoration(this, LineVerComDecoration.VERTICAL_LIST)); /***设置Item间分割线***/
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*** 设置布局管理器
         {@link android.support.v7.widget.LinearLayoutManager}
         {@link android.support.v7.widget.GridLayoutManager}
         {@link android.support.v7.widget.StaggeredGridLayoutManager}
         ***/

        mAdapter = new AccountQueryAdapter(R.layout.account_query_item);
        mAdapter.addHeadView(mHeadView);
        mAdapter.addHeadView(mHeadView1);/*** add Head ***/
        mAdapter.addFootView(mFootView);
        mAdapter.addFootView(mFootView1);/*** add Foot ***/
        mAdapter.setSwipeMenuCreator(swipeMenuCreator);/*** 若不使用左右滑动功能Item，无需添加 ***/
        mAdapter.addNoDataView(nodataView);
        mAdapter.addLoadMoreView();/***若不使用上拉加载，无需添加***/
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mAdapter.resetDatas(new ArrayList<AccountQueryItem>());
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(refreshListener); //下拉刷新监听
        mRecyclerView.addOnScrollListener(scrollListener);   //上拉加载更多监听
        mAdapter.setItemClickListener(itemListener);         //ItemClickListener
        mAdapter.setSwipeMenuItemClickListener(swipeClickListener);/**左右拖动菜单点击监听,若未使用{@link com.igeek.hfrecyleviewlib.CommonAdapter#setSwipeMenuCreator(SwipeMenuCreator)}方法，无需使用此监听***/
    }

    NestedRefreshLayout.OnRefreshListener refreshListener = new NestedRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //todo  add netting here
            Message msg = mHandler.obtainMessage();
            msg.what = PULL_REFRESH;
            mHandler.sendMessageDelayed(msg, 2000);
        }
    };

    RecycleScrollListener scrollListener = new RecycleScrollListener() {
        @Override
        public void loadMore() {
            //todo  add netting here
            Message msg = mHandler.obtainMessage();
            msg.what = LOAD_MORE;
            mHandler.sendMessageDelayed(msg, 2000);
        }
    };

    CommonViewHolder.OnItemClickListener itemListener = new CommonViewHolder.OnItemClickListener() {
        @Override
        public void OnItemClick(View v, int adapterPosition) {
            uc.toast("adapterPosition:" + adapterPosition);
            mRefreshLayout.setForbidRefresh(false); /** 禁止下拉刷新,默认为false**/
        }
    };

    OnSwipeMenuItemClickListener swipeClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            uc.toast(adapterPosition + "  " + menuPosition + "  " + direction);
            if (menuPosition == 0)
                mAdapter.removeData(0);
            else {
                AccountQueryItem item = new AccountQueryItem("2012.10.21", "结算失败", "insert", "￥123");
                mAdapter.insertData(0, item);
            }

        }
    };

    /**
     * Slide the menu builder, support left or right . Do not init this if you don't want this feature .
     * {@link com.igeek.hfrecyleviewlib.CommonAdapter#setSwipeMenuCreator(SwipeMenuCreator)}
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int width = getResources().getDimensionPixelSize(R.dimen.height_49dp);
            // MATCH_PARENT 自适应高度，保持和内容一样高
            int height = getResources().getDimensionPixelSize(R.dimen.height_49dp);
            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(TestRecyActivity.this)
                        .setBackgroundDrawable(getResources().getDrawable(R.color.color3F3F3F))// 点击的背景。
                        .setImage(R.drawable.icon_fail) // 图标。
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(TestRecyActivity.this)
                        .setBackgroundDrawable(getResources().getDrawable(R.color.colorFBAF47))
                        .setImage(R.drawable.icon_success)
                        .setWidth(width)
                        .setHeight(height);

                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
                swipeLeftMenu.setOrientation(SwipeMenu.HORIZONTAL);
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(TestRecyActivity.this)
                        .setBackgroundDrawable(getResources().getDrawable(R.color.color3F3F3F))
                        .setImage(R.drawable.icon_success)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(TestRecyActivity.this)
                        .setBackgroundDrawable(getResources().getDrawable(R.color.colorFBAF47))
                        .setImage(R.drawable.icon_fail)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

            }
        }
    };

    public List createData() {
        ArrayList lists = new ArrayList();
        lists.add(new AccountQueryItem("2x1x.10.21", "结算成功", "D1", "￥13124"));
        lists.add(new AccountQueryItem("2012.xx.21", "结算成功", "T+0", "￥12321"));
        lists.add(new AccountQueryItem("2012.10.2x", "结算成功", "T+0", "￥12.21"));
//        lists.add(new AccountQueryItem("x012.10.21", "结算成功", "T+0", "￥651"));
//        lists.add(new AccountQueryItem("20x2.10.x1", "结算失败", "T+0", "￥123"));
        return lists;
    }

}
