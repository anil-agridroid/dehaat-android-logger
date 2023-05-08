package com.dehaat.logger.attributes

class ConnectionInfoAttributes(
    connectedToNetwork: String,
) : LinkedHashMap<String, String>() {

    init {
        put(ConnectionInfoAttributeKey.CONNECTED_TO_NETWORK, connectedToNetwork)
    }
}