package com.dehaat.logger.network

import com.dehaat.logger.LoggerUtils
import com.dehaat.logger.events.EventCategory
import okhttp3.Response

class AuthInterceptOnStartData(
	val response: Response,
	accessTokenValid: Boolean,
	refreshTokenValid: Boolean,
	alreadyMadeFollowRequest: Boolean,
	isLocalTokenValid: Boolean,
	lastAuthTokenUpdateTimeMillis: Long,
	authTokenLocalValidityIntervalInSeconds: Long,
) : LinkedHashMap<String, String>() {
	init {

		val map = LinkedHashMap<String, String>()

		val request = response.request
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

		map[EventCategory.LogAuthenticatorIntercept.Info.CODE] = responseCode.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.RequestMethod] = requestMethod
		map[EventCategory.LogAuthenticatorIntercept.Info.URL] = url

		map[EventCategory.LogAuthenticatorIntercept.Info.accessTokenValid] =
			accessTokenValid.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.refreshTokenValid] =
			refreshTokenValid.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.alreadyMadeFollowRequest] =
			alreadyMadeFollowRequest.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.isLocalTokenValid] =
			isLocalTokenValid.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.lastAuthTokenUpdateTimeMillis] =
			lastAuthTokenUpdateTimeMillis.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.authTokenLocalValidityIntervalInSeconds] =
			authTokenLocalValidityIntervalInSeconds.toString()

		map[EventCategory.LogAuthenticatorIntercept.Info.RequestedTime] = requestedTime.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.ResponseTime] = responseTime.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.RequestResponseInterval] =
			requestResponseInterval.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.ResponseMessage] = message

		LoggerUtils.getResponseFailureBody(response, map)?.let { failureResponseBody ->
			map[EventCategory.LogAuthenticatorIntercept.Info.Body] = failureResponseBody
		}

		map[EventCategory.LogAuthenticatorIntercept.Info.MethodAndUrl] = methodAndUrl
		map[EventCategory.LogAuthenticatorIntercept.Info.IsRedirected] = redirected.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.IsPriorResponseAvailable] =
			priorResponseAvailable.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.PriorResponseCode] =
			priorResponseCode.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.IsCachedResponseAvailable] =
			cachedResponse.toString()
		map[EventCategory.LogAuthenticatorIntercept.Info.IdentifyRequest] = identifyRequest

		map.putAll(requestHeaders.appendKeyPrefix("header_request_"))
		map.putAll(responseHeaders.appendKeyPrefix("header_response_"))

		putAll(map)

	}
}