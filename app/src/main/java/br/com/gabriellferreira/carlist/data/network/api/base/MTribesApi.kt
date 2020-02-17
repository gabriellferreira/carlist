package br.com.gabriellferreira.carlist.data.network.api.base

import br.com.gabriellferreira.carlist.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MTribesApi @Inject constructor() {

    companion object {
        private const val API_TIMEOUT = 30
        const val PARAM_CONTENT_TYPE = "Content-Type"
        const val VALUE_APPLICATION_JSON = "application/json"
    }

    fun build(timeout: Int = API_TIMEOUT): Retrofit {
        val baseUrl = BuildConfig.M_TRIBES_BASE_URL

        val builder = OkHttpClient.Builder()
            .addInterceptor(GeneralInterceptor())

        val clientBuilder = builder
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)

        val client = clientBuilder.build()
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))

        return retrofitBuilder.build()
    }
}

class GeneralInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
            .header(MTribesApi.PARAM_CONTENT_TYPE, MTribesApi.VALUE_APPLICATION_JSON)
            .method(original.method(), original.body())

        val request = builder.build()
        return chain.proceed(request)
    }
}