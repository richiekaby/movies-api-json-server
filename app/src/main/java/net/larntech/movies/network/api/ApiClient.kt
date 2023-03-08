package net.larntech.movies.network.api

import com.pesapal.paygateway.activities.payment.data.services.ApiServices
import net.larntech.movies.config.BaseUrl.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

        private val retrofit: Retrofit
            get() {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build() }

        val apiServices: ApiServices get() = retrofit.create(ApiServices::class.java)

        private fun getOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .build()
                        chain.proceed(newRequest)
                }
            return builder.build()
        }

}