package com.dehaat.logger.attributes

class DeviceInfoAttributes(
    androidDeviceId: String,
    googleAdId: String,
) : LinkedHashMap<String, String>() {

    init {
        put(DeviceInfoAttributeKey.ANDROID_DEVICE_ID, androidDeviceId)
        put(DeviceInfoAttributeKey.GOOGLE_AD_ID, googleAdId)
    }
}



