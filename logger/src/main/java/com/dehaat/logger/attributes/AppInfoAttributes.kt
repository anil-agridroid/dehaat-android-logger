package com.dehaat.logger.attributes

class AppInfoAttributes(
    versionCode: String,
    versionName: String,
    buildType: String,
) : LinkedHashMap<String, String>() {

    init {
        put(AppInfoAttributeKey.VERSION_CODE, versionCode)
        put(AppInfoAttributeKey.VERSION_NAME, versionName)
        put(AppInfoAttributeKey.BUILD_TYPE, buildType)
    }
}