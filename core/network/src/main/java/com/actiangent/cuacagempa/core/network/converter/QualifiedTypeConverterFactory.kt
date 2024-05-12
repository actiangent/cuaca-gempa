package com.actiangent.cuacagempa.core.network.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class QualifiedTypeConverterFactory(
    private val jsonConverterFactory: Converter.Factory,
    private val xmlConverterFactory: Converter.Factory,
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        annotations.forEach { annotation ->
            if (annotation is Json) {
                return jsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
            }
            if (annotation is Xml) {
                return xmlConverterFactory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        parameterAnnotations.forEach { annotation ->
            if (annotation is Json) {
                return jsonConverterFactory
                    .requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
            }
            if (annotation is Xml) {
                return xmlConverterFactory
                    .requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
            }
        }
        return null
    }
}

@Retention(AnnotationRetention.RUNTIME)
internal annotation class Json

@Retention(AnnotationRetention.RUNTIME)
internal annotation class Xml

