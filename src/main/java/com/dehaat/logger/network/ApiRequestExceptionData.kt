package com.dehaat.logger.network

import com.dehaat.logger.events.EventCategory
import okhttp3.Request

class ApiRequestExceptionData(
    val request: Request,
    val exception: Exception,
) : LinkedHashMap<String, String>() {
    init {

        val map = LinkedHashMap<String, String>()

        val url = request.url.toString()
        val requestMethod = request.method
        val requestHeaders = request.headers
        val identifyRequest = identifyRequest(request)
        val methodAndUrl = "$requestMethod•$url"

        map[EventCategory.LogApiRequestIntercept.Info.RequestMethod] = requestMethod
        map[EventCategory.LogApiRequestIntercept.Info.URL] = url

        map[EventCategory.LogApiRequestIntercept.Info.EXCEPTION_MESSAGE] =
            exception.message ?: "Request Exception"
        map[EventCategory.LogApiRequestIntercept.Info.EXCEPTION_CAUSE] = "${exception.cause}"

        map[EventCategory.LogApiRequestIntercept.Info.MethodAndUrl] = methodAndUrl
        map[EventCategory.LogApiRequestIntercept.Info.IdentifyRequest] = identifyRequest
        map.putAll(requestHeaders.appendKeyPrefix("header_request_"))
        putAll(map)

    }

}