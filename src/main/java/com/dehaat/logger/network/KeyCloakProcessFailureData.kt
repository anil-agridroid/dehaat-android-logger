package com.dehaat.logger.network

import com.dehaat.logger.events.EventCategory

class KeyCloakProcessFailureData(
    exception: Exception?,
    extraInfo: Map<String, String>?
) : LinkedHashMap<String, String>() {
    init {
        val map = LinkedHashMap<String, String>()
        if (exception != null) {
            map[EventCategory.LogKeyCloak.Info.EXCEPTION_MESSAGE] = exception.message
                ?: "--"
            map[EventCategory.LogKeyCloak.Info.EXCEPTION_CAUSE] =
                exception.cause?.message ?: "--"
        }
        if (extraInfo != null) {
            map.putAll(extraInfo)
        }
        putAll(map)
    }
}