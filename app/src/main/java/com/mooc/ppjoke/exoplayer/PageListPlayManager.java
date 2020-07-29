package com.mooc.ppjoke.exoplayer;

import android.app.Application;
import android.net.Uri;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSinkFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.mooc.libcommon.global.AppGlobals;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 能适应多个页面视频播放的 播放器管理者
 * 每个页面一个播放器
 * 方便管理每个页面的暂停/恢复操作
 */
public class PageListPlayManager {
    private static HashMap<String, PageListPlay> sPageListPlayHashMap = new HashMap<>();
    private static final ProgressiveMediaSource.Factory mediaSourceFactory;

    static {
        Application application = AppGlobals.getApplication();
        //创建http视频资源如何加载的工厂对象
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(application, application.getPackageName()));
        //创建缓存，指定缓存位置，和缓存策略,为最近最少使用原则,最大为200m
        Cache cache = new SimpleCache(application.getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024 * 1024 * 200));
        //把缓存对象cache和负责缓存数据读取、写入的工厂类CacheDataSinkFactory 相关联
        CacheDataSinkFactory cacheDataSinkFactory = new CacheDataSinkFactory(cache, Long.MAX_VALUE);

        /**创建能够 边播放边缓存的 本地资源加载和http网络数据写入的工厂类
         * public CacheDataSourceFactory(
         *       Cache cache, 缓存写入策略和缓存写入位置的对象
         *       DataSource.Factory upstreamFactory,http视频资源如何加载的工厂对象
         *       DataSource.Factory cacheReadDataSourceFactory,本地缓存数据如何读取的工厂对象
         *       @Nullable DataSink.Factory cacheWriteDataSinkFactory,http网络数据如何写入本地缓存的工厂对象
         *       @CacheDataSource.Flags int flags,加载本地缓存数据进行播放时的策略,如果遇到该文件正在被写入数据,或读取缓存数据发生错误时的策略
         *       @Nullable CacheDataSource.EventListener eventListener  缓存数据读取的回调
         */
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache,
                dataSourceFactory,
                new FileDataSourceFactory(),
                cacheDataSinkFactory,
                CacheDataSource.FLAG_BLOCK_ON_CACHE,
                null);

        //最后 还需要创建一个 MediaSource 媒体资源 加载的工厂类
        //因为由它创建的MediaSource 能够实现边缓冲边播放的效果,
        //如果需要播放hls,m3u8 则需要创建DashMediaSource.Factory()
        mediaSourceFactory = new ProgressiveMediaSource.Factory(cacheDataSourceFactory);

    }

    public static MediaSource createMediaSource(String url) {
        return mediaSourceFactory.createMediaSource(Uri.parse(url));
    }

    public static PageListPlay get(String pageName) {
        PageListPlay pageListPlay = sPageListPlayHashMap.get(pageName);
        if (pageListPlay == null) {
            pageListPlay = new PageListPlay();
            sPageListPlayHashMap.put(pageName, pageListPlay);
        }
        return pageListPlay;
    }

    public static void release(String pageName) {
        PageListPlay pageListPlay = sPageListPlayHashMap.remove(pageName);
        if (pageListPlay != null) {
            pageListPlay.release();
        }
    }
}
