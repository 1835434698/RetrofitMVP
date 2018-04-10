package com.cnepay.android.swiper.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BannerBean;
import com.cnepay.android.swiper.bean.BannerBodyBean;
import com.cnepay.android.swiper.bean.DownLoadBannerBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.presenter.BannerPresenter;
import com.cnepay.android.swiper.presenter.DownloadBannerPresenter;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.BannerView;
import com.cnepay.android.swiper.view.DownloadBannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by millerJK on time : 2017/5/7
 * description : 热门活动
 */
public class HotEventActivity extends MvpAppCompatActivity implements BannerView,DownloadBannerView {
    private static final int GET_BANNER_SUCCESS = 0x001;
    private static final int GET_BANNER_IMAGES_SUCCESS = 0x002;

    private ViewPager mViewPager;
    private BannerPresenter bannerPresenter;
    private DownloadBannerPresenter downloadPresenter;
    private ArrayList<BannerBodyBean> data = new ArrayList<>();
    private ArrayList<DownLoadBannerBean> images=new ArrayList<>();
    private AdPagerAdapter pagerAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_BANNER_SUCCESS:
////                    for (int i = 0; i <data.size() ; i++) {
                        downloadPresenter.downloadBanner("201705/121/20170503151247883picAndroid.jpg", K.BANNER);
////                    }
                    pagerAdapter = new AdPagerAdapter(HotEventActivity.this,data);
                    mViewPager.setAdapter(pagerAdapter);
                    break;
                case GET_BANNER_IMAGES_SUCCESS:
//                    pagerAdapter = new AdPagerAdapter(HotEventActivity.this,images);
//                    mViewPager.setAdapter(pagerAdapter);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewPure(R.layout.activity_hot_event);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.hot_event_vp);
        bannerPresenter = new BannerPresenter();
        bannerPresenter.attachView(this);
        downloadPresenter=new DownloadBannerPresenter();
        downloadPresenter.attachView(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bannerPresenter.getBannerList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerPresenter.detachView(false);
        downloadPresenter.detachView(false);
    }

    @Override
    public void getBanner(Object content) {
        if (content instanceof BannerBean) {
            BannerBean bean = (BannerBean) content;
            data = (ArrayList<BannerBodyBean>) bean.getBody();
            Logger.i("wjl","数据： "+data.toString());
            Message message=handler.obtainMessage(GET_BANNER_SUCCESS);
            handler.sendMessage(message);
        }
    }

    @Override
    public void downloadbanner(Object content) {
        Logger.i("wjl","下载成功图片");
        DownLoadBannerBean bean= (DownLoadBannerBean) content;
        images.add(bean);
    }


    class AdPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<BannerBodyBean> data;

        public AdPagerAdapter(Context mContext,List<BannerBodyBean> data) {
            this.data = data;
            this.mContext = mContext;
            Logger.i("wjl","一共几个数据--------："+data.size());
        }

        @Override
        public int getCount() {
           return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View item = getLayoutInflater().inflate(R.layout.hot_event_item, null);
            ImageView mAdImage = (ImageView) item.findViewById(R.id.ad_show);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mAdImage.getLayoutParams();
            layoutParams.width = (int) (0.8f * screenWidth());
            layoutParams.height = (int) (screenHeight() * 2.0f / 3);
            mAdImage.setLayoutParams(layoutParams);
            container.addView(item);

            View line = item.findViewById(R.id.ad_close_line);
            LinearLayout.LayoutParams lineLp = (LinearLayout.LayoutParams) line.getLayoutParams();
            lineLp.height = (int) (screenHeight() * 2.0f / 3 / 10);
            line.setLayoutParams(lineLp);

            View close = item.findViewById(R.id.ad_close);
            close.setOnClickListener(v -> finish());
            String imgurl="http://192.168.1.240:29110/downloadImg.action?type=banner&fileName=201705/121/20170503151247883picAndroid.jpg&appVersion=android.wealth.3.0.0&sign=appVersion%3Dandroid.wealth.3.0.0%26fileName%3D201705/121/20170503151247883picAndroid.jpg%26type%3Dbanner";

            Logger.i("wjl","加载了那张图："+imgurl);
            Glide.with(mContext)
                    .load(imgurl)
                    .into(mAdImage);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    public int screenWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int screenHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
