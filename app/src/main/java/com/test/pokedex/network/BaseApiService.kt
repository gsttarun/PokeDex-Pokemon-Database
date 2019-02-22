package com.test.pokedex.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BaseApiService {
    companion object {
        inline fun <reified T> create(endPoint: String): T {

            val httpLoggingInterceptor =
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                        breakAndLog(message)
//                        LogUtil.info("APIService", message)
                    }).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }

            val headerInterceptor = Interceptor {
                val request = it.request()?.let { request ->
                    request.newBuilder()?.apply {
                        /*if (endPoint.contentEquals(BuildConfig.AUTH_ENDPOINT)) {
                            header(APIConstants.X_REQUESTED_WITH, APIConstants.XML_HTTP_REQUEST)
                            header(APIConstants.APPID, APIConstants.APP_ID_DEFAULT_VALUE)
                        }*/
                    }
                }?.build()

                return@Interceptor it.proceed(request)
            }


            val okHttpClient = OkHttpClient.Builder().apply {
                connectTimeout(60, TimeUnit.SECONDS)
                addInterceptor(headerInterceptor)
                addInterceptor(httpLoggingInterceptor)
            }.build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(endPoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()


            return retrofit.create(T::class.java)
        }

        fun breakAndLog(message: String) {
            val maxLogSize = 3000
            for (i in 0..message.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > message.length) message.length else end
                Timber.i(message.substring(start, end))
            }
        }


    }
}