package com.mooc.libcommon.extention;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

public class LiveDataBus {

//
//    Handler mHandler = new Handler(Looper.getMainLooper()){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//        }
//    };
//    mHandler.sendMessage(msg)

    //正常的事件
    // LiveData mLiveData=null;
    // mLiveData.observer(this,new Observer<User>){
    //        void onChanged(User user){
    //
    //        }
    //    }
    //mLiveData.postValue(data);

    //黏性事件。先发送。后注册监听
//    LiveData mLiveData=null;
//    mLiveData.postValue(data);
//    mLiveData.observer(this,new Observer<User>){
//        void onChanged(User user){
//
//        }
//    }

    private static class Lazy {
        static LiveDataBus sLiveDataBus = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return Lazy.sLiveDataBus;
    }

    private ConcurrentHashMap<String, StickyLiveData> mHashMap = new ConcurrentHashMap<>();

    public StickyLiveData with(String eventName) {
        StickyLiveData liveData = mHashMap.get(eventName);
        if (liveData == null) {
            liveData = new StickyLiveData(eventName);
            mHashMap.put(eventName, liveData);
        }
        return liveData;
    }

    /**
     * 实际上liveData黏性事件总线的实现方式 还有另外一套实现方式。
     * 一堆反射 获取LiveData的mVersion字段，来控制数据的分发与否，不够优雅。
     * <p>
     * 但实际上 是不需要那么干的。请看我们下面的实现方式。
     *
     * @param <T>
     */
    public class StickyLiveData<T> extends LiveData<T> {

        private String mEventName;

        private T mStickyData;

        private int mVersion = 0;

        public StickyLiveData(String eventName) {

            mEventName = eventName;
        }

        @Override
        public void setValue(T value) {
            mVersion++;
            super.setValue(value);
        }

        @Override
        public void postValue(T value) {
            mVersion++;
            super.postValue(value);
        }

        public void setStickyData(T stickyData) {
            this.mStickyData = stickyData;
            setValue(stickyData);
        }

        public void postStickyData(T stickyData) {
            this.mStickyData = stickyData;
            postValue(stickyData);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            observerSticky(owner, observer, false);
        }

        public void observerSticky(LifecycleOwner owner, Observer<? super T> observer, boolean sticky) {
            super.observe(owner, new WrapperObserver(this, observer, sticky));
            owner.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        mHashMap.remove(mEventName);
                    }
                }
            });
        }


        private class WrapperObserver<T> implements Observer<T> {
            private StickyLiveData<T> mLiveData;
            private Observer<T> mObserver;
            private boolean mSticky;

            //标记该liveData已经发射几次数据了，用以过滤老数据重复接收
            private int mLastVersion = 0;

            public WrapperObserver(StickyLiveData liveData, Observer<T> observer, boolean sticky) {


                mLiveData = liveData;
                mObserver = observer;
                mSticky = sticky;

                //比如先使用StickyLiveData发送了一条数据。StickyLiveData#version=1
                //那当我们创建WrapperObserver注册进去的时候，就至少需要把它的version和 StickyLiveData的version保持一致
                //用以过滤老数据，否则 岂不是会收到老的数据？
                mLastVersion = mLiveData.mVersion;
            }

            @Override
            public void onChanged(T t) {
                //如果当前observer收到数据的次数已经大于等于了StickyLiveData发送数据的个数了则return

                /**
                 * observer.mLastVersion >= mLiveData.mVersion
                 * 这种情况 只会出现在，我们先行创建一个liveData发射了一条数据。此时liveData的mversion=1.
                 *
                 * 而后注册一个observer进去。由于我们代理了传递进来的observer,进而包装成wrapperObserver，此时wrapperObserver的lastVersion 就会跟liveData的mversion 对齐。保持一样。把wrapperObserver注册到liveData中。
                 *
                 * 根据liveData的原理，一旦一个新的observer 注册进去,也是会尝试把数据派发给他的。这就是黏性事件(先发送,后接收)。
                 *
                 * 但此时wrapperObserver的lastVersion 已经和 liveData的version 一样了。由此来控制黏性事件的分发与否
                 */
                if (mLastVersion >= mLiveData.mVersion) {
                    //但如果当前observer它是关心 黏性事件的，则给他。
                    if (mSticky && mLiveData.mStickyData != null) {
                        mObserver.onChanged(mLiveData.mStickyData);
                    }
                    return;
                }

                mLastVersion = mLiveData.mVersion;
                mObserver.onChanged(t);
            }
        }

    }
}
