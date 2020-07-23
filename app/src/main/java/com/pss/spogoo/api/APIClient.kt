package com.pss.spogoo.api

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

/**
 * Created by suchi  on 15/07/2019.
 */
class APIClient {

    companion object {
        private const val BASE_URL = "https://spadmin.purplesoftwaresolutions.com/Spogoo_api/"
//        private const val BASE_URL = "https://avc.net.in/spogoo/Spogoo_api/"


        var retofit: Retrofit? = null

        val client: Retrofit
            get() {

                if (retofit == null) {
                    val gson = GsonBuilder()

                    retofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
//                        .addConverterFactory(NullOnEmptyConverterFactory())

                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retofit!!
            }
    }

    internal class NullOnEmptyConverterFactory : Converter.Factory() {
        fun responseBody(
            type: Type?,
            annotations: Array<Annotation?>?,
            retrofit: Retrofit
        ): Any {
            val delegate: Converter<ResponseBody, *> =
                retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return object {
                fun convert(body: ResponseBody): Any? {
                    return if (body.contentLength() == 0L) null else delegate.convert(body)
                }
            }
        }
    }

//    class EmptyArrayConverterFactory : Converter.Factory() {
//
//        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
//            val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
//            return Converter<ResponseBody, Any> { body ->
//                var bodyString = body.string()
//                if (bodyString == "{[]}") { // Or whatever the empty array is returned as
//                    bodyString = //Convert body string to empty/error TransactionModel JSON
//                }
//                delegate.convert(bodyString)
//            }
//        }
//
//    }
}

