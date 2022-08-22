package com.dehaat.logger.events

object EventCategory {

    //"logApiRequestIntercept" This is main category
    object LogApiRequestIntercept : BaseEventCategory("logApiRequestIntercept") {

        //Sub category or event name.
        object EventType {
            const val ERROR_RESPONSE_TO_STRING = "errorResponseToString"
            const val Success = "success"
            const val Failure = "failure"
            const val RequestException = "apiRequestException"
        }

        //Data Keys
        object Info {
            const val RequestedTime = "requestedTime"
            const val ResponseTime = "responseTime"
            const val RequestResponseInterval = "requestResponseInterval"
            const val ResponseMessage = "responseMessage"
            const val IsPriorResponseAvailable = "isPriorResponseAvailable"
            const val PriorResponseCode = "priorResponseCode"
            const val IsRedirected = "isRedirected"
            const val IsCachedResponseAvailable = "isCachedResponseAvailable"
            const val MethodAndUrl = "methodAndUrl"
            const val IdentifyRequest = "identifyRequest"
            const val RequestMethod = "requestMethod"
            const val CODE = "code"
            const val URL = "url"
            const val EXCEPTION_MESSAGE = "exceptionMessage"
            const val EXCEPTION_CAUSE = "exceptionCause"
        }
    }

    object LogAuthenticatorIntercept : BaseEventCategory("logAuthenticatorIntercept") {

        //Sub category or event name.
        object EventType {
            const val UnAuthorisedError = "unAuthorisedError"
            const val KEY_CLOAK_REFRESH_TOKEN_START = "keyCloakRefreshTokenStart"
            const val KEY_CLOAK_REFRESH_TOKEN_SUCCESS = "keyCloakRefreshTokenSuccess"
            const val KEY_CLOAK_REFRESH_TOKEN_FAILURE = "keyCloakRefreshTokenFailure"
        }

        //Data Keys
        object Info {
            const val accessTokenValid = "accessTokenValid"
            const val refreshTokenValid = "refreshTokenValid"
            const val alreadyMadeFollowRequest = "alreadyMadeFollowRequest"
            const val isLocalTokenValid = "isLocalTokenValid"
            const val lastAuthTokenUpdateTimeMillis = "lastAuthTokenUpdateTimeMillis"
            const val authTokenLocalValidityIntervalInSeconds = "authTokenLocalValidityIntervalInSeconds"
            const val RequestedTime = "requestedTime"
            const val ResponseTime = "responseTime"
            const val RequestResponseInterval = "requestResponseInterval"
            const val ResponseMessage = "responseMessage"
            const val IsPriorResponseAvailable = "isPriorResponseAvailable"
            const val PriorResponseCode = "priorResponseCode"
            const val IsRedirected = "isRedirected"
            const val IsCachedResponseAvailable = "isCachedResponseAvailable"
            const val MethodAndUrl = "methodAndUrl"
            const val IdentifyRequest = "identifyRequest"
            const val RequestMethod = "requestMethod"
            const val RefreshTokenProcessId = "refreshTokenProcessId"
            const val CODE = "code"
            const val URL = "url"
            const val EXCEPTION_MESSAGE = "exceptionMessage"
            const val EXCEPTION_CAUSE = "exceptionCause"
        }
    }

    object LogKeyCloak : BaseEventCategory("logKeyClock") {

        //Sub category or event name.
        object EventType {
            const val KEY_CLOAK_REFRESH_TOKEN_FAILURE = "keyCloakRefreshTokenFailure"
            const val KEY_CLOAK_LOGIN_FAILURE = "keyCloakLoginFailure"
            const val KEY_CLOAK_LOGOUT_FAILURE = "keyCloakLogoutFailure"
        }

        //Data Keys
        object Info {
            const val EXCEPTION_MESSAGE = "exceptionMessage"
            const val EXCEPTION_CAUSE = "exceptionCause"
        }
    }
}