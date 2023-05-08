package com.dehaat.logger.events

object EventCategory {

    //"logApiRequestIntercept" This is main category
    object LogApiRequestIntercept : BaseEventCategory("LogApiRequestIntercept") {

        //Sub category or event name.
        object EventType {
            const val Success = "Success"
            const val Failure = "Failure"
            const val ERROR_RESPONSE_TO_STRING = "ErrorResponseToString"
            const val RequestException = "ApiRequestException"
        }

        //Data Keys
        object Info {
            const val RequestedTime = CommonInfoKeys.RequestedTime
            const val ResponseTime = CommonInfoKeys.ResponseTime
            const val RequestResponseInterval = CommonInfoKeys.RequestResponseInterval
            const val ResponseMessage = CommonInfoKeys.ResponseMessage
            const val Body = CommonInfoKeys.Body
            const val IsPriorResponseAvailable = CommonInfoKeys.IsPriorResponseAvailable
            const val PriorResponseCode = CommonInfoKeys.PriorResponseCode
            const val IsRedirected = CommonInfoKeys.IsRedirected
            const val IsCachedResponseAvailable = CommonInfoKeys.IsCachedResponseAvailable
            const val MethodAndUrl = CommonInfoKeys.MethodAndUrl
            const val IdentifyRequest = CommonInfoKeys.IdentifyRequest
            const val RequestMethod = CommonInfoKeys.RequestMethod
            const val CODE = CommonInfoKeys.CODE
            const val URL = CommonInfoKeys.URL
            const val EXCEPTION_MESSAGE = CommonInfoKeys.EXCEPTION_MESSAGE
            const val EXCEPTION_CAUSE = CommonInfoKeys.EXCEPTION_CAUSE
        }
    }

    object LogAuthenticatorIntercept : BaseEventCategory("LogAuthenticatorIntercept") {

        //Sub category or event name.
        object EventType {
            const val UnAuthorisedError = "UnAuthorisedError"
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
            const val RefreshTokenProcessId = "refreshTokenProcessId"

            const val RequestedTime = CommonInfoKeys.RequestedTime
            const val ResponseTime = CommonInfoKeys.ResponseTime
            const val RequestResponseInterval = CommonInfoKeys.RequestResponseInterval
            const val ResponseMessage = CommonInfoKeys.ResponseMessage
            const val Body = CommonInfoKeys.Body
            const val IsPriorResponseAvailable = CommonInfoKeys.IsPriorResponseAvailable
            const val PriorResponseCode = CommonInfoKeys.PriorResponseCode
            const val IsRedirected = CommonInfoKeys.IsRedirected
            const val IsCachedResponseAvailable = CommonInfoKeys.IsCachedResponseAvailable
            const val MethodAndUrl = CommonInfoKeys.MethodAndUrl
            const val IdentifyRequest = CommonInfoKeys.IdentifyRequest
            const val RequestMethod = CommonInfoKeys.RequestMethod
            const val CODE = CommonInfoKeys.CODE
            const val URL = CommonInfoKeys.URL
            const val EXCEPTION_MESSAGE = CommonInfoKeys.EXCEPTION_MESSAGE
            const val EXCEPTION_CAUSE = CommonInfoKeys.EXCEPTION_CAUSE
        }
    }

    object LogKeyCloak : BaseEventCategory("LogKeyClock") {

        //Sub category or event name.
        object EventType {
            const val KEY_CLOAK_REFRESH_TOKEN_FAILURE = "RefreshTokenFailed"
            const val KEY_CLOAK_LOGIN_FAILURE = "KeycloakLoginFailed"
            const val KEY_CLOAK_LOGOUT_FAILURE = "KeycloakLogoutFailed"
        }

        //Data Keys
        object Info {
            const val EXCEPTION_MESSAGE = CommonInfoKeys.EXCEPTION_MESSAGE
            const val EXCEPTION_CAUSE = CommonInfoKeys.EXCEPTION_CAUSE
        }
    }
}

object CommonInfoKeys{
    const val RequestedTime = "requestedTime"
    const val ResponseTime = "responseTime"
    const val Body = "body"
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