package com.dehaat.logger

import android.util.Log
import com.datadog.android.Datadog
import com.dehaat.logger.attributes.provider.AttributeProvider
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

    fun getExtraAttributeForEachLog(attributeProvider: AttributeProvider?) =
        mutableMapOf<String, Any?>().apply {
            put(LoggerArgumentKey.Log.LOCAL_LOGGING_TIME, System.currentTimeMillis())
            attributeProvider?.let {
                this.putAll(it.provideDeviceInfo())
                this.putAll(it.provideUserInfo())
                this.putAll(it.provideConnectionInfo())
            }
        }

    fun logRequestAndException(request: Request, exception: Exception) {
        tryCatch(tryBlock = {
            val map = ApiRequestExceptionData(request = request, exception = exception)
            val logData = LogData(
                eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
                eventType = EventCategory.LogApiRequestIntercept.EventType.RequestException,
                eventMessage = "API call exception",
                map = map
            )
            DehaatLogger.e(logData, exception)
        })
    }

    fun logApiSuccess(map: Map<String, String>) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
                eventType = EventCategory.LogApiRequestIntercept.EventType.Success,
                eventMessage = "API call success",
                map = map
            )
            DehaatLogger.i(logData)
        })
    }

    fun logApiFailure(map: ApiRequestData) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
                eventType = EventCategory.LogApiRequestIntercept.EventType.Failure,
                eventMessage = "API call failure",
                map = map
            )
            DehaatLogger.e(logData)
        })
    }

    fun logAPIFailureResponseToString(map: Map<String, String>, th: Throwable) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogApiRequestIntercept.name,
                eventType = EventCategory.LogApiRequestIntercept.EventType.ERROR_RESPONSE_TO_STRING,
                eventMessage = "Error while converting response body to string",
                map = map,
                throwable = th
            )
            DehaatLogger.e(logData, th)
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
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
                eventType = EventCategory.LogAuthenticatorIntercept.EventType.UnAuthorisedError,
                eventMessage = "Authentication process started",
                map = AuthInterceptOnStartData(
                    response,
                    accessTokenValid,
                    refreshTokenValid,
                    alreadyMadeFollowRequest,
                    isLocalTokenValid,
                    lastAuthTokenUpdateTimeMillis,
                    authTokenLocalValidityIntervalInSeconds
                )
            )
            DehaatLogger.i(logData)
        })
    }

    fun logAuthenticatorRefreshTokenStarted(refreshTokenProcessId: String, response: Response) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
                eventType = EventCategory.LogAuthenticatorIntercept.EventType.KEY_CLOAK_REFRESH_TOKEN_START,
                eventMessage = "Authenticator Refresh Token process started",
                map = AuthKeyCloakRefreshTokenStartData(
                    refreshTokenProcessId,
                    response,
                )
            )
            DehaatLogger.i(logData)
        })
    }

    fun logAuthenticatorRefreshTokenSuccess(refreshTokenProcessId: String, response: Response) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
                eventType = EventCategory.LogAuthenticatorIntercept.EventType.KEY_CLOAK_REFRESH_TOKEN_SUCCESS,
                eventMessage = "Authenticator Refresh Token process success",
                map = AuthKeyCloakRefreshTokenStartData(
                    refreshTokenProcessId,
                    response,
                )
            )
            DehaatLogger.i(logData)
        })
    }

    fun logAuthenticatorRefreshTokenFailure(
        refreshTokenProcessId: String,
        response: Response,
        ex: Exception?
    ) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogAuthenticatorIntercept.name,
                eventType = EventCategory.LogAuthenticatorIntercept.EventType.KEY_CLOAK_REFRESH_TOKEN_FAILURE,
                eventMessage = "Authenticator Refresh Token process failed",
                map = AuthKeyCloakRefreshTokenFailureData(
                    refreshTokenProcessId,
                    response,
                    ex
                )
            )
            DehaatLogger.e(logData, ex)
        })
    }

    fun logKeyCloakLoginFailure(exception: Exception?, extraInfo: Map<String, String>?) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogKeyCloak.name,
                eventType = EventCategory.LogKeyCloak.EventType.KEY_CLOAK_LOGIN_FAILURE,
                eventMessage = "KeyCloak(AuthUtils) login failed",
                map = KeyCloakProcessFailureData(exception, extraInfo)
            )
            DehaatLogger.e(logData, exception)
        })
    }

    fun logKeyCloakLogoutFailure(exception: Exception?, extraInfo: Map<String, String>?) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogKeyCloak.name,
                eventType = EventCategory.LogKeyCloak.EventType.KEY_CLOAK_LOGOUT_FAILURE,
                eventMessage = "KeyCloak(AuthUtils) logout failed",
                map = KeyCloakProcessFailureData(exception, extraInfo)
            )
            DehaatLogger.e(logData, exception)
        })
    }

    fun logKeyCloakRefreshTokenFailure(exception: Exception?, extraInfo: Map<String, String>?) {
        tryCatch(tryBlock = {
            val logData = LogData(
                eventTypeCategory = EventCategory.LogKeyCloak.name,
                eventType = EventCategory.LogKeyCloak.EventType.KEY_CLOAK_REFRESH_TOKEN_FAILURE,
                eventMessage = "KeyCloak(AuthUtils) refresh token failed",
                map = KeyCloakProcessFailureData(exception, extraInfo)
            )
            DehaatLogger.e(logData, exception)
        })
    }
}

inline fun ifDataDogInitialized(
    funName: String,
    nonInitializedBlock: () -> Unit = {
        Log.d(
            TAG,
            "Data dog not initialised"
        )
    }, block: () -> Unit
) {
    if (Datadog.isInitialized()) {
        block()
    } else {
        nonInitializedBlock()
        DehaatLogger.provideLoggerNotInitializedHandler()
            ?.notInitialize(mapOf("functionName" to funName))
    }
}

inline fun <R> tryCatchWithReturn(tryBlock: () -> R, catchBlock: (ex: Exception) -> R) = try {
    tryBlock()
} catch (ex: Exception) {
    ex.printStackTrace()
    useClientExceptionHandler(ex)
    catchBlock(ex)
}

fun useClientExceptionHandler(ex: Exception) {
    try {
        DehaatLogger.provideCatchExceptionHandler()?.onExceptionCatch(ex)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

inline fun tryCatch(tryBlock: () -> Unit = {}, catchBlock: (ex: Exception) -> Unit = {}) = try {
    tryBlock()
} catch (ex: Exception) {
    ex.printStackTrace()
    catchBlock(ex)
    useClientExceptionHandler(ex)
}