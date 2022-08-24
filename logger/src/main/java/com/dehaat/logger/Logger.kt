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
	private var iLoggerExceptionHandler: ILoggerExceptionHandler? = null
	private var iLoggerNonInitializedHandler: ILoggerNonInitializedHandler? = null
	private var attributeProvider: AttributeProvider? = null

	fun initialize(
		context: Context,
		iDehaatLogger: IDehaatLogger
	) {
		this.attributeProvider = iDehaatLogger.providerEachLogAttributes()
		this.iLoggerExceptionHandler = iDehaatLogger
		this.iLoggerNonInitializedHandler = iDehaatLogger

		val config = iDehaatLogger.providerConfig()
		val credential = iDehaatLogger.providerCredential()

		val configuration = Configuration.Builder(
			logsEnabled = config.logsEnabled,
			tracesEnabled = config.tracesEnabled,
			crashReportsEnabled = config.crashReportsEnabled,
			rumEnabled = config.rumEnabled
		).trackInteractions()
			.trackLongTasks()
			.useViewTrackingStrategy(ActivityViewTrackingStrategy(true))
			.useSite(DatadogSite.EU1)
			.build()

		val credentials = Credentials(
			credential.clientToken,
			credential.envName,
			credential.variant,
			credential.rumApplicationId,
			credential.serviceName
		)
		Datadog.initialize(context, credentials, configuration, TRACKING_CONSENT)
		prepareLogger(iDehaatLogger)
	}

	fun provideLoggerInterceptor(): Interceptor = DehaatNetworkLoggerInterceptor()
	fun provideCatchExceptionHandler() = iLoggerExceptionHandler
	fun provideLoggerNotInitializedHandler() = iLoggerNonInitializedHandler

	internal fun i(logData: LogData, throwable: Throwable? = null) {
		ifDataDogInitialized(funName = "Logger.i") {
			dataDogLogger?.i(
				message = getFormattedMessage(logData),
				throwable = throwable,
				attributes = eventAttributeForEachLog(logData)
			)
		}
	}

	internal fun d(logData: LogData, throwable: Throwable? = null) {
		ifDataDogInitialized(funName = "Logger.d") {
			dataDogLogger?.d(
				message = getFormattedMessage(logData),
				throwable = throwable,
				attributes = eventAttributeForEachLog(logData)
			)
		}
	}

	internal fun v(logData: LogData, throwable: Throwable? = null) {
		ifDataDogInitialized(funName = "Logger.d") {
			dataDogLogger?.v(
				message = getFormattedMessage(logData),
				throwable = throwable,
				attributes = eventAttributeForEachLog(logData)
			)
		}
	}

	internal fun e(logData: LogData, throwable: Throwable? = null) {
		ifDataDogInitialized(funName = "Logger.e") {
			dataDogLogger?.e(
				message = getFormattedMessage(logData),
				throwable = throwable,
				attributes = eventAttributeForEachLog(logData)
			)
		}
	}

	private fun prepareLogger(loggerConfigProvider: ILoggerConfig) {
		ifDataDogInitialized("prepareLogger") {
			val loggerInfo = loggerConfigProvider.providerLoggerConfig()
			val eventAttributes = loggerConfigProvider.providerLoggerEventAttributes()
			dataDogLogger = Logger.Builder()
				.setNetworkInfoEnabled(loggerInfo.networkLogEnabled)
				.setLogcatLogsEnabled(loggerInfo.logcatLogsEnabled)
				.setDatadogLogsEnabled(loggerInfo.datadogLogsEnabled)
				.setLoggerName(loggerInfo.loggerName)
				.build()
				.apply {
					eventAttributes.forEach {
						addAttribute(it.key, it.value)
					}
				}
		}
	}

	private fun eventAttributeForEachLog(logData: LogData) = mutableMapOf<String, Any?>().apply {
		attributeProvider?.let {
			this.putAll(it.getCommonAttributeForEachLog())
		}
		val extraEventAttribute = logData.currentLogAttribute
		if (extraEventAttribute.isNotEmpty()) {
			putAll(extraEventAttribute)
		}
	}

	private fun getFormattedMessage(logData: LogData) = LoggerUtils.getMessage(logData)
}

interface IDehaatLogger : ICredentialProvider, IConfigProvider, ILoggerConfig,
	ILoggerNonInitializedHandler, ILoggerExceptionHandler {

}

interface ICredentialProvider {
	fun providerCredential(): DataDogCredentials
}

interface IConfigProvider {
	fun providerConfig(): DataDogConfig
}

interface ILoggerConfig {
	fun providerLoggerConfig(): LoggerConfig
	fun providerLoggerEventAttributes(): Map<String, String>
	fun providerEachLogAttributes(): AttributeProvider
}

data class DataDogCredentials(
	val clientToken: String,
	val envName: String,
	val variant: String,
	val rumApplicationId: String?,
	val serviceName: String? = null
)

data class DataDogConfig(
	val logsEnabled: Boolean = true,
	val tracesEnabled: Boolean = true,
	val crashReportsEnabled: Boolean = true,
	val rumEnabled: Boolean = false
)

data class LoggerConfig(
	val loggerName: String,
	val networkLogEnabled: Boolean = true,
	val logcatLogsEnabled: Boolean = true,
	val datadogLogsEnabled: Boolean = true
)