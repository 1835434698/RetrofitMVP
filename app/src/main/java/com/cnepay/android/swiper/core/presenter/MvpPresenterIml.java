package com.cnepay.android.swiper.core.presenter;

import com.cnepay.android.swiper.core.view.MvpView;

import java.lang.ref.WeakReference;

/**
 * Created by xugang on 2017/4/24.
 * Describe
 */

public class MvpPresenterIml<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> viewRef;

    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    public V getView(){
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    @Override
    public void detachView(boolean saveInstance) {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

}
