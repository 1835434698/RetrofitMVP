package com.cnepay.android.swiper.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.Utils;
import com.igeek.hfrecyleviewlib.RecycleScrollListener;
import com.igeek.hfrecyleviewlib.divider.LineVerComDecoration;
import com.igeek.hfrecyleviewlib.refresh.NestedRefreshLayout;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by millerJK on time : 2017/5/7
 * description :结算查询
 */
public abstract class BaseQueryActivity extends MvpAppCompatActivity
        implements View.OnClickListener {

    private final int DATE_LIMIT = 6 * 30;
    private RadioGroup mRadioGroup;
    private View mStartView, mStopView;
    private TextView mStartTv, mStopTv;
    private DatePickerDialog mPickerDialog;
    private TextView mTipTv;
    private boolean isStart;
    private Date startDate, endDate;
    private SimpleDateFormat sdf;
    public NestedRefreshLayout mRefreshLayout;
    public SwipeMenuRecyclerView mRecyclerView;
    public TextView mQueryNumTipTv, mTotalMoneyTipTv;
    public TextView mQueryNumTv, mTotalMoneyTv;
    public View mTimeoutView;
    public ImageView mTimeoutImg;
    public ProgressBar mProgressBar;
    public View noDataView;
    public String start, end, type;
    public int totalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_account_query);
        initView();
        initData();
        initListener();


    }

    private void initView() {
        uc.getRightText().setText(R.string.account_query_title);
        mStartView = findViewById(R.id.query_start_time);
        mStopView = findViewById(R.id.query_stop_time);
        mStartTv = (TextView) findViewById(R.id.query_start_time_tv);
        mStopTv = (TextView) findViewById(R.id.query_stop_time_tv);
        mTipTv = (TextView) findViewById(R.id.query_tip);
        mTimeoutView = findViewById(R.id.query_connect_timeout);
        mProgressBar = (ProgressBar) findViewById(R.id.query_timeout_pro);
        mTimeoutImg = (ImageView) findViewById(R.id.query_timeout_img);
        mRadioGroup = (RadioGroup) findViewById(R.id.query_radio);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.query_recyclerView);
        mRecyclerView.addItemDecoration(new LineVerComDecoration(this, LineVerComDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout = (NestedRefreshLayout) findViewById(R.id.query_refresh_lay);
        mTotalMoneyTipTv = (TextView) findViewById(R.id.query_money_tip);
        mQueryNumTipTv = (TextView) findViewById(R.id.query_list_num_tip);
        mQueryNumTv = (TextView) findViewById(R.id.query_list_num);
        mTotalMoneyTv = (TextView) findViewById(R.id.query_money);
        noDataView = getLayoutInflater().inflate(R.layout.account_query_no_data, null);
        TextView noDataTv = (TextView) noDataView.findViewById(R.id.query_no_data_tv);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) noDataTv.getLayoutParams();
        params.width = Utils.screenWidth(this);
        noDataTv.setLayoutParams(params);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        init();
    }

    private void initData() {
        ((RadioButton) mRadioGroup.getChildAt(2)).setChecked(true);
        type = "2";
        endDate = new Date();
        startDate = getDate(endDate, -30);
        mStartTv.setText(sdf.format(startDate));
        mStopTv.setText(sdf.format(endDate));
    }

    private void initListener() {
        mStartView.setOnClickListener(this);
        mStopView.setOnClickListener(this);
        mTimeoutView.setOnClickListener(this);
        uc.getRightText().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(() -> {
            resetNetting();
        });
        mRecyclerView.addOnScrollListener(mListener);

        mRadioGroup.setOnCheckedChangeListener(mChangeListener);
    }

    public String getType() {
        int id = mRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.query_all:
                return "0";
            case R.id.query_t1:
                return "1";
            case R.id.query_d0:
                return "2";
        }
        return "-1";
    }

    RecycleScrollListener mListener = new RecycleScrollListener() {
        @Override
        public void loadMore() {
            start = mStartTv.getText().toString();
            end = mStopTv.getText().toString();
            loadMoreNetting();
        }
    };

    public abstract void init();

    public abstract void loadMoreNetting();

    public abstract void resetNetting();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_start_time:
                isStart = true;
                showDialog(true);
                break;
            case R.id.query_stop_time:
                isStart = false;
                showDialog(false);
                break;
            case R.id.query_connect_timeout:
                startAnimation();
                break;
            case R.id.tvRight:
                totalNum = 0;
                mTipTv.setVisibility(View.GONE);
                start = mStartTv.getText().toString();
                end = mStopTv.getText().toString();
                resetNetting();
                break;
        }
    }

    private void showDialog(boolean isStart) {
        Date date;
        if (isStart)
            date = startDate;
        else
            date = endDate;

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        mPickerDialog = new DatePickerDialog(this, dataListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        mPickerDialog.show();
    }

    private RadioGroup.OnCheckedChangeListener mChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            type = getType();
        }
    };

    DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Date tempDate = new Date(year - 1900, month, dayOfMonth);
            Date today = new Date();
            if (tempDate.after(today)) {
                tempDate = today;
            }
            if (isStart) {
                startDate = tempDate;
                mStartTv.setText(sdf.format(tempDate));
                if (!checkDataValid(tempDate, endDate)) {
                    endDate = getDate(tempDate, DATE_LIMIT);
                    mStopTv.setText(sdf.format(getDate(tempDate, DATE_LIMIT)));
                }
            } else {
                endDate = tempDate;
                mStopTv.setText(sdf.format(tempDate));
                if (!checkDataValid(startDate, tempDate)) {
                    startDate = getDate(tempDate, -DATE_LIMIT);
                    mStartTv.setText(sdf.format(startDate));
                }
            }
        }
    };


    private void startAnimation() {
        start = mStartTv.getText().toString();
        end = mStopTv.getText().toString();
        mRefreshLayout.setVisibility(View.GONE);
        mTimeoutView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTimeoutImg.setVisibility(View.INVISIBLE);
        resetNetting();
    }

    /**
     * @param date     startDate format yyyy-mm-dd
     * @param distance day number
     */
    public Date getDate(Date date, int distance) {
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(date);
        theCa.add(theCa.DATE, distance);
        Date today = new Date();
        Date stop = theCa.getTime();
        if (stop.after(today))
            stop = today;
        return stop;
    }

    public boolean checkDataValid(Date start, Date end) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(start);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);

        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1) <= DATE_LIMIT && timeDistance + (day2 - day1) >= 0 ? true : false;
        } else {
            return (day2 - day1) <= DATE_LIMIT && (day2 - day1) >= 0 ? true : false;
        }
    }


}
