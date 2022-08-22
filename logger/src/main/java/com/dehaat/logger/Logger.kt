package com.dehaat.logger

import android.content.Context
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.core.configuration.Credentials
import com.datadog.android.log.Logger
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.tracking.ActivityViewTrackingStrategy
import com.dehaat.logger.attributes.provider.AttributeProvider
import com.dehaat.logger.network.DehaatNetworkLoggerInterceptor
import okhttp3.Interceptor

const val TAG = "DataDogLogger"
typealias EventCategoryName = String
typealias EventSubCategoryName = String

object DehaatLogger {

    private val TRACKING_CONSENT = TrackingConsent.GRANTED
    private var dataDogLogger: Logger? = null
    private var attributeProvider: AttributeProvider? = null
    private var iLoggerExceptionHandler: ILoggerExceptionHandler? = null
    private var iLoggerNonInitializedHandler: ILoggerNonInitializedHandler? = null

    fun initialize(
        context: Context,
        debug: Boolean,
        clientToken: String,
        loggerName: String,
        appLevelAttributes: Map<String, String>,
        attributeProvider: AttributeProvider,
        iLoggerExceptionHandler: ILoggerExceptionHandler,
        iLoggerNonInitializedHandler: ILoggerNonInitializedHandler
    ) {
        this.attributeProvider = attributeProvider
        this.iLoggerExceptionHandler = iLoggerExceptionHandler
        this.iLoggerNonInitializedHandler = iLoggerNonInitializedHandler
        val configuration = Configuration.Builder(
            logsEnabled = true,
            tracesEnabled = true,
            crashReportsEnabled = true,
            rumEnabled = false
        ).trackInteractions()
            .trackLongTasks()
            .useViewTrackingStrategy(ActivityViewTrackingStrategy(true))
            .useSite(DatadogSite.EU1)
            .build()

        val envName = if (debug) "debug" else "production"
        val appVariantName = if (debug) "debug" else "release"

        val credentials = Credentials(clientToken, envName, appVariantName, null, null)
        Datadog.initialize(context, credentials, configuration, TRACKING_CONSENT)
        prepareLogger(loggerName, appLevelAttributes)
    }

    internal fun i(logData: LogData, throwable: Throwable? = null) {
        ifDataDogInitialized(funName = "Logger.i") {
            dataDogLogger?.i(
                message = getFormattedMessage(logData),
                throwable = throwable,
                attributes = LoggerUtils.getExtraAttributeForEachLog(attributeProvider)
            )
        }
    }

    internal fun d(logData: LogData, throwable: Throwable? = null) {
        ifDataDogInitialized(funName = "Logger.d") {
            dataDogLogger?.d(
                message = getFormattedMessage(logData),
                throwable = throwable,
                attributes = LoggerUtils.getExtraAttributeForEachLog(attributeProvider)
            )
        }
    }

    internal fun e(logData: LogData, throwable: Throwable? = null) {
        ifDataDogInitialized(funName = "Logger.e") {
            dataDogLogger?.e(
                message = getFormattedMessage(logData),
                throwable = throwable,
                attributes = LoggerUtils.getExtraAttributeForEachLog(attributeProvider)
            )
        }
    }

    private fun prepareLogger(loggerName: String, appLevelAttributes: Map<String, String>) {
        if (Datadog.isInitialized()) {
            dataDogLogger = Logger.Builder()
                .setNetworkInfoEnabled(true)
                .setLogcatLogsEnabled(true)
                .setDatadogLogsEnabled(true)
                .setLoggerName(loggerName)
                .build()
                .apply {
                    appLevelAttributes.forEach {
                        addAttribute(it.key, it.value)
                    }
                }

        }
    }

    private fun getFormattedMessage(
        logData: LogData
    ) = LoggerUtils.getMessage(logData)

    fun provideLoggerInterceptor(): Interceptor = DehaatNetworkLoggerInterceptor()
    fun provideCatchExceptionHandler() = iLoggerExceptionHandler
    fun provideLoggerNotInitializedHandler() = iLoggerNonInitializedHandler
}