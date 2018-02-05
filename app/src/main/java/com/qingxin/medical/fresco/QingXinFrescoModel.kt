package com.qingxin.medical.fresco

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.internal.Supplier
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.MemoryTrimmable
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.vlee78.android.vl.VLModel
import com.vlee78.android.vl.VLUtils
import okhttp3.OkHttpClient
import java.util.*

/**
 * Network fetcher that uses OkHttp 3 as a backend.
 * Date 2017-12-29
 * @author zhikuo
 */
class QingXinFrescoModel : VLModel() {

    override fun onCreate() {
        super.onCreate()
        initFresco(getApplication())
    }

    private val MAX_DISK_CACHE_SIZE = 80 * ByteConstants.MB
    private val MAX_DISK_ON_LOW_DISK_SPACE_CACHE_SIZE = 50 * ByteConstants.MB
    private val MAX_DISK_ON_VERY_LOW_DISK_SPACE_CACHE_SIZE = 10 * ByteConstants.MB

    fun initFresco(context: Context) {
        val mOkHttpClient = OkHttpClient()
        val cacheDir = VLUtils.getCacheDir(context)
        val mSmallDCC = DiskCacheConfig
                .newBuilder(context)
                .setBaseDirectoryPath(cacheDir)
                .setBaseDirectoryName("s_image_cache")
                .build()
        val mDCC = DiskCacheConfig
                .newBuilder(context)
                .setBaseDirectoryPath(cacheDir)
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE.toLong())
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_ON_LOW_DISK_SPACE_CACHE_SIZE.toLong())
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_ON_VERY_LOW_DISK_SPACE_CACHE_SIZE.toLong())
                .build()
        val memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance()
        memoryTrimmableRegistry.registerMemoryTrimmable(FrescoMemoryTrimmable())
        val imagePipelineConfig = ImagePipelineConfig.newBuilder(context).setNetworkFetcher(OkHttpNetworkFetcher(mOkHttpClient))
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setMainDiskCacheConfig(mDCC)
                .setSmallImageDiskCacheConfig(mSmallDCC)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setBitmapMemoryCacheParamsSupplier(FrescoSupplier(context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager))
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setRequestListeners(HashSet<RequestListener>())
                .build()
        Fresco.initialize(context, imagePipelineConfig)
    }

    private class FrescoMemoryTrimmable : MemoryTrimmable {

        override fun trim(trimType: MemoryTrimType) {
            val suggestedTrimRatio = trimType.suggestedTrimRatio
            if (MemoryTrimType.OnCloseToDalvikHeapLimit.suggestedTrimRatio == suggestedTrimRatio
                    || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.suggestedTrimRatio == suggestedTrimRatio
                    || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.suggestedTrimRatio == suggestedTrimRatio) {
                Fresco.getImagePipeline().clearMemoryCaches()
            }
        }
    }

    private class FrescoSupplier internal constructor(private val activityManager: ActivityManager) : Supplier<MemoryCacheParams> {

        private val maxCacheSize: Int
            get() {
                val min = Math.min(activityManager.memoryClass * ByteConstants.MB, Integer.MAX_VALUE)
                if (min < 32 * ByteConstants.MB) {
                    return 4 * ByteConstants.MB
                }
                return if (min < 64 * ByteConstants.MB) {
                    6 * ByteConstants.MB
                } else min / 6
            }

        override fun get(): MemoryCacheParams {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MemoryCacheParams(maxCacheSize, 50, 20 * ByteConstants.MB, 200, ByteConstants.MB)
            } else MemoryCacheParams(maxCacheSize, 256, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE)
        }
    }
}