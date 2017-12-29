package com.qingxin.medical.retrofit

import android.content.Context
import com.vlee78.android.vl.VLModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author zhikuo retrofit model
 */
class RetrofitModel : VLModel() {
    private lateinit var BASE_URL: String
    private lateinit var retrofit: Retrofit
    private lateinit var fileRetrofit: Retrofit

    override fun onCreate() {
        super.onCreate()
        //TODO
        init(getApplication(), "https://www.baidu.com");
    }

    fun init(context: Context, url: String) {
        BASE_URL = url

        val builder = getRetrofitBuilder()
        builder.baseUrl(BASE_URL)
        //builder.client(OkHttpClientFactory.newHttp(context))
        retrofit = builder.build()

        val fileBuilder = getRetrofitBuilder()
        fileBuilder.baseUrl(BASE_URL)
        //fileBuilder.client(OkHttpClientFactory.newFile(context))
        fileRetrofit = fileBuilder.build()
    }

    private fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create())
    }

    fun <T> getService(service: Class<T>): T = retrofit.create(service)

    fun <T> getFileUploadService(service: Class<T>): T = fileRetrofit.create(service)

}