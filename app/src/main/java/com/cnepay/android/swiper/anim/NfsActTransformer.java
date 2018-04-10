package com.cnepay.android.swiper.anim;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.widget.AnimViewPager;

public class NfsActTransformer implements AnimViewPager.PageTransformer {
	private static final String TAG = "NfsActTransformer";
	private float LIMITMIN = 0.4f;
	private float LIMITMAX = 0.6f;
	private Context context;
	private int width ;
	private float infoX;
	private float infoY;
	public NfsActTransformer(Context context){
		this.context = context;
		width = ((WindowManager)(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth();
	}

	private TextView info;
	private RelativeLayout feature;
	private ImageView light;
	private ImageView foundation;


	public void setInfo(TextView info) {
		this.info = info;
		infoX = info.getX();
		infoY = info.getY();
	}
	float featureX;
	float featureY;
	public void setFeature(RelativeLayout feature) {
		this.feature = feature;
		featureX = feature.getX();
		featureY = feature.getY();
	}

	float lightX;
	float lightY;
	public void setLight(ImageView light) {
		this.light = light;
		lightX = light.getX();
		lightY = light.getY();
	}

	float foundationX;
	float foundationY;
	public void setFoundation(ImageView foundation) {
		this.foundation = foundation;
		foundationX = foundation.getX();
		foundationY = foundation.getY();
	}

	@Override
	public void transformPage(View page, float position) {
		Logger.d(TAG, "position = "+position);
		position = ((position*width)%width)/width;
		float scale = position < 0 ? - position : 1f - position;
		Logger.d(TAG, "scale = "+scale);
		startAnimation(scale);
	}
	private void startAnimation(float scale) {
		float v;
		float v1;
		if(scale < LIMITMIN){
			v = scale * width;
			v1 = 1f - scale * 2.5f;

			if (light != null && foundation != null){
				int x = (int) (foundationY +(v/1.5));
				light.scrollTo(x, (int) foundationY);
				light.setAlpha(v1);
				foundation.scrollTo(x, (int) foundationY);
				foundation.setAlpha(v1);
			}
			if (info!= null){
				int x = (int) (infoX +(v/1.5));
				info.scrollTo(x, (int) (infoY -v/5));
				info.setAlpha(v1);
			}
			if (feature != null){
				int x = (int) (featureY +(v/1.5));
				feature.scrollTo(x, (int) (featureY +v/2));
				feature.setAlpha(v1);
			}
		}else if(scale > LIMITMAX){
			v = (1-scale) * width;
			v1 = scale*2.5f -1.5f;
			if (light != null && foundation != null){
				int x = (int) (foundationY -(v/1.5));
				light.scrollTo(x,(int) foundationY);
				light.setAlpha(v1);
				foundation.scrollTo(x,(int) foundationY);
				foundation.setAlpha(v1);
			}
			if (info!= null){
				int x = (int) (infoX -(v/1.5));
				info.scrollTo(x,(int) (infoY -v/5));
				info.setAlpha(v1);
			}
			if (feature != null){
				int x = (int) (featureY -(v/1.5));
				feature.scrollTo(x,(int) (featureY +v/2));
				feature.setAlpha(v1);
			}
		}else {

			if (light != null && foundation != null){
				light.setAlpha(0f);
				foundation.setAlpha(0f);
			}
			if (info!= null){
				info.setAlpha(0f);
			}
			if (feature != null){
				feature.setAlpha(0f);
			}
		}
	}

}
