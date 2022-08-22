package com.dehaat.logger.network

import com.dehaat.logger.tryCatchWithReturn
import okhttp3.Headers
import okhttp3.Request
import okhttp3.internal.EMPTY_REQUEST

fun identifyRequest(request: Request): String {
    val method = request.method
    val url = request.url
    val body = request.body
    return if (body == null || body == EMPTY_REQUEST) {
        "$method•$url"
    } else {
        val contentLength = body.contentLength()
        val contentType = body.contentType()
        "$method•$url•$contentLength•$contentType"
    }
}

fun Headers.appendKeyPrefix(prefix: String) = tryCatchWithReturn(tryBlock = {
    this.associate {
        "${prefix}${it.first}" to it.second
    }
}, catchBlock = { emptyMap() })