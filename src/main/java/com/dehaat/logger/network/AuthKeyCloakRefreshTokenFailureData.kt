package com.dehaat.logger.network

import com.dehaat.logger.events.EventCategory
import okhttp3.Response

class AuthKeyCloakRefreshTokenFailureData(
    refreshTokenProcessId: String,
    response: Response,
    ex: Exception?,
) : LinkedHashMap<String, String>() {
    init {

        val map = LinkedHashMap<String, String>()

        val request = response.request
        val url = request.url.toString()
        val requestMethod = request.method
        val identifyRequest = identifyRequest(request)
        val methodAndUrl = "$requestMethod•$url"

        map[EventCategory.LogAuthenticatorIntercept.Info.RefreshTokenProcessId] =
            refreshTokenProcessId
        if (ex != null) {
            map[EventCategory.LogAuthenticatorIntercept.Info.EXCEPTION_MESSAGE] = ex.message ?: "--"
            map[EventCategory.LogAuthenticatorIntercept.Info.EXCEPTION_CAUSE] =
                ex.cause?.message ?: "--"
        }
        map[EventCategory.LogAuthenticatorIntercept.Info.RequestMethod] = requestMethod
        map[EventCategory.LogAuthenticatorIntercept.Info.URL] = url
        map[EventCategory.LogAuthenticatorIntercept.Info.MethodAndUrl] = methodAndUrl
        map[EventCategory.LogAuthenticatorIntercept.Info.IdentifyRequest] = identifyRequest

        putAll(map)

    }
}