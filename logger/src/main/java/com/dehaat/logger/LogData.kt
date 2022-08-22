package com.dehaat.logger

class LogData constructor(
    eventTypeCategory: EventCategoryName,
    eventType: EventSubCategoryName,
    eventMessage: String = "",
    map: Map<String, String> = emptyMap(),
) : LinkedHashMap<String, String>() {

    constructor(
        eventTypeCategory: EventCategoryName,
        eventType: EventSubCategoryName,
        eventMessage: String = "",
        map: Map<String, String> = emptyMap(),
        throwable: Throwable,
    ) : this(eventTypeCategory, eventType, eventMessage, map) {
        put(LoggerArgumentKey.Log.EXCEPTION_MESSAGE, throwable.message.toString())
        put(LoggerArgumentKey.Log.EXCEPTION_CAUSE, throwable.cause.toString())
    }

    init {
        put(LoggerArgumentKey.Log.EVENT_CATEGORY, eventTypeCategory)
        put(LoggerArgumentKey.Log.EVENT_TYPE, eventType)
        put(LoggerArgumentKey.Log.EVENT_MESSAGE, eventMessage)
        putAll(map)
    }

}