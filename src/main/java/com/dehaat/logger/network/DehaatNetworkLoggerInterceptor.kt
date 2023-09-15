package com.dehaat.logger.network

import com.dehaat.logger.LoggerUtils
import com.dehaat.logger.loggerTryCatch
import com.dehaat.logger.loggerTryCatchWithReturn
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DehaatNetworkLoggerInterceptor() : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		return loggerTryCatchWithReturn(tryBlock = {
			val response = chain.proceed(request)
			logRequestResponse(request, response)
			response
		}, catchBlock = {
			LoggerUtils.logRequestAndException(request, it)
			throw it
		})
	}

	private fun logRequestResponse(request: Request, response: Response) {
		loggerTryCatch(tryBlock = {
			val map = ApiRequestData(request = request, response = response)
			if (response.isSuccessful) {
				LoggerUtils.logApiSuccess(map = map)
			} else {
				LoggerUtils.logApiFailure(map = map)
			}
		})
	}
}
