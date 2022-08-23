package com.dehaat.logger.network

import com.dehaat.logger.LoggerUtils
import com.dehaat.logger.events.EventCategory
import com.dehaat.logger.tryCatchWithReturn
import okhttp3.Request
import okhttp3.Response

class ApiRequestData(
    val request: Request,
    val response: Response,
) : LinkedHashMap<String, String>() {
    init {

        val map = LinkedHashMap<String, String>()

        val url = request.url.toString()
        val requestMethod = request.method
        val requestHeaders = request.headers
        val identifyRequest = identifyRequest(request)
        val methodAndUrl = "$requestMethod•$url"

        val responseCode = response.code
        val requestedTime = response.sentRequestAtMillis
        val responseTime = response.receivedResponseAtMillis
        val requestResponseInterval = responseTime.minus(requestedTime)
        val cachedResponse = response.cacheResponse != null
        val responseHeaders = response.headers
        val redirected = response.isRedirect
        val priorResponseAvailable = response.priorResponse != null
        val priorResponseCode = response.priorResponse?.code
        val message = response.message

        map[EventCategory.LogApiRequestIntercept.Info.CODE] = responseCode.toString()
        map[EventCategory.LogApiRequestIntercept.Info.RequestMethod] = requestMethod
        map[EventCategory.LogApiRequestIntercept.Info.URL] = url
        map[EventCategory.LogApiRequestIntercept.Info.RequestedTime] = requestedTime.toString()
        map[EventCategory.LogApiRequestIntercept.Info.ResponseTime] = responseTime.toString()
        map[EventCategory.LogApiRequestIntercept.Info.RequestResponseInterval] =
            requestResponseInterval.toString()
        map[EventCategory.LogApiRequestIntercept.Info.ResponseMessage] = message
        val failureResponseBody = if (response.code !in 200..399) {
            tryCatchWithReturn(
                {
                    if (response.body != null) {
                        response.peekBody(Long.MAX_VALUE).string()
                    } else {
                        "null"
                    }
                }, {
                    LoggerUtils.logAPIFailureResponseToString(map = map, th = it)
                    "Exception while converting error response body to string, with message ${it.message}"
                }
            )
        } else {
            null
        }

        failureResponseBody?.let {
            map[EventCategory.LogApiRequestIntercept.Info.Body] = failureResponseBody
        }

        map[EventCategory.LogApiRequestIntercept.Info.MethodAndUrl] = methodAndUrl
        map[EventCategory.LogApiRequestIntercept.Info.IsRedirected] = redirected.toString()
        map[EventCategory.LogApiRequestIntercept.Info.IsPriorResponseAvailable] =
            priorResponseAvailable.toString()
        map[EventCategory.LogApiRequestIntercept.Info.PriorResponseCode] =
            priorResponseCode.toString()
        map[EventCategory.LogApiRequestIntercept.Info.IsCachedResponseAvailable] =
            cachedResponse.toString()
        map[EventCategory.LogApiRequestIntercept.Info.IdentifyRequest] = identifyRequest

        map.putAll(requestHeaders.appendKeyPrefix("header_request_"))
        map.putAll(responseHeaders.appendKeyPrefix("header_response_"))

        putAll(map)

    }
}