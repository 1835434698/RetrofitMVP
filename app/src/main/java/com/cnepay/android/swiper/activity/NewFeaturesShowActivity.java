package com.cnepay.android.swiper.activity;

import android.content.Context;
import android.os.Bundle;
import com.cnepay.android.swiper.widget.AnimPagerAdapter;
import com.cnepay.android.swiper.widget.AnimViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.anim.NfsActTransformer;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.Logger;

/**
 * Created by tangzy on 2017/4/27.
 */

public class NewFeaturesShowActivity extends MvpAppCompatActivity{

    private static final String TAG = "NewFeaturesShowActivity";
    private int[] resIds = {R.drawable.new_feature_1, R.drawable.new_feature_2,
            R.drawable.new_feature_3};
    private int[] featureIds = {R.drawable.ic_nfs_feature_1, R.drawable.ic_nfs_feature_2,
            R.drawable.ic_nfs_feature_3};
    private int[] lightIds = {R.drawable.ic_nfs_light_1, R.drawable.ic_nfs_light_2,
            R.drawable.ic_nfs_light_3};
    private int[] foundationIds = {R.drawable.ic_nfs_foundation_1, R.drawable.ic_nfs_foundation_2,
            R.drawable.ic_nfs_foundation_3};
    private int[] infoIds = {R.string.feature_info1, R.string.feature_info2,
            R.string.feature_info3};


    private TextView tv_info;
    private ImageView iv_feature;
    private RelativeLayout rl_feature;
    private ImageView iv_foundation;
    private ImageView iv_light;


    private AnimViewPager vp_content;
    private LinearLayout indicator;
    private int currPosition;
    private int lastPosition;

    private NewPagerAdapter adapter;

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        uc.setContentViewPure(R.layout.activity_newfeatures);
        indicator = (LinearLayout) findViewById(R.id.ll_viewpager_indicator);
        vp_content = (AnimViewPager) findViewById(R.id.viewpager);
        rl_feature = (RelativeLayout) findViewById(R.id.rl_feature);
        iv_feature = (ImageView) findViewById(R.id.iv_feature);

        iv_foundation = (ImageView) findViewById(R.id.iv_foundation);

        iv_light = (ImageView) findViewById(R.id.iv_light);

        tv_info = (TextView) findViewById(R.id.tv_info);
        width = ((WindowManager)(this.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth();
        Logger.d(TAG, "width = "+width);

        init();

    }

    private NfsActTransformer nfsActTransformer;

    private void init(){
        for (int i = 0; i < resIds.length; i++) {
            View view = View.inflate(this, R.layout.item_newf_pointer, null);
            ImageView image = (ImageView) view.findViewById(R.id.item_pointer_image);
            if (i == 0) {
                image.setImageResource(R.drawable.pointer_checked);
            } else {
                image.setImageResource(R.drawable.pointer_unchecked);
            }
            indicator.addView(view);
        }
        adapter = new NewPagerAdapter();
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(pageChangeListener);
        nfsActTransformer = new NfsActTransformer(this);
        nfsActTransformer.setInfo(tv_info);
        nfsActTransformer.setFeature(rl_feature);
        nfsActTransformer.setLight(iv_light);
        nfsActTransformer.setFoundation(iv_foundation);
        vp_content.setPageTransformer(true, nfsActTransformer);
    }


    /**
     * 页面切换监听
     */
    private AnimViewPager.OnPageChangeListener pageChangeListener = new AnimViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            Logger.d(TAG, "Anim onPageScrolled ------ i = "+i);
        }

        @Override
        public void onPageSelected(int position) {
            Logger.d(TAG, "Anim onPageSelected ----- position = "+position);
            ImageView image = (ImageView) indicator.getChildAt(lastPosition).findViewById(R.id.item_pointer_image);
            image.setImageResource(R.drawable.pointer_unchecked);
            image = (ImageView) indicator.getChildAt(position).findViewById(R.id.item_pointer_image);
            image.setImageResource(R.drawable.pointer_checked);
            refreshData(position);
            lastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

        @Override
        public void onPageSelecting(int position) {
            Logger.d(TAG, "Anin position = "+position+"-----currPosition = "+currPosition);
            refreshData(position);
        }
    };

    private void refreshData(int position) {
        Logger.d(TAG, "refreshData.currPosition = "+ this.currPosition);
        if (position != currPosition){
            currPosition = position ;
            iv_feature.setImageResource(featureIds[this.currPosition]);
            iv_feature.invalidate();
            iv_foundation.setImageResource(foundationIds[this.currPosition]);
            iv_light.setImageResource(lightIds[this.currPosition]);
            tv_info.setText(infoIds[this.currPosition]);
            tv_info.invalidate();
        }
    }

    class NewPagerAdapter extends AnimPagerAdapter {
        ViewHolder holder;
        public ViewHolder getHolder(){
            return holder;
        }
        @Override
        public int getCount() {
            return resIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Logger.i(TAG, "Object:"+object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (view == null) {
                view = View.inflate(NewFeaturesShowActivity.this, R.layout.item_viewpager, null);
                holder = new ViewHolder();

                holder.rlbg = (RelativeLayout) view.findViewById(R.id.viewpager_imageview);
                holder.rlbg.setBackgroundResource(resIds[position]);
//
//                holder.iv_feature = (ImageView) view.findViewById(R.id.iv_feature);
//                holder.iv_feature.setImageResource(featureIds[position]);
//
//                holder.iv_foundation = (ImageView) view.findViewById(R.id.iv_foundation);
//                holder.iv_foundation.setImageResource(foundationIds[position]);
//
//                holder.iv_light = (ImageView) view.findViewById(R.id.iv_light);
//                holder.iv_light.setImageResource(lightIds[position]);
//
//                holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
//                holder.tv_info.setText(infoIds[position]);

                if((resIds.length - 1) == position ){
                    Button button = (Button) view.findViewById(R.id.btn_open_world);

                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
                container.addView(view);
            } else {
                holder = (ViewHolder) container.getTag();
            }

            return view;
        }

        class ViewHolder {
            RelativeLayout rlbg;
        }
    }

    @Override
    public void onBackPressed() {
//		super.onBackPressed();
    }
}
