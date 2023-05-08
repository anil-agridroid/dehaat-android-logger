package com.dehaat.logger

import com.dehaat.logger.events.EventCategory
import com.dehaat.logger.network.*
import okhttp3.Request
import okhttp3.Response

object LoggerUtils {

	fun getMessage(logData: LogData): String {
		val sb = StringBuilder()
		if (logData.isNotEmpty()) {
			logData.forEach { item ->
				sb.append("${item.key} = ${item.value}, ")
			}
		}
		return sb.toString()
	}

	fun logRequestAndException(request: Request, exception: Exception) {
		logIt(loggingType = LoggingType.ERROR, throwable = exception, logDataProvider = {
			val map = ApiRequestExceptionData(request = request, exception = exception)
			LogData(
				eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
				eventType = EventCategory.LogApiRequestIntercept.EventType.RequestException,
				eventMessage = "API call exception",
				mapForMessage = map
			)
		})
	}

	fun logApiSuccess(map: Map<String, String>) {
		logIt(loggingType = LoggingType.INFO, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
				eventType = EventCategory.LogApiRequestIntercept.EventType.Success,
				eventMessage = "API call success",
				mapForMessage = map
			)
		})
	}

	fun logApiFailure(map: ApiRequestData) {
		logIt(loggingType = LoggingType.ERROR, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
				eventType = EventCategory.LogApiRequestIntercept.EventType.Failure,
				eventMessage = "API call failure",
				mapForMessage = map
			)
		})
	}

	fun logAPIFailureResponseToString(map: Map<String, String>, th: Throwable) {
		logIt(loggingType = LoggingType.ERROR, throwable = th, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
				eventType = EventCategory.LogApiRequestIntercept.EventType.ERROR_RESPONSE_TO_STRING,
				eventMessage = "Error while converting response body to string",
				mapForMessage = map,
				throwable = th
			)
		})
	}

	fun logAuthenticatorUnAuthorisedError(
		response: Response,
		accessTokenValid: Boolean,
		refreshTokenValid: Boolean,
		alreadyMadeFollowRequest: Boolean,
		isLocalTokenValid: Boolean,
		lastAuthTokenUpdateTimeMillis: Long,
		authTokenLocalValidityIntervalInSeconds: Long,
	) {
		logIt(loggingType = LoggingType.INFO, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
				eventType = EventCategory.LogAuthenticatorIntercept.EventType.UnAuthorisedError,
				eventMessage = "Authentication process started",
				mapForMessage = AuthInterceptOnStartData(
					response,
					accessTokenValid,
					refreshTokenValid,
					alreadyMadeFollowRequest,
					isLocalTokenValid,
					lastAuthTokenUpdateTimeMillis,
					authTokenLocalValidityIntervalInSeconds
				)
			)
		})
	}

	fun logAuthenticatorRefreshTokenStarted(refreshTokenProcessId: String, response: Response) {
		logIt(loggingType = LoggingType.INFO, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
				eventType = EventCategory.LogAuthenticatorIntercept.EventType.KEY_CLOAK_REFRESH_TOKEN_START,
				eventMessage = "Authenticator Refresh Token process started",
				mapForMessage = AuthKeyCloakRefreshTokenStartData(
					refreshTokenProcessId,
					response,
				)
			)
		})

	}

	fun logAuthenticatorRefreshTokenSuccess(refreshTokenProcessId: String, response: Response) {
		logIt(loggingType = LoggingType.INFO, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
				eventType = EventCategory.LogAuthenticatorIntercept.EventType.KEY_CLOAK_REFRESH_TOKEN_SUCCESS,
				eventMessage = "Authenticator Refresh Token process success",
				mapForMessage = AuthKeyCloakRefreshTokenStartData(
					refreshTokenProcessId,
					response,
				)
			)
		})
	}

	fun logAuthenticatorRefreshTokenFailure(
		refreshTokenProcessId: String,
		response: Response,
		ex: Exception?
	) {
		logIt(loggingType = LoggingType.ERROR, throwable = ex, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
				eventType = EventCategory.LogAuthenticatorIntercept.EventType.KEY_CLOAK_REFRESH_TOKEN_FAILURE,
				eventMessage = "Authenticator Refresh Token process failed",
				mapForMessage = AuthKeyCloakRefreshTokenFailureData(
					refreshTokenProcessId,
					response,
					ex
				)
			)
		})

	}

	fun logKeyCloakLoginFailure(exception: Exception?, extraInfo: Map<String, String>?) {
		logIt(loggingType = LoggingType.ERROR, throwable = exception, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogKeyCloak.name,
				eventType = EventCategory.LogKeyCloak.EventType.KEY_CLOAK_LOGIN_FAILURE,
				eventMessage = "KeyCloak(AuthUtils) login failed",
				mapForMessage = KeyCloakProcessFailureData(exception, extraInfo)
			)
		})
	}

	fun logKeyCloakLogoutFailure(exception: Exception?, extraInfo: Map<String, String>?) {
		logIt(loggingType = LoggingType.ERROR, throwable = exception, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogKeyCloak.name,
				eventType = EventCategory.LogKeyCloak.EventType.KEY_CLOAK_LOGOUT_FAILURE,
				eventMessage = "KeyCloak(AuthUtils) logout failed",
				mapForMessage = KeyCloakProcessFailureData(exception, extraInfo)
			)
		})
	}

	fun logKeyCloakRefreshTokenFailure(exception: Exception?, extraInfo: Map<String, String>?) {
		logIt(loggingType = LoggingType.ERROR, throwable = exception, logDataProvider = {
			LogData(
				eventTypeCategory = EventCategory.LogKeyCloak.name,
				eventType = EventCategory.LogKeyCloak.EventType.KEY_CLOAK_REFRESH_TOKEN_FAILURE,
				eventMessage = "KeyCloak(AuthUtils) refresh token failed",
				mapForMessage = KeyCloakProcessFailureData(exception, extraInfo)
			)
		})
	}

	fun getResponseFailureBody(response: Response, map: LinkedHashMap<String, String>): String? {
		val failureResponseBody = if (response.code !in 200..399) {
			try {
				if (response.body != null) {
					response.peekBody(Long.MAX_VALUE).string()
				} else {
					"null"
				}
			} catch (ex: Exception) {
				logAPIFailureResponseToString(map = map, th = ex)
				"Exception while converting error response body to string, with message ${ex.message}"
			}
		} else {
			null
		}
		return failureResponseBody
	}

	fun logIt(
		loggingType: LoggingType,
		throwable: Throwable? = null,
		logDataProvider: () -> LogData
	) {
		loggerTryCatchWithClientExceptionHandler(tryBlock = {
			val dataToLog = logDataProvider()
			with(DehaatLogger) {
				when (loggingType) {
					LoggingType.INFO -> i(dataToLog, throwable)
					LoggingType.ERROR -> e(dataToLog, throwable)
					LoggingType.DEBUG -> d(dataToLog, throwable)
					LoggingType.VERBOSE -> v(dataToLog, throwable)
				}
			}
		})
	}
}

sealed class LoggingType {
	object INFO : LoggingType()
	object ERROR : LoggingType()
	object DEBUG : LoggingType()
	object VERBOSE : LoggingType()
}

inline fun loggerTryCatch(tryBlock: () -> Unit = {}, catchBlock: (ex: Exception) -> Unit = {}) {
	try {
		tryBlock()
	} catch (ex: Exception) {
		ex.printStackTrace()
		catchBlock(ex)
	}
}

inline fun <R> loggerTryCatchWithReturn(
	tryBlock: () -> R,
	catchBlock: (ex: Exception) -> R
) = try {
	tryBlock()
} catch (ex: Exception) {
	ex.printStackTrace()
	catchBlock(ex)
}

fun useClientExceptionHandler(ex: DehaatLoggerException) = loggerTryCatch(tryBlock = {
	DehaatLogger.provideCatchExceptionHandler()?.onExceptionCatch(ex)
})

inline fun <R> loggerTryCatchReturnWithClientExceptionHandler(
	tryBlock: () -> R,
	catchBlock: (ex: Exception) -> R
) = try {
	tryBlock()
} catch (ex: Exception) {
	useClientExceptionHandler(DehaatLoggerException(ex))
	catchBlock(ex)
}

private inline fun loggerTryCatchWithClientExceptionHandler(
	tryBlock: () -> Unit,
	catchBlock: (ex: DehaatLoggerException) -> Unit = {
		it.exception.printStackTrace()
	}
) = loggerTryCatch(tryBlock = tryBlock, catchBlock = {
	val ex = DehaatLoggerException(it)
	catchBlock(ex)
	useClientExceptionHandler(ex)
})

data class DehaatLoggerException(val exception: Exception)