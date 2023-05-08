package com.dehaat.logger

import com.dehaat.logger.attributes.provider.AttributeProvider
import com.dehaat.logger.network.DehaatNetworkLoggerInterceptor
import okhttp3.Interceptor

typealias EventCategoryName = String
typealias EventSubCategoryName = String

object DehaatLogger {

	private var iLoggerExceptionHandler: ILoggerExceptionHandler? = null
	private var attributeProvider: AttributeProvider? = null
	var iLoggerConfig: ILoggerConfig? = null
	private var iLogger: ILogger? = null

	fun initialize(iDehaatLogger: IDehaatLogger) {
		this.attributeProvider = iDehaatLogger.providerEachLogAttributes()
		this.iLoggerConfig = iDehaatLogger
		this.iLogger = iDehaatLogger
	}

	fun provideLoggerInterceptor(): Interceptor = DehaatNetworkLoggerInterceptor()
	fun provideCatchExceptionHandler() = iLoggerExceptionHandler

	internal fun i(logData: LogData, throwable: Throwable? = null) {
		iLogger?.logMessage(
			getFormattedMessage(logData) + eventAttributeForEachLog(logData), LoggingType.INFO
		)
		if (throwable != null) {
			iLogger?.logError(throwable)
		}
	}

	internal fun d(logData: LogData, throwable: Throwable? = null) {
		iLogger?.logMessage(
			getFormattedMessage(logData) + eventAttributeForEachLog(logData), LoggingType.DEBUG
		)
		if (throwable != null) {
			iLogger?.logError(throwable)
		}
	}

	internal fun v(logData: LogData, throwable: Throwable? = null) = d(logData, throwable)

	internal fun e(logData: LogData, throwable: Throwable? = null) {
		iLogger?.logMessage(
			getFormattedMessage(logData) + eventAttributeForEachLog(logData), LoggingType.ERROR
		)
		if (throwable != null) {
			iLogger?.logError(throwable)
		}
	}

	private fun eventAttributeForEachLog(logData: LogData) = mutableMapOf<String, Any?>().apply {
		attributeProvider?.let {
			this.putAll(it.getCommonAttributeForEachLog())
		}
		iLoggerConfig?.let {
			this.putAll(it.providerLoggerEventAttributes())
		}
		put("dehaat_logger_version", "1.0.4")
		val extraEventAttribute = logData.currentLogAttribute
		if (extraEventAttribute.isNotEmpty()) {
			putAll(extraEventAttribute)
		}
	}

	private fun getFormattedMessage(logData: LogData) = LoggerUtils.getMessage(logData.apply {
		attributeProvider?.getCommonAttributeForEachMsgLog()?.takeIf { it.isNotEmpty() }?.let {
			this.putAll(it)
		}
	})
}

interface IDehaatLogger : ILoggerConfig, ILoggerExceptionHandler, ILogger

interface ILoggerConfig {
	fun providerLoggerEventAttributes(): Map<String, String>
	fun providerEachLogAttributes(): AttributeProvider
	fun shouldLogAPISuccess(): Boolean
}

interface ILogger {

	fun logMessage(message: String, loggingLevel: LoggingType)

	fun logError(throwable: Throwable)
}