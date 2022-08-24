package com.dehaat.logger

class LogData constructor(
    eventTypeCategory: EventCategoryName,
    eventType: EventSubCategoryName,
    eventMessage: String = "",
    mapForMessage: Map<String, String> = emptyMap(),
    val currentLogAttribute: Map<String, String> = emptyMap(),
    throwable: Throwable? = null,
) : LinkedHashMap<String, String>() {

    init {
        put(LoggerArgumentKey.Log.EVENT_CATEGORY, eventTypeCategory)
        put(LoggerArgumentKey.Log.EVENT_TYPE, eventType)
        put(LoggerArgumentKey.Log.EVENT_MESSAGE, eventMessage)
        putAll(mapForMessage)
        throwable?.let {
            put(LoggerArgumentKey.Log.EXCEPTION_MESSAGE, it.message.toString())
            put(LoggerArgumentKey.Log.EXCEPTION_CAUSE, it.cause.toString())
        }
    }

}